package net.dummythinking.difficultLife.utils.commands;

import net.dummythinking.difficultLife.init.DLConfigSetup;
import net.dummythinking.difficultLife.utils.DLSaveStorage;
import net.dummythinking.difficultLife.utils.DLUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class CommandSetDifficulty extends CommandBase {
    @Override
    public String getCommandName() {
        return "setDifficulty";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "/setDifficulty <difficulty> ";
    }

    @Override
    public void processCommand(ICommandSender ICS, String[] stringArray) {
        int difficulty = parseIntWithMin(ICS, stringArray[0], 0);
        int maxDifficulty = (int) DLConfigSetup.DIFFICULTY_MAX;

        DLSaveStorage.commonGenericTag.setFloat("difficulty", (difficulty <= maxDifficulty) ? difficulty : maxDifficulty);

        String msg = "Difficulty was set to: " + DLUtils.getDifficultyLocalization(DLSaveStorage.commonGenericTag.getFloat("difficulty"));

        ICS.addChatMessage(new ChatComponentText(msg));
    }
}
