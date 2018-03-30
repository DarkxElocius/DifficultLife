package net.dummythinking.difficultLife.client;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import net.dummythinking.difficultLife.DLCore;
import net.dummythinking.difficultLife.DLServer;
import net.dummythinking.difficultLife.init.DLConfigSetup;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class DLClient extends DLServer {

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world,
                                      int x, int y, int z) {
        return new GuiVanityArmor(player);
    }

    @Override
    public void clientInfo() {
        if (DLConfigSetup.ENABLE_VANITY_RENDERER)
            MinecraftForge.EVENT_BUS.register(new ClientVanityRendererHandler());
        if (DLConfigSetup.ADD_GUI_BUTTON)
            MinecraftForge.EVENT_BUS.register(new ClientGUIEventManager());
        if (DLConfigSetup.RENDER_DIFFICULTY_METER)
            MinecraftForge.EVENT_BUS.register(new ClientDisplayManagement());
        if (!Loader.isModLoaded("TConstruct")) {
            if (DLConfigSetup.CHANGE_HEART_RENDERING && DLConfigSetup.ENABLE_DL_HEART_SYSTEM)
                MinecraftForge.EVENT_BUS.register(new net.dummythinking.difficultLife.client.ClientHeartDisplayManager());
        } else {
            FMLLog.info("[DifficultLife]TConstruct detected, heart rendering disabled.", DLCore.nObj());
        }
    }

    @Override
    public EntityPlayer getClientPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }


}
