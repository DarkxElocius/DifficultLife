package difficultLife.network;

import io.netty.channel.ChannelHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import difficultLife.DLCore;
import difficultLife.utils.DLSaveStorage;
import difficultLife.utils.DLUtils;

@ChannelHandler.Sharable
public class DLPacketHandler implements IMessageHandler<DLPacket,IMessage>
{

	@Override
	public IMessage onMessage(DLPacket message, MessageContext ctx) 
	{
		if(message.id.equalsIgnoreCase("worldData"))
		{
			DLSaveStorage.commonGenericTag = message.syncedTag;
		}
		if(message.id.equalsIgnoreCase("playerData"))
		{
			String tagName = message.syncedTag.getString("username");
			String username = DLCore.proxy.getClientPlayer().getCommandSenderName();
			if(username.equalsIgnoreCase(tagName))
				DLSaveStorage.clientPlayerData = message.syncedTag;
			else
				DLSaveStorage.playerData.put(tagName, message.syncedTag);
		}
		if(message.id.equalsIgnoreCase("guiRequest"))
		{
			DLUtils.handleGuiRequest(message);
		}
		return null; //Always return null, so the Network does not gets overfilled by packets!
	}

}
