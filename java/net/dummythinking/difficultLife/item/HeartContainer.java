package net.dummythinking.difficultLife.item;

import net.dummythinking.difficultLife.utils.DLSaveStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class HeartContainer extends Item {

    public ItemStack onItemRightClick(ItemStack stk, World w, EntityPlayer p) {
        if (!w.isRemote) {
            DLSaveStorage.increasePlayerHeartsBy1(p);
            p.inventory.decrStackSize(p.inventory.currentItem, 1);
        }

        return stk;
    }

}
