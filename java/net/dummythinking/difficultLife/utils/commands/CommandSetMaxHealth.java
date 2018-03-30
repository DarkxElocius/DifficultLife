package net.dummythinking.difficultLife.utils.commands;

import net.dummythinking.difficultLife.utils.DLSaveStorage;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.util.Arrays;
import java.util.List;

public class CommandSetMaxHealth extends CommandBase {
    @Override
    public String getCommandName() {
        return "setMaxHealth";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "/setMaxHealth <health> <player>";
    }

    @Override
    public List addTabCompletionOptions(ICommandSender IC, String[] stringAr) {
        if (stringAr.length > 1) {
            return Arrays.asList(MinecraftServer.getServer().getConfigurationManager().getAllUsernames());
        }

        return null;
    }

    @Override
    public void processCommand(ICommandSender IC, String[] stringAr) {
        String playerName = (stringAr.length > 1) ? stringAr[1] : IC.getCommandSenderName();
        int health = Integer.parseInt(stringAr[0]);

        if (health <= 0) {
            IC.addChatMessage(new ChatComponentText("Health cannot be 0 or smaller!"));
            return;
        }

        for (int i = 0; i < MinecraftServer.getServer().getCurrentPlayerCount(); ++i) {
            EntityPlayerMP player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(MinecraftServer.getServer().getConfigurationManager().getAllUsernames()[i]);

            System.out.println(health);

            if (player != null) {
                if (playerName.equals(player.getDisplayName())) {
                    DLSaveStorage.setHealthTo(player, health);
                    player.setHealth(health);

                    IC.addChatMessage(new ChatComponentText(playerName + " have " + health + "healths now!"));
                }
            }
        }
    }
}
