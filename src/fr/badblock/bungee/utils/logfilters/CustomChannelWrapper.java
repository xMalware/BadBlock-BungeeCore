package fr.badblock.bungee.utils.logfilters;

import io.netty.channel.ChannelHandlerContext;
import net.md_5.bungee.netty.ChannelWrapper;

public class CustomChannelWrapper extends ChannelWrapper {

	public CustomChannelWrapper(ChannelHandlerContext ctx) {
		super(ctx);
	}

	@Override
	public boolean isClosed() {
		return false;
	}

	@Override
	public void write(Object packet) {

	}

}
