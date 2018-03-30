package net.dummythinking.difficultLife.utils;

import net.dummythinking.difficultLife.network.DataSyncManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class InventoryVanityArmor implements IInventory {

    public ItemStack[] armorItems = new ItemStack[4];
    public EntityPlayer owner;

    public InventoryVanityArmor(EntityPlayer e) {
        owner = e;
    }

    public void readFromNBT(NBTTagCompound tag) {
        for (int i = 0; i < this.armorItems.length; ++i) {
            this.armorItems[i] = null;
            if (tag.hasKey("item_tag_" + i)) {
                NBTTagCompound itemTag = tag.getCompoundTag("item_tag_" + i);
                armorItems[i] = ItemStack.loadItemStackFromNBT(itemTag);
            }
        }

    }

    public void writeToNBT(NBTTagCompound tag) {
        for (int i = 0; i < this.armorItems.length; ++i) {
            tag.removeTag("item_tag_" + i);
            if (this.armorItems[i] != null) {
                NBTTagCompound tg = new NBTTagCompound();
                armorItems[i].writeToNBT(tg);
                tag.setTag("item_tag_" + i, tg);
            }
        }
    }

    @Override
    public int getSizeInventory() {
        return armorItems.length;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return armorItems.length <= i ? armorItems[3] : armorItems[i];
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {

        if (this.armorItems[i] != null) {
            ItemStack itemstack;
            if (this.armorItems[i].stackSize <= j) {
                itemstack = this.armorItems[i];
                this.armorItems[i] = null;
                this.markDirty();
                return itemstack;
            } else {
                itemstack = this.armorItems[i].splitStack(j);
                if (this.armorItems[i].stackSize == 0) {
                    this.armorItems[i] = null;
                }
                this.markDirty();
                return itemstack;
            }
        } else {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        if (this.armorItems[i] != null) {
            ItemStack itemstack = this.armorItems[i];
            this.armorItems[i] = null;
            this.markDirty();
            return itemstack;
        } else {
            this.markDirty();
            return null;
        }

    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        armorItems[i] = itemstack;
        if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
            itemstack.stackSize = this.getInventoryStackLimit();
        }
        this.markDirty();
    }

    @Override
    public String getInventoryName() {
        // TODO Auto-generated method stub
        return "Vanity Armor";
    }

    @Override
    public boolean hasCustomInventoryName() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        // TODO Auto-generated method stub
        return 1;
    }

    @Override
    public void markDirty() {
        if (!this.owner.worldObj.isRemote) {
            //this.writeToNBT(DLSaveStorage.playerData.get(this.owner));
            DataSyncManager.requestServerToClientMessage("playerData", (EntityPlayerMP) owner, DLSaveStorage.playerData.get(owner.getCommandSenderName()), true);
        }
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return true;
    }

    @Override
    public void openInventory() {
        this.markDirty();
    }

    @Override
    public void closeInventory() {
        this.markDirty();
    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return p_94041_2_ != null && p_94041_2_.getItem() != null && p_94041_2_.getItem() instanceof ItemArmor;
    }

}
