package difficultLife.command;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import difficultLife.init.DLConfigSetup;
import difficultLife.utils.DLSaveStorage;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import scala.annotation.meta.param;

public class setDifficulty implements ICommand{

	@Override
	public int compareTo(Object o) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "setDifficulty";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "Usage: /setDifficulty (Float)[difficulty]";
	}

	@Override
	public List getCommandAliases() {
		List<String> alias = new ArrayList<String>();
		alias.add("setdifficulty");
		return alias;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] parameters) {
		if (parameters.length == 1) {
			float difficulty = 0;
			try {
				difficulty = Float.parseFloat(parameters[0]);
				
				if (difficulty >= DLConfigSetup.DIFFICULTY_DEFAULT && difficulty <= DLConfigSetup.DIFFICULTY_MAX) {
					DLSaveStorage.setDifficulty(difficulty);
				} else {
					sender.addChatMessage(new ChatComponentText("§4The difficulty should be in the config files boundaries: MIN = §a0" + " §4MAX = §a" + DLConfigSetup.DIFFICULTY_MAX));
				}
			} catch (Exception e) {
				sender.addChatMessage(new ChatComponentText("§4The input difficulty is malformed, is must be a float e.g '10.2'"));
			}
		} else {
			sender.addChatMessage(new ChatComponentText("§4Usage: /setDifficulty (Float) [difficulty]"));
		}
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		if (sender instanceof EntityPlayerMP) {
			return MinecraftServer.getServer().getConfigurationManager().func_152596_g((((EntityPlayer) sender).getGameProfile()));
		} else if (sender instanceof MinecraftServer) {
			return true;
		}
		return false;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] parameters) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] sender, int parameters) {
		return false;
	}

}
