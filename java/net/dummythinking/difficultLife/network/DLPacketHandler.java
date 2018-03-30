package net.dummythinking.difficultLife.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.channel.ChannelHandler;
import net.dummythinking.difficultLife.DLCore;
import net.dummythinking.difficultLife.utils.DLSaveStorage;
import net.dummythinking.difficultLife.utils.DLUtils;

@ChannelHandler.Sharable
public class DLPacketHandler implements IMessageHandler<DLPacket, IMessage> {

    @Override
    public IMessage onMessage(DLPacket message, MessageContext ctx) {
        if (message.id.equalsIgnoreCase("worldData")) {
            DLSaveStorage.commonGenericTag = message.syncedTag;
        }
        if (message.id.equalsIgnoreCase("playerData")) {
            String tagName = message.syncedTag.getString("username");
            String username = DLCore.proxy.getClientPlayer().getCommandSenderName();
            if (username.equalsIgnoreCase(tagName))
                DLSaveStorage.clientPlayerData = message.syncedTag;
            else
                DLSaveStorage.playerData.put(tagName, message.syncedTag);
        }
        if (message.id.equalsIgnoreCase("guiRequest")) {
            DLUtils.handleGuiRequest(message);
        }
        return null; //Always return null, so the Network does not gets overfilled by packets!
    }

}
