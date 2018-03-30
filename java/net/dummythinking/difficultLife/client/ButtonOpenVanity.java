package net.dummythinking.difficultLife.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;

public class ButtonOpenVanity extends GuiButton {

    public ButtonOpenVanity(int p_i1021_1_, int p_i1021_2_, int p_i1021_3_, int p_i1021_4_, int p_i1021_5_, String p_i1021_6_) {
        super(p_i1021_1_, p_i1021_2_, p_i1021_3_, p_i1021_4_, p_i1021_5_, p_i1021_6_);
    }

    public void drawButton(Minecraft mc, int xx, int yy) {
        if (visible) {
            FontRenderer fontrenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(GuiVanityArmor.background);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            field_146123_n = xx >= xPosition && yy >= yPosition && xx < xPosition + width && yy < yPosition + height;
            int k = getHoverState(field_146123_n);
            GL11.glEnable(3042);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glBlendFunc(770, 771);
            if (k == 1) {
                drawTexturedModalRect(xPosition, yPosition, 200, 48, 10, 10);
            } else {
                drawTexturedModalRect(xPosition, yPosition, 210, 48, 10, 10);
                drawCenteredString(fontrenderer, displayString, xPosition + 5, yPosition + height, 0xffffff);
            }
            mouseDragged(mc, xx, yy);
        }
    }
}