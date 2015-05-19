package difficultLife.client;

import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.Loader;
import difficultLife.utils.ContainerVanityArmor;

public class GuiVanityArmor extends InventoryEffectRenderer
{

    public GuiVanityArmor(EntityPlayer player)
    {
        super(new ContainerVanityArmor(player.inventory, !player.worldObj.isRemote, player));
        allowUserInput = true;
    }

    public void updateScreen()
    {
        try
        {
			Class InventoryBaubles = Class.forName("baubles.common.container.InventoryBaubles");
			Field blockEvents = InventoryBaubles.getField("blockEvents");
			blockEvents.set(((ContainerVanityArmor)inventorySlots).baubles, true);
        }
        catch(Exception e) { }
    }

    public void initGui()
    {
        buttonList.clear();
        super.initGui();
    }

    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
    {
        fontRendererObj.drawString(I18n.format("container.crafting", new Object[0]), 124, 4, 0x404040);
    }

    public void drawScreen(int par1, int par2, float par3)
    {
        super.drawScreen(par1, par2, par3);
        xSizeFloat = par1;
        ySizeFloat = par2;
    }

    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(background);
        int k = guiLeft;
        int l = guiTop;
        drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
        for(int i1 = 0; i1 < inventorySlots.inventorySlots.size(); i1++)
        {
            Slot slot = (Slot)inventorySlots.inventorySlots.get(i1);
            if(slot.getHasStack() && slot.getSlotStackLimit() == 1)
                drawTexturedModalRect(k + slot.xDisplayPosition, l + slot.yDisplayPosition, 200, 0, 16, 16);
        }
        if(Loader.isModLoaded("Baubles"))
        {
        	drawTexturedModalRect(k+97, l+7, xSize, 0, 18, ySize);
        }
        drawPlayerModel(k + 69, l + 71, 30, (float)(k + 51) - xSizeFloat, (float)((l + 75) - 50) - ySizeFloat, mc.thePlayer);
    }

    public static void drawPlayerModel(int x, int y, int scale, float yaw, float pitch, EntityLivingBase playerdrawn)
    {
        GL11.glEnable(2903);
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 50F);
        GL11.glScalef(-scale, scale, scale);
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        float f2 = playerdrawn.renderYawOffset;
        float f3 = playerdrawn.rotationYaw;
        float f4 = playerdrawn.rotationPitch;
        float f5 = playerdrawn.prevRotationYawHead;
        float f6 = playerdrawn.rotationYawHead;
        GL11.glRotatef(135F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(-135F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-(float)Math.atan(pitch / 40F) * 20F, 1.0F, 0.0F, 0.0F);
        playerdrawn.renderYawOffset = (float)Math.atan(yaw / 40F) * 20F;
        playerdrawn.rotationYaw = (float)Math.atan(yaw / 40F) * 40F;
        playerdrawn.rotationPitch = -(float)Math.atan(pitch / 40F) * 20F;
        playerdrawn.rotationYawHead = playerdrawn.rotationYaw;
        playerdrawn.prevRotationYawHead = playerdrawn.rotationYaw;
        GL11.glTranslatef(0.0F, playerdrawn.yOffset, 0.0F);
        RenderManager.instance.playerViewY = 180F;
        RenderManager.instance.renderEntityWithPosYaw(playerdrawn, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        playerdrawn.renderYawOffset = f2;
        playerdrawn.rotationYaw = f3;
        playerdrawn.rotationPitch = f4;
        playerdrawn.prevRotationYawHead = f5;
        playerdrawn.rotationYawHead = f6;
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(32826);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(3553);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    protected void actionPerformed(GuiButton button)
    {
        if(button.id == 0)
            mc.displayGuiScreen(new GuiAchievements(this, mc.thePlayer.getStatFileWriter()));
        if(button.id == 1)
            mc.displayGuiScreen(new GuiStats(this, mc.thePlayer.getStatFileWriter()));
    }

    protected void keyTyped(char par1, int par2)
    {
            super.keyTyped(par1, par2);
    }

    public static final ResourceLocation background = new ResourceLocation("difficultlife", "textures/inventory.png");
    private float xSizeFloat;
    private float ySizeFloat;

}