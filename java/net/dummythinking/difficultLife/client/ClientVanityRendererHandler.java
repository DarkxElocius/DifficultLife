package net.dummythinking.difficultLife.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.dummythinking.difficultLife.utils.DLSaveStorage;
import net.dummythinking.difficultLife.utils.InventoryVanityArmor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderPlayerEvent.SetArmorModel;
import org.lwjgl.opengl.GL11;

import java.util.Hashtable;

public class ClientVanityRendererHandler {

    public Hashtable<String, InventoryVanityArmor> playersInventory = new Hashtable<String, InventoryVanityArmor>();
    public InventoryVanityArmor vanityInventory;
    public int aort;


    @SubscribeEvent
    public void renderPlayerModel(SetArmorModel event) {
        if (event.entityPlayer == Minecraft.getMinecraft().thePlayer) {
            if (vanityInventory == null) {
                vanityInventory = new InventoryVanityArmor(event.entityPlayer);
                vanityInventory.readFromNBT(DLSaveStorage.clientPlayerData);
            }
            if (event.entityPlayer.ticksExisted % 100 == 0) {
                vanityInventory.readFromNBT(DLSaveStorage.clientPlayerData);
            }


            int slot = 3 - event.slot;
            ItemStack stk = vanityInventory.getStackInSlot(slot);

            if (stk != null) {
                Item item = stk.getItem();

                if (item instanceof ItemArmor) {
                    event.result = 0;
                    ItemArmor itemarmor = (ItemArmor) item;
                    Minecraft.getMinecraft().renderEngine.bindTexture(RenderBiped.getArmorResource(event.entityPlayer, stk, slot, null));
                    RenderPlayer rp = (RenderPlayer) RenderManager.instance.getEntityRenderObject(Minecraft.getMinecraft().thePlayer);
                    ModelBiped modelbiped = slot == 2 ? rp.modelArmor : rp.modelArmorChestplate;
                    modelbiped.bipedHead.showModel = slot == 0;
                    modelbiped.bipedHeadwear.showModel = slot == 0;
                    modelbiped.bipedBody.showModel = slot == 1 || slot == 2;
                    modelbiped.bipedRightArm.showModel = slot == 1;
                    modelbiped.bipedLeftArm.showModel = slot == 1;
                    modelbiped.bipedRightLeg.showModel = slot == 2 || slot == 3;
                    modelbiped.bipedLeftLeg.showModel = slot == 2 || slot == 3;
                    modelbiped = net.minecraftforge.client.ForgeHooksClient.getArmorModel(event.entityPlayer, stk, slot, modelbiped);
                    rp.setRenderPassModel(modelbiped);

                    modelbiped.onGround = event.entityPlayer.swingProgress;
                    modelbiped.isRiding = event.entityPlayer.isRiding();
                    modelbiped.isChild = event.entityPlayer.isChild();

                    int j = itemarmor.getColor(stk);
                    if (j != -1) {
                        float f1 = (float) (j >> 16 & 255) / 255.0F;
                        float f2 = (float) (j >> 8 & 255) / 255.0F;
                        float f3 = (float) (j & 255) / 255.0F;
                        GL11.glColor3f(f1, f2, f3);

                        if (stk.isItemEnchanted()) {
                            event.result = 31;
                            return;
                        }

                        event.result = 16;
                        return;
                    }

                    GL11.glColor3f(1.0F, 1.0F, 1.0F);

                    if (stk.isItemEnchanted()) {
                        event.result = 15;
                        return;
                    }

                    event.result = 11;
                    return;
                }
                event.result = 0;
            }

        } else {
            InventoryVanityArmor ivm = null;
            if (!playersInventory.containsKey(event.entityPlayer.getCommandSenderName())) {
                ivm = new InventoryVanityArmor(event.entityPlayer);
                if (DLSaveStorage.playerData.containsKey(event.entityPlayer.getCommandSenderName()))
                    ivm.readFromNBT(DLSaveStorage.playerData.get(event.entityPlayer.getCommandSenderName()));
                playersInventory.put(event.entityPlayer.getCommandSenderName(), ivm);
            } else
                ivm = playersInventory.get(event.entityPlayer.getCommandSenderName());
            if (event.entityPlayer.ticksExisted % 20 == 0) {
                if (DLSaveStorage.playerData.containsKey(event.entityPlayer.getCommandSenderName()))
                    ivm.readFromNBT(DLSaveStorage.playerData.get(event.entityPlayer.getCommandSenderName()));
            }


            int slot = 3 - event.slot;
            ItemStack stk = ivm.getStackInSlot(slot);

            if (stk != null) {
                Item item = stk.getItem();

                if (item instanceof ItemArmor) {
                    event.result = 0;
                    ItemArmor itemarmor = (ItemArmor) item;
                    Minecraft.getMinecraft().renderEngine.bindTexture(RenderBiped.getArmorResource(event.entityPlayer, stk, slot, null));
                    RenderPlayer rp = (RenderPlayer) RenderManager.instance.getEntityRenderObject(Minecraft.getMinecraft().thePlayer);
                    ModelBiped modelbiped = slot == 2 ? rp.modelArmor : rp.modelArmorChestplate;
                    modelbiped.bipedHead.showModel = slot == 0;
                    modelbiped.bipedHeadwear.showModel = slot == 0;
                    modelbiped.bipedBody.showModel = slot == 1 || slot == 2;
                    modelbiped.bipedRightArm.showModel = slot == 1;
                    modelbiped.bipedLeftArm.showModel = slot == 1;
                    modelbiped.bipedRightLeg.showModel = slot == 2 || slot == 3;
                    modelbiped.bipedLeftLeg.showModel = slot == 2 || slot == 3;
                    modelbiped = net.minecraftforge.client.ForgeHooksClient.getArmorModel(event.entityPlayer, stk, slot, modelbiped);
                    rp.setRenderPassModel(modelbiped);

                    modelbiped.onGround = event.entityPlayer.swingProgress;
                    modelbiped.isRiding = event.entityPlayer.isRiding();
                    modelbiped.isChild = event.entityPlayer.isChild();

                    int j = itemarmor.getColor(stk);
                    if (j != -1) {
                        float f1 = (float) (j >> 16 & 255) / 255.0F;
                        float f2 = (float) (j >> 8 & 255) / 255.0F;
                        float f3 = (float) (j & 255) / 255.0F;
                        GL11.glColor3f(f1, f2, f3);

                        if (stk.isItemEnchanted()) {
                            event.result = 31;
                            return;
                        }

                        event.result = 16;
                        return;
                    }

                    GL11.glColor3f(1.0F, 1.0F, 1.0F);

                    if (stk.isItemEnchanted()) {
                        event.result = 15;
                        return;
                    }

                    event.result = 11;
                    return;
                }
                event.result = 0;
            }
        }
    }

}
