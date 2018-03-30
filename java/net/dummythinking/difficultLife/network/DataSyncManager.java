package net.dummythinking.difficultLife.network;

import cpw.mods.fml.common.FMLLog;
import net.dummythinking.difficultLife.DLCore;
import net.dummythinking.difficultLife.init.DLConfigSetup;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.logging.log4j.Level;

import java.util.Hashtable;

public class DataSyncManager {

    public static final Hashtable<String, Integer> packetSyncManager = new Hashtable<String, Integer>();

    public static void requestGuiOpenPacket(EntityPlayer player, int id) {
        if (player.worldObj.isRemote) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("username", player.getCommandSenderName());
            tag.setInteger("guiid", id);
            DLCore.networkManager.sendToServer(new DLPacket().setID("guiRequest").setNBT(tag));
        } else {
            FMLLog.log(Level.DEBUG, "GuiOpen packet can only be requested from CLIENT side, aborting!", DLCore.nObj());
        }
    }

    public static void requestServerToClientMessage(String messageID, EntityPlayerMP client, NBTTagCompound message, boolean requirePacket) {
        if (requirePacket) {
            packetSyncManager.put(messageID + "_" + client.getCommandSenderName(), 0);
            DLCore.networkManager.sendTo(new DLPacket().setID(messageID).setNBT(message), client);
        }
        if (packetSyncManager.containsKey(messageID + "_" + client.getCommandSenderName())) {
            packetSyncManager.put(messageID + "_" + client.getCommandSenderName(), packetSyncManager.get(messageID + "_" + client.getCommandSenderName()) + 1);
            if (packetSyncManager.get(messageID + "_" + client.getCommandSenderName()) >= DLConfigSetup.PACKET_DELAY) {
                packetSyncManager.put(messageID + "_" + client.getCommandSenderName(), 0);
                DLCore.networkManager.sendTo(new DLPacket().setID(messageID).setNBT(message), client);
            }
        } else {
            packetSyncManager.put(messageID, 0);
        }
    }

}
