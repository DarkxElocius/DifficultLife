package difficultLife;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import difficultLife.events.DLEventHandler;
import difficultLife.init.DLConfigSetup;
import difficultLife.init.DLItems;
import difficultLife.network.DLPacket;
import difficultLife.network.DLPacketHandler;

@Mod(
		modid = DLCore.modid,
		name = DLCore.modname,
		version = DLCore.version
	)
public class DLCore {
	
	public static final String modid = "difficultlife";
	public static final String modname = "Difficult Life";
	public static final String version = "1.1.1710.11";
	
	public static SimpleNetworkWrapper networkManager;
	
	public static Configuration modCFG;
	
	@SidedProxy(serverSide="difficultLife.DLServer",clientSide="difficultLife.client.DLClient")
	public static DLServer proxy;
	
	public static DLCore instance;
	
	@EventHandler
	public void modPreInit(FMLPreInitializationEvent event)
	{
		instance=this;
		FMLLog.info("[DifficultLife]Starting pre-initialization phase...", nObj());
		FMLLog.info("[DifficultLife]	*Generating configuration file...", nObj());
			modCFG = new Configuration(event.getSuggestedConfigurationFile());
			DLConfigSetup.setupCFG(modCFG);
		FMLLog.info("[DifficultLife]	*Finished generating configuration file!", nObj());
		
		networkManager = NetworkRegistry.INSTANCE.newSimpleChannel("DifficultLife");
		networkManager.registerMessage(DLPacketHandler.class, DLPacket.class, 0, Side.SERVER);
		networkManager.registerMessage(DLPacketHandler.class, DLPacket.class, 0, Side.CLIENT);
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
		
		FMLCommonHandler.instance().bus().register(new DLEventHandler());
		MinecraftForge.EVENT_BUS.register(new DLEventHandler());
		
		DLItems.init();
		DLEventHandler.initPotions();
		proxy.clientInfo();
	}
	
	
	
	public static Object[] nObj(Object... objects)
	{
		if(objects != null && objects.length > 0)
		{
			return objects;
		}
		return new Object[]{};
	}

}
