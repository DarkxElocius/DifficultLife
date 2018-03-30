package net.dummythinking.difficultLife.utils;

import baubles.api.BaubleType;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.IIcon;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ContainerVanityArmor extends Container {

    public IInventory tryLoadStackFromPlayer(EntityPlayer player) {
        InventoryVanityArmor iva = new InventoryVanityArmor(player);
        if (!player.worldObj.isRemote)
            iva.readFromNBT(DLSaveStorage.playerData.get(player.getCommandSenderName()));
        else
            iva.readFromNBT(DLSaveStorage.clientPlayerData);
        return iva;
    }

    public ContainerVanityArmor(InventoryPlayer playerInv, boolean par2, EntityPlayer player) {
        craftMatrix = new InventoryCrafting(this, 2, 2);
        craftResult = new InventoryCraftResult();
        isLocalWorld = par2;
        thePlayer = player;
        vanityArmor = tryLoadStackFromPlayer(player);

        addSlotToContainer(new SlotCrafting(playerInv.player, craftMatrix, craftResult, 0, 136, 62));
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++)
                addSlotToContainer(new Slot(craftMatrix, j + i * 2, 127 + j * 18, 13 + i * 18));

        }

        for (int i = 0; i < 4; i++) {
            final int k = i;
            addSlotToContainer(new Slot(playerInv, playerInv.getSizeInventory() - 1 - i, 8, 8 + i * 18) {
                public int getSlotStackLimit() {
                    return 1;
                }

                public boolean isItemValid(ItemStack p_75214_1_) {
                    if (p_75214_1_ == null) return false;
                    return p_75214_1_.getItem().isValidArmor(p_75214_1_, k, thePlayer);
                }

                @SideOnly(Side.CLIENT)
                public IIcon getBackgroundIconIndex() {
                    return ItemArmor.func_94602_b(k);
                }
            });
        }

        for (int i = 0; i < 4; i++) {
            final int k = i;
            addSlotToContainer(new Slot(vanityArmor, i, 26, 8 + i * 18) {
                public int getSlotStackLimit() {
                    return 1;
                }

                public boolean isItemValid(ItemStack p_75214_1_) {
                    if (p_75214_1_ == null) return false;
                    return p_75214_1_.getItem().isValidArmor(p_75214_1_, k, thePlayer);
                }

                @SideOnly(Side.CLIENT)
                public IIcon getBackgroundIconIndex() {
                    return ItemArmor.func_94602_b(k);
                }
            });
        }

        //TODO Baubles integration
        try {
            if (Loader.isModLoaded("Baubles")) {
                Class<?> SlotBauble = Class.forName("baubles.common.container.SlotBauble");
                Constructor<?> sbc = SlotBauble.getConstructor(IInventory.class, BaubleType.class, int.class, int.class, int.class);

                Class<?> InventoryBaubles = Class.forName("baubles.common.container.InventoryBaubles");
                baubles = IInventory.class.cast(InventoryBaubles.getConstructor(EntityPlayer.class).newInstance(player));
                InventoryBaubles.getMethod("setEventHandler", Container.class).invoke(baubles, this);
                if (!player.worldObj.isRemote) {
                    Field stackList = InventoryBaubles.getField("stackList");
                    Class<?> PlayerHandler = Class.forName("baubles.common.lib.PlayerHandler");
                    stackList.set(baubles, stackList.get(PlayerHandler.getMethod("getPlayerBaubles", EntityPlayer.class).invoke(null, player)));
                }
                addSlotToContainer(Slot.class.cast(sbc.newInstance(baubles, BaubleType.AMULET, 0, 98, 8)));
                addSlotToContainer(Slot.class.cast(sbc.newInstance(baubles, BaubleType.RING, 1, 98, 26)));
                addSlotToContainer(Slot.class.cast(sbc.newInstance(baubles, BaubleType.RING, 2, 98, 44)));
                addSlotToContainer(Slot.class.cast(sbc.newInstance(baubles, BaubleType.BELT, 3, 98, 62)));
            }
        } catch (Exception e) {
            //Silently catching error, probably a class misnaming
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++)
                addSlotToContainer(new Slot(playerInv, j + (i + 1) * 9, 8 + j * 18, 84 + i * 18));

        }

        for (int i = 0; i < 9; i++)
            addSlotToContainer(new Slot(playerInv, i, 8 + i * 18, 142));

        onCraftMatrixChanged(craftMatrix);
    }

    public void onCraftMatrixChanged(IInventory par1IInventory) {
        craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(craftMatrix, thePlayer.worldObj));
    }

    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        if (!player.worldObj.isRemote)
            ((InventoryVanityArmor) this.vanityArmor).writeToNBT(DLSaveStorage.playerData.get(player.getCommandSenderName()));
        else
            ((InventoryVanityArmor) this.vanityArmor).writeToNBT(DLSaveStorage.clientPlayerData);

        for (int i = 0; i < 4; i++) {
            ItemStack itemstack = craftMatrix.getStackInSlotOnClosing(i);
            if (itemstack != null)
                player.dropPlayerItemWithRandomChoice(itemstack, false);
        }

        craftResult.setInventorySlotContents(0, (ItemStack) null);
        if (!player.worldObj.isRemote) {
            if (Loader.isModLoaded("Baubles")) {
                try {
                    Class<?> PlayerHandler = Class.forName("baubles.common.lib.PlayerHandler");
                    Class<?> InventoryBaubles = Class.forName("baubles.common.container.InventoryBaubles");
                    Method setPlayerBaubles = PlayerHandler.getMethod("setPlayerBaubles", EntityPlayer.class, InventoryBaubles);
                    setPlayerBaubles.invoke(null, player, baubles);
                } catch (Exception e) {
                    //Silently catching error, probably a class misnaming
                }
            }
        }
    }

    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
        return null;
    }

    public void putStacksInSlots(ItemStack p_75131_1_[]) {
        if (Loader.isModLoaded("Baubles")) {
            try {
                Class<?> InventoryBaubles = Class.forName("baubles.common.container.InventoryBaubles");
                Field blockEvents = InventoryBaubles.getField("blockEvents");
                blockEvents.set(baubles, true);
            } catch (Exception e) {
                //
            }
        }
        super.putStacksInSlots(p_75131_1_);
    }

    protected boolean mergeItemStack(ItemStack par1ItemStack, int par2, int par3, boolean par4, Slot ss) {
        boolean flag1 = false;
        int k = par2;
        if (par4)
            k = par3 - 1;
        if (par1ItemStack.isStackable())
            while (par1ItemStack.stackSize > 0 && (!par4 && k < par3 || par4 && k >= par2)) {
                Slot slot = (Slot) inventorySlots.get(k);
                ItemStack itemstack1 = slot.getStack();
                if (itemstack1 != null && itemstack1.getItem() == par1ItemStack.getItem() && (!par1ItemStack.getHasSubtypes() || par1ItemStack.getItemDamage() == itemstack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(par1ItemStack, itemstack1)) {
                    int l = itemstack1.stackSize + par1ItemStack.stackSize;
                    if (l <= par1ItemStack.getMaxStackSize()) {
                        par1ItemStack.stackSize = 0;
                        itemstack1.stackSize = l;
                        slot.onSlotChanged();
                        flag1 = true;
                    } else if (itemstack1.stackSize < par1ItemStack.getMaxStackSize()) {
                        par1ItemStack.stackSize -= par1ItemStack.getMaxStackSize() - itemstack1.stackSize;
                        itemstack1.stackSize = par1ItemStack.getMaxStackSize();
                        slot.onSlotChanged();
                        flag1 = true;
                    }
                }
                if (par4)
                    k--;
                else
                    k++;
            }
        if (par1ItemStack.stackSize > 0) {
            if (par4)
                k = par3 - 1;
            else
                k = par2;
            do {
                if ((par4 || k >= par3) && (!par4 || k < par2))
                    break;
                Slot slot = (Slot) inventorySlots.get(k);
                ItemStack itemstack1 = slot.getStack();
                if (itemstack1 == null) {
                    slot.putStack(par1ItemStack.copy());
                    slot.onSlotChanged();
                    par1ItemStack.stackSize = 0;
                    flag1 = true;
                    break;
                }
                if (par4)
                    k--;
                else
                    k++;
            } while (true);
        }
        return flag1;
    }

    public boolean func_94530_a(ItemStack par1ItemStack, Slot par2Slot) {
        return par2Slot.inventory != craftResult && super.func_94530_a(par1ItemStack, par2Slot);
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return true;
    }

    public InventoryCrafting craftMatrix;
    public IInventory craftResult;
    public IInventory vanityArmor;
    public IInventory baubles;
    public boolean isLocalWorld;
    private final EntityPlayer thePlayer;

}
