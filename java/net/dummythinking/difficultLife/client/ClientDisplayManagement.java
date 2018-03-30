package net.dummythinking.difficultLife.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.dummythinking.difficultLife.DLCore;
import net.dummythinking.difficultLife.init.DLConfigSetup;
import net.dummythinking.difficultLife.utils.DLSaveStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import org.lwjgl.opengl.GL11;

public class ClientDisplayManagement {

    public static final ResourceLocation diffManager = new ResourceLocation(DLCore.modid, "textures/diffMeter.png");
    double zLevel = 0D;

    @SubscribeEvent
    public void renderTick(RenderGameOverlayEvent.Post event) {
        if (Minecraft.getMinecraft().theWorld != null && event.type == ElementType.ALL) {
            ScaledResolution res = event.resolution;
            int width = res.getScaledWidth();
            int height = res.getScaledHeight();
            GL11.glPushMatrix();

            Minecraft.getMinecraft().renderEngine.bindTexture(diffManager);
            int left = width - DLConfigSetup.DIFFICULTY_GUI_HORISONTAL;
            int top = height / 3 - DLConfigSetup.DIFFICULTY_GUI_VERTICAL;

            if (!DLConfigSetup.DIFFICULTY_GUI_RIGHT)
                left = DLConfigSetup.DIFFICULTY_GUI_HORISONTAL;
            if (!DLConfigSetup.DIFFICULTY_GUI_TOP)
                top = DLConfigSetup.DIFFICULTY_GUI_VERTICAL;

            this.drawTexturedModalRect(left, top, 0, 0, 8, 64);
            float currentDif = DLSaveStorage.commonGenericTag.getFloat("difficulty");
            float percentage = currentDif / DLConfigSetup.DIFFICULTY_MAX;
            int textureRect = MathHelper.floor_float(percentage * 64);
            if (textureRect > 64) textureRect = 64;
            this.drawTexturedModalRect(left, top + (64 - textureRect), 8, 64 - textureRect, 8, textureRect);

            float s = 0.5F;
            GL11.glScalef(s, s, s);

            String str = "dl.hahaha+";

            if (currentDif < DLConfigSetup.DIFFICULTY_BOUNDS[9])
                str = "dl.hahaha";
            if (currentDif < DLConfigSetup.DIFFICULTY_BOUNDS[8])
                str = "dl.imcomingforyou";
            if (currentDif < DLConfigSetup.DIFFICULTY_BOUNDS[7])
                str = "dl.iseeyou";
            if (currentDif < DLConfigSetup.DIFFICULTY_BOUNDS[6])
                str = "dl.impossible";
            if (currentDif < DLConfigSetup.DIFFICULTY_BOUNDS[5])
                str = "dl.insane";
            if (currentDif < DLConfigSetup.DIFFICULTY_BOUNDS[4])
                str = "dl.veryHard";
            if (currentDif < DLConfigSetup.DIFFICULTY_BOUNDS[3])
                str = "dl.hard";
            if (currentDif < DLConfigSetup.DIFFICULTY_BOUNDS[2])
                str = "dl.medium";
            if (currentDif < DLConfigSetup.DIFFICULTY_BOUNDS[1])
                str = "dl.easy";
            if (currentDif < DLConfigSetup.DIFFICULTY_BOUNDS[0])
                str = "dl.veryEasy";

            Minecraft.getMinecraft().fontRenderer.drawString(StatCollector.translateToLocal(str), (int) ((left - str.length() * 2.5F + 8) / s), (int) ((top + 64) / s) + 1, 0xffffff);

            GL11.glPopMatrix();
        }
    }

    public void drawTexturedModalRect(int p_73729_1_, int p_73729_2_, int p_73729_3_, int p_73729_4_, int p_73729_5_, int p_73729_6_) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double) (p_73729_1_ + 0), (double) (p_73729_2_ + p_73729_6_), (double) this.zLevel, (double) ((float) (p_73729_3_ + 0) * f), (double) ((float) (p_73729_4_ + p_73729_6_) * f1));
        tessellator.addVertexWithUV((double) (p_73729_1_ + p_73729_5_), (double) (p_73729_2_ + p_73729_6_), (double) this.zLevel, (double) ((float) (p_73729_3_ + p_73729_5_) * f), (double) ((float) (p_73729_4_ + p_73729_6_) * f1));
        tessellator.addVertexWithUV((double) (p_73729_1_ + p_73729_5_), (double) (p_73729_2_ + 0), (double) this.zLevel, (double) ((float) (p_73729_3_ + p_73729_5_) * f), (double) ((float) (p_73729_4_ + 0) * f1));
        tessellator.addVertexWithUV((double) (p_73729_1_ + 0), (double) (p_73729_2_ + 0), (double) this.zLevel, (double) ((float) (p_73729_3_ + 0) * f), (double) ((float) (p_73729_4_ + 0) * f1));
        tessellator.draw();
    }

}
