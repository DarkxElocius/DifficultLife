package net.dummythinking.difficultLife;

import cpw.mods.fml.common.network.IGuiHandler;
import net.dummythinking.difficultLife.utils.ContainerVanityArmor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class DLServer implements IGuiHandler {

    public void clientInfo() {

    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world,
                                      int x, int y, int z) {
        return new ContainerVanityArmor(player.inventory, player.worldObj.isRemote, player);
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world,
                                      int x, int y, int z) {
        // TODO Auto-generated method stub
        return null;
    }

    public EntityPlayer getClientPlayer() {
        return null;
    }

}
