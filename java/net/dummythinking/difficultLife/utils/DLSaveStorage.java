package net.dummythinking.difficultLife.utils;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import net.dummythinking.difficultLife.DLCore;
import net.dummythinking.difficultLife.init.DLConfigSetup;
import net.dummythinking.difficultLife.network.DataSyncManager;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Save;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

public class DLSaveStorage {

    public static NBTTagCompound commonGenericTag; //Should? be same for both server and client
    public static Hashtable<String, NBTTagCompound> playerData = new Hashtable<String, NBTTagCompound>(); //SERVER-SIDE ONLY!
    public static NBTTagCompound clientPlayerData; //CLIENT-SIDE ONLY!

    public static void manageDifficultyIncrease(float increasedBy) {
        if (commonGenericTag == null || commonGenericTag.hasNoTags()) {
            commonGenericTag = new NBTTagCompound();
            commonGenericTag.setFloat("difficulty", DLConfigSetup.DIFFICULTY_DEFAULT);
            if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
                for (int i = 0; i < MinecraftServer.getServer().getCurrentPlayerCount(); ++i) {
                    EntityPlayerMP player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(MinecraftServer.getServer().getConfigurationManager().getAllUsernames()[i]);

                    if (player != null) {
                        DataSyncManager.requestServerToClientMessage("worldData", player, commonGenericTag, true);
                    }
                }
            }
        }

        float difficulty = commonGenericTag.getFloat("difficulty");
        if (difficulty < DLConfigSetup.DIFFICULTY_MAX) {
            difficulty += increasedBy;
        }

        commonGenericTag.setFloat("difficulty", difficulty);
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
            for (int i = 0; i < MinecraftServer.getServer().getCurrentPlayerCount(); ++i) {
                EntityPlayerMP player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(MinecraftServer.getServer().getConfigurationManager().getAllUsernames()[i]);
                if (player != null) {
                    DataSyncManager.requestServerToClientMessage("worldData", player, commonGenericTag, false);
                }
            }
        }
    }

    public static void increasePlayerHeartsBy1(EntityPlayer p) {
        setHealthTo(p, (int) p.getMaxHealth() + 2);
    }

    public static void setHealthTo(EntityPlayer p, int health) {
        if (p instanceof EntityPlayerMP) {
            NBTTagCompound tag = playerData.get(p.getCommandSenderName());
            if (tag == null || tag.hasNoTags()) {
                tag = new NBTTagCompound();
                tag.setInteger("health", DLConfigSetup.PLAYER_HEARTS_GENERIC * 2);
                playerData.put(p.getCommandSenderName(), tag);
                DataSyncManager.requestServerToClientMessage("playerData", (EntityPlayerMP) p, DLSaveStorage.playerData.get(p.getCommandSenderName()), true);
            }
            int hp = tag.getInteger("health");
            if (hp + 2 < DLConfigSetup.MAX_ADDITIONAL_HEARTS && DLConfigSetup.MAX_ADDITIONAL_HEARTS != -1)
                return;

            int diff = health - tag.getInteger("health");

            tag.setInteger("health", tag.getInteger("health") + diff);
            AttributeModifier mod = p.getEntityAttribute(SharedMonsterAttributes.maxHealth).getModifier(DLUtils.modifierID);
            if (mod != null) {
                AttributeModifier mCopy = new AttributeModifier(mod.getID(), mod.getName(), mod.getAmount() + diff, mod.getOperation());
                p.getEntityAttribute(SharedMonsterAttributes.maxHealth).removeModifier(mod);
                p.getEntityAttribute(SharedMonsterAttributes.maxHealth).applyModifier(mCopy);
            }

            DataSyncManager.requestServerToClientMessage("playerData", (EntityPlayerMP) p, DLSaveStorage.playerData.get(p.getCommandSenderName()), true);
        } else {
            FMLLog.warning("[DifficultLife]Attempts to call playerhealth on CLIENT side is not cool. Canceled.", DLCore.nObj());
        }
    }

    public static int getSuggestedAmmoundOfHealthForPlayer(EntityPlayer p) {
        if (p instanceof EntityPlayerMP) {
            NBTTagCompound tag = playerData.get(p.getCommandSenderName());
            if (tag == null || tag.hasNoTags()) {
                tag = new NBTTagCompound();
                tag.setInteger("health", DLConfigSetup.PLAYER_HEARTS_GENERIC * 2);
                playerData.put(p.getCommandSenderName(), tag);
                DataSyncManager.requestServerToClientMessage("playerData", (EntityPlayerMP) p, DLSaveStorage.playerData.get(p.getCommandSenderName()), true);
            }
            return tag.getInteger("health");
        } else {
            FMLLog.warning("[DifficultLife]Attempts to call playerhealth on CLIENT side is not cool. Canceled.", DLCore.nObj());
        }
        return 20;
    }

    public static boolean generatePlayerSaveFile(PlayerEvent.LoadFromFile event) {
        try {
            EntityPlayer p = event.entityPlayer; //Player reference for easier coding
            if (p != null && !p.worldObj.isRemote) //If we are working on a SERVER
            {
                if (DLConfigSetup.FML_LOG) {
                    FMLLog.info("[DifficultLife]Beginning loading of the file for player %s on side %s", DLCore.nObj(p, FMLCommonHandler.instance().getEffectiveSide()));
                }

                File f = event.playerDirectory;
                if (f != null) {
                    String fPath = f.getAbsolutePath();
                    File worldSaveFile = new File(fPath + "//DifficultLifeData_" + p.getCommandSenderName() + ".dat");
                    if (worldSaveFile.isDirectory()) {
                        throw new IOException("File is a directory! Please, delete the DifficultLifeData folder in your save and launch the game again!");
                    }
                    if (!worldSaveFile.exists()) {
                        FMLLog.log(Level.WARN, "[DifficultLife]	*Server save file not found. This is completely normal if this is your first launch of the game. Otherwise please, report this to the author!", DLCore.nObj());
                        worldSaveFile.createNewFile();
                    }
                    FileInputStream iStream = new FileInputStream(worldSaveFile);
                    try {
                        playerData.put(p.getCommandSenderName(), CompressedStreamTools.readCompressed(iStream));
                    } catch (IOException e) {
                        FMLLog.log(Level.WARN, "[DifficultLife]	*Unable to read NBT from server save file. This is completely normal if this is your first launch of the game. Otherwise please, report this to the author!", DLCore.nObj());
                        playerData.put(p.getCommandSenderName(), new NBTTagCompound());
                    } finally {
                        iStream.close();
                    }
                    return true;
                }
                return false;
            }
            return false;
        } catch (Exception e) {
            FMLLog.log(Level.FATAL, e, "[DifficultLife]	*Catched an exception during load of server file!", DLCore.nObj());
            return false;
        }
    }

    public static boolean saveServerPlayerFile(PlayerEvent.SaveToFile event) {
        try {
            EntityPlayer p = event.entityPlayer; //Player reference for easier coding
            if (p != null && !p.worldObj.isRemote) //If we are working on a SERVER
            {
                if (DLConfigSetup.FML_LOG) {
                    FMLLog.info("[DifficultLife]Beginning saving of the file for player %s on side %s", DLCore.nObj(p, FMLCommonHandler.instance().getEffectiveSide()));
                }

                File f = event.playerDirectory;
                if (f != null) {
                    String fPath = f.getAbsolutePath();
                    File worldSaveFile = new File(fPath + "//DifficultLifeData_" + p.getCommandSenderName() + ".dat");
                    if (worldSaveFile.isDirectory()) {
                        throw new IOException("File is a directory! Please, delete the DifficultLifeData folder in your save and launch the game again!");
                    }
                    if (!worldSaveFile.exists()) {
                        worldSaveFile.createNewFile();
                    }
                    FileOutputStream oStream = new FileOutputStream(worldSaveFile);
                    try {
                        CompressedStreamTools.writeCompressed(playerData.get(p.getCommandSenderName()), oStream);
                    } catch (IOException e) {
                        throw new IOException("Unable to write NBT to server save file! Please, delete the DifficultLifeData.dat in your save folder and restart the server!");
                    } catch (NullPointerException e) {
                        throw new IOException("Server NBT does not exists! This is impossible, please, report this to the author!" + e.getMessage());
                    } finally {
                        oStream.close();
                    }
                    return true;
                }
                return false;
            }
            return false;
        } catch (Exception e) {
            FMLLog.log(Level.FATAL, e, "[DifficultLife]	*Catched an exception during save of server file!", DLCore.nObj());
            return false;
        }
    }

    public static boolean generateServerWorldFile(Load event) {
        try {
            World w = event.world; //World reference for easier coding
            if (w != null && !w.isRemote && w.provider != null && w.provider.dimensionId == 0) //If we are working on a SERVER and with the OVERWORLD.
            {
                if (DLConfigSetup.FML_LOG) {
                    FMLLog.info("[DifficultLife]Beginning loading of the file for world %s on side %s", DLCore.nObj(w, FMLCommonHandler.instance().getEffectiveSide()));
                }

                File f = event.world.getSaveHandler().getWorldDirectory();
                if (f != null) {
                    String fPath = f.getAbsolutePath();
                    File worldSaveFile = new File(fPath + "//DifficultLifeData.dat");
                    if (worldSaveFile.isDirectory()) {
                        throw new IOException("File is a directory! Please, delete the DifficultLifeData folder in your save and launch the game again!");
                    }
                    if (!worldSaveFile.exists()) {
                        FMLLog.log(Level.WARN, "[DifficultLife]	*Server save file not found. This is completely normal if this is your first launch of the game. Otherwise please, report this to the author!", DLCore.nObj());
                        worldSaveFile.createNewFile();
                    }
                    FileInputStream iStream = new FileInputStream(worldSaveFile);
                    try {
                        commonGenericTag = CompressedStreamTools.readCompressed(iStream);
                    } catch (IOException e) {
                        FMLLog.log(Level.WARN, "[DifficultLife]	*Unable to read NBT from server save file. This is completely normal if this is your first launch of the game. Otherwise please, report this to the author!", DLCore.nObj());
                        commonGenericTag = new NBTTagCompound();
                    } finally {
                        iStream.close();
                    }
                    return true;
                }
                return false;
            }
            return false;
        } catch (Exception e) {
            FMLLog.log(Level.FATAL, e, "[DifficultLife]	*Catched an exception during load of server file!", DLCore.nObj());
            return false;
        }
    }

    public static boolean saveServerWorldFile(Save event) {
        try {
            World w = event.world;
            if (w != null && !w.isRemote && w.provider != null && w.provider.dimensionId == 0) {
                if (DLConfigSetup.FML_LOG) {
                    FMLLog.info("[DifficultLife]Beginning saving of the file for world %s on side %s", DLCore.nObj(w, FMLCommonHandler.instance().getEffectiveSide()));
                }

                File f = event.world.getSaveHandler().getWorldDirectory();
                if (f != null) {
                    String fPath = f.getAbsolutePath();
                    File worldSaveFile = new File(fPath + "//DifficultLifeData.dat");
                    if (worldSaveFile.isDirectory()) {
                        throw new IOException("File is a directory! Please, delete the DifficultLifeData folder in your save and launch the game again!");
                    }
                    if (!worldSaveFile.exists()) {
                        throw new IOException("File does not exists! This means, that it got deleted after being created! Make sure, that your antivirus does not recognizes the file as a virus!");
                    }
                    FileOutputStream oStream = new FileOutputStream(worldSaveFile);
                    try {
                        CompressedStreamTools.writeCompressed(commonGenericTag, oStream);
                    } catch (IOException e) {
                        throw new IOException("Unable to write NBT to server save file! Please, delete the DifficultLifeData.dat in your save folder and restart the server!");
                    } catch (NullPointerException e) {
                        throw new IOException("Server NBT does not exists! This is impossible, please, report this to the author!" + e.getMessage());
                    } finally {
                        oStream.close();
                    }
                    return true;
                }
                return false;
            }
            return false;
        } catch (Exception e) {
            FMLLog.log(Level.FATAL, e, "[DifficultLife]	*Catched an exception during save of server file!", DLCore.nObj());
            return false;
        }
    }
}
