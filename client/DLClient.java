package difficultLife.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import difficultLife.DLCore;
import difficultLife.DLServer;
import difficultLife.init.DLConfigSetup;

public class DLClient extends DLServer{
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) 
	{
		return new GuiVanityArmor(player);
	}
	
	@Override
	public void clientInfo()
	{
		if(DLConfigSetup.ENABLE_VANITY_RENDERER)
			MinecraftForge.EVENT_BUS.register(new ClientVanityRendererHandler());
		if(DLConfigSetup.ADD_GUI_BUTTON)
			MinecraftForge.EVENT_BUS.register(new ClientGUIEventManager());
		if(DLConfigSetup.RENDER_DIFFICULTY_METER)
			MinecraftForge.EVENT_BUS.register(new ClientDisplayManagement());
		if(!Loader.isModLoaded("TConstruct"))
		{
			if(DLConfigSetup.CHANGE_HEART_RENDERING)
				MinecraftForge.EVENT_BUS.register(new ClientHeartDisplayManager());
		}else
		{
			FMLLog.info("[DifficultLife]TConstruct detected, heart rendering disabled.", DLCore.nObj());
		}
	}
	
	@Override
	public EntityPlayer getClientPlayer()
	{
		return Minecraft.getMinecraft().thePlayer;
	}


}
