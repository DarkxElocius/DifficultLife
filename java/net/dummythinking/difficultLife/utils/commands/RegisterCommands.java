package net.dummythinking.difficultLife.utils.commands;

import net.dummythinking.difficultLife.init.DLConfigSetup;
import net.minecraft.command.CommandHandler;
import net.minecraft.server.MinecraftServer;

public class RegisterCommands {

    public void initCommands(MinecraftServer mcserver) {
        if (DLConfigSetup.ENABLE_COMMAND_HEAL) {
            ((CommandHandler) mcserver.getCommandManager()).registerCommand(new CommandHeal());
        }

        if (DLConfigSetup.ENABLE_COMMAND_SETDIFFICULTY) {
            ((CommandHandler) mcserver.getCommandManager()).registerCommand(new CommandSetDifficulty());
        }

        if (DLConfigSetup.ENABLE_COMMAND_SETMAXHEALTH) {
            ((CommandHandler) mcserver.getCommandManager()).registerCommand(new CommandSetMaxHealth());
        }
    }
}
