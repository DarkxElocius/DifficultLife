package difficultLife.utils;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import difficultLife.DLCore;
import difficultLife.network.DLPacket;

public class DLUtils {
	
	public static final UUID modifierID = UUID.fromString("af286358-cd54-11e4-afdc-1681e6b88ec1");
	
	
	public static void handleGuiRequest(DLPacket pkt)
	{
		NBTTagCompound tg = pkt.syncedTag;
		int id = tg.getInteger("guiid");
		if(id == 0)
		{
			EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(tg.getString("username"));
			player.openGui(DLCore.instance, tg.getInteger("guiid"), player.worldObj, 0, 0, 0);
		}
		if(id == 1)
		{
			EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(tg.getString("username"));
	        player.openContainer.onContainerClosed(player);
	        player.openContainer = player.inventoryContainer;
		}
	}
}
