package net.dummythinking.difficultLife.item;

import net.dummythinking.difficultLife.DLCore;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class GlassArmor extends ItemArmor {

    public GlassArmor(ArmorMaterial material, int type, int renderID) {
        super(material, type, renderID);
    }

    @Override
    public String getArmorTexture(ItemStack itemstack, Entity entity, int slot, String type) {
        return DLCore.modid + ":textures/items/armor/null.png";
    }

}
