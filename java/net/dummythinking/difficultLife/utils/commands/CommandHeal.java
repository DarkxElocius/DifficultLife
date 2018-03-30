package net.dummythinking.difficultLife.utils.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.util.Arrays;
import java.util.List;

public class CommandHeal extends CommandBase {
    @Override
    public String getCommandName() {
        return "heal";
    }

    @Override
    public String getCommandUsage(ICommandSender IC) {
        return "/heal <player>";
    }

    @Override
    public List addTabCompletionOptions(ICommandSender IC, String[] stringAr) {
        return Arrays.asList(MinecraftServer.getServer().getConfigurationManager().getAllUsernames());
    }

    @Override
    public void processCommand(ICommandSender IC, String[] stringAr) {
        String playerName = IC.getCommandSenderName();

        if (stringAr.length > 0) {
            playerName = stringAr[0];
        }

        for (int i = 0; i < MinecraftServer.getServer().getCurrentPlayerCount(); ++i) {
            EntityPlayerMP player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(MinecraftServer.getServer().getConfigurationManager().getAllUsernames()[i]);

            if (player != null) {
                if (playerName.equals(player.getDisplayName())) {
                    player.setHealth(player.getMaxHealth());
                    player.getFoodStats().setFoodLevel(20);

                    IC.addChatMessage(new ChatComponentText(playerName + " was healed!"));
                }
            }
        }
    }
}
