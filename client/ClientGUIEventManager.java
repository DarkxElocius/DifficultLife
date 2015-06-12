package difficultLife.client;

import java.lang.reflect.Method;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import difficultLife.network.DataSyncManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.resources.I18n;

public class ClientGUIEventManager {
	
	@SuppressWarnings("unchecked")
	@SubscribeEvent
    public void guiPostInit(net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Post event)
    {
        if((event.gui instanceof GuiInventory) || (event.gui instanceof GuiVanityArmor))
        {
            int xSize = 176;
            int ySize = 166;
            int guiLeft = (event.gui.width - xSize) / 2;
            int guiTop = (event.gui.height - ySize) / 2;
            if(!event.gui.mc.thePlayer.getActivePotionEffects().isEmpty() && isNeiHidden())
                guiLeft = 160 + (event.gui.width - xSize - 200) / 2;
            
            if(event.gui instanceof GuiVanityArmor)
            	guiLeft += 18;
            event.buttonList.add(new ButtonOpenVanity(58, guiLeft + 26, guiTop + 68, 10, 10, I18n.format((event.gui instanceof GuiInventory) ? "button.vanity" : "button.normal", new Object[0])));
        }
    }
    
	@SubscribeEvent
    public void guiPostAction(net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Post event)
    {
        if((event.gui instanceof GuiInventory) && event.button.id == 58)
            DataSyncManager.requestGuiOpenPacket(Minecraft.getMinecraft().thePlayer, 0);
        if((event.gui instanceof GuiVanityArmor) && event.button.id == 58)
        {
            event.gui.mc.displayGuiScreen(new GuiInventory(event.gui.mc.thePlayer));
            DataSyncManager.requestGuiOpenPacket(Minecraft.getMinecraft().thePlayer, 1);
        }
    }
    
    boolean isNeiHidden()
    {
        boolean hidden = true;
        try
        {
            if(isNEIHidden == null)
            {
                Class<?> fake = Class.forName("codechicken.nei.NEIClientConfig");
                isNEIHidden = fake.getMethod("isHidden", new Class[0]);
            }
            hidden = ((Boolean)isNEIHidden.invoke(null, new Object[0])).booleanValue();
        }
        catch(Exception ex) { }
        return hidden;
    }

    static Method isNEIHidden;

}
