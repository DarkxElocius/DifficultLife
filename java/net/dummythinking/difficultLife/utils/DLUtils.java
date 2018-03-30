package net.dummythinking.difficultLife.utils;

import net.dummythinking.difficultLife.DLCore;
import net.dummythinking.difficultLife.init.DLConfigSetup;
import net.dummythinking.difficultLife.network.DLPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StatCollector;

import java.util.UUID;

public class DLUtils {

    public static final UUID modifierID = UUID.fromString("af286358-cd54-11e4-afdc-1681e6b88ec1");


    public static void handleGuiRequest(DLPacket pkt) {
        NBTTagCompound tg = pkt.syncedTag;
        int id = tg.getInteger("guiid");
        if (id == 0) {
            EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(tg.getString("username"));
            player.openGui(DLCore.instance, tg.getInteger("guiid"), player.worldObj, 0, 0, 0);
        }
        if (id == 1) {
            EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(tg.getString("username"));
            player.openContainer.onContainerClosed(player);
            player.openContainer = player.inventoryContainer;
        }
    }

    public static String getDifficultyLocalization(float currentDif) {
        String str = "dl.hahaha+";

        if (currentDif < DLConfigSetup.DIFFICULTY_BOUNDS[9])
            str = "dl.hahaha";
        if (currentDif < DLConfigSetup.DIFFICULTY_BOUNDS[8])
            str = "dl.imcomingforyou";
        if (currentDif < DLConfigSetup.DIFFICULTY_BOUNDS[7])
            str = "dl.iseeyou";
        if (currentDif < DLConfigSetup.DIFFICULTY_BOUNDS[6])
            str = "dl.impossible";
        if (currentDif < DLConfigSetup.DIFFICULTY_BOUNDS[5])
            str = "dl.insane";
        if (currentDif < DLConfigSetup.DIFFICULTY_BOUNDS[4])
            str = "dl.veryHard";
        if (currentDif < DLConfigSetup.DIFFICULTY_BOUNDS[3])
            str = "dl.hard";
        if (currentDif < DLConfigSetup.DIFFICULTY_BOUNDS[2])
            str = "dl.medium";
        if (currentDif < DLConfigSetup.DIFFICULTY_BOUNDS[1])
            str = "dl.easy";
        if (currentDif < DLConfigSetup.DIFFICULTY_BOUNDS[0])
            str = "dl.veryEasy";

        return StatCollector.translateToLocal(str);
    }
}
