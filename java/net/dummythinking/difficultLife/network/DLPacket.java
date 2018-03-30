package net.dummythinking.difficultLife.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

public class DLPacket implements IMessage {

    public String id;
    public NBTTagCompound syncedTag;
    public boolean hasInit = false;

    public DLPacket() {
        super();
        this.hasInit = true;
    }

    public DLPacket setNBT(NBTTagCompound tag) {
        if (this.hasInit)
            syncedTag = tag;
        return this;
    }

    public DLPacket setID(String s) {
        if (this.hasInit)
            id = s;
        return this;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        NBTTagCompound byteReadTag = ByteBufUtils.readTag(buf);
        this.id = byteReadTag.getString("id");
        this.syncedTag = byteReadTag.getCompoundTag("tag");
    }

    @Override
    public void toBytes(ByteBuf buf) {
        if (this.hasInit) {
            NBTTagCompound byteWriteTag = new NBTTagCompound();
            byteWriteTag.setString("id", this.id);
            byteWriteTag.setTag("tag", this.syncedTag);
            ByteBufUtils.writeTag(buf, byteWriteTag);
        }

    }

}
