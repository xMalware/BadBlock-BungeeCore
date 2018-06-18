package fr.badblock.bungee.utils.logfilters;

import java.net.SocketAddress;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelProgressivePromise;
import io.netty.channel.ChannelPromise;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.EventExecutor;

public class CustomChannelHandlerContext implements ChannelHandlerContext {

	@Override
	public <T> Attribute<T> attr(AttributeKey<T> arg0) {
		return null;
	}

	@Override
	public ByteBufAllocator alloc() {
		return null;
	}

	@Override
	public ChannelFuture bind(SocketAddress arg0) {
		return new CustomChannelFuture();
	}

	@Override
	public ChannelFuture bind(SocketAddress arg0, ChannelPromise arg1) {
		return new CustomChannelFuture();
	}

	@Override
	public Channel channel() {
		return null;
	}

	@Override
	public ChannelFuture close() {
		return new CustomChannelFuture();
	}

	@Override
	public ChannelFuture close(ChannelPromise arg0) {
		return new CustomChannelFuture();
	}

	@Override
	public ChannelFuture connect(SocketAddress arg0) {
		return new CustomChannelFuture();
	}

	@Override
	public ChannelFuture connect(SocketAddress arg0, SocketAddress arg1) {
		return new CustomChannelFuture();
	}

	@Override
	public ChannelFuture connect(SocketAddress arg0, ChannelPromise arg1) {
		return new CustomChannelFuture();
	}

	@Override
	public ChannelFuture connect(SocketAddress arg0, SocketAddress arg1, ChannelPromise arg2) {
		return new CustomChannelFuture();
	}

	@Override
	public ChannelFuture deregister() {
		return new CustomChannelFuture();
	}

	@Override
	public ChannelFuture deregister(ChannelPromise arg0) {
		return new CustomChannelFuture();
	}

	@Override
	public ChannelFuture disconnect() {
		return new CustomChannelFuture();
	}

	@Override
	public ChannelFuture disconnect(ChannelPromise arg0) {
		return new CustomChannelFuture();
	}

	@Override
	public EventExecutor executor() {
		return null;
	}

	@Override
	public ChannelHandlerContext fireChannelActive() {
		return new CustomChannelHandlerContext();
	}

	@Override
	public ChannelHandlerContext fireChannelInactive() {
		return new CustomChannelHandlerContext();
	}

	@Override
	public ChannelHandlerContext fireChannelRead(Object arg0) {
		return new CustomChannelHandlerContext();
	}

	@Override
	public ChannelHandlerContext fireChannelReadComplete() {
		return new CustomChannelHandlerContext();
	}

	@Override
	public ChannelHandlerContext fireChannelRegistered() {
		return new CustomChannelHandlerContext();
	}

	@Override
	public ChannelHandlerContext fireChannelUnregistered() {
		return new CustomChannelHandlerContext();
	}

	@Override
	public ChannelHandlerContext fireChannelWritabilityChanged() {
		return new CustomChannelHandlerContext();
	}

	@Override
	public ChannelHandlerContext fireExceptionCaught(Throwable arg0) {
		return new CustomChannelHandlerContext();
	}

	@Override
	public ChannelHandlerContext fireUserEventTriggered(Object arg0) {
		return new CustomChannelHandlerContext();
	}

	@Override
	public ChannelHandlerContext flush() {
		return new CustomChannelHandlerContext();
	}

	@Override
	public ChannelHandler handler() {
		return null;
	}

	@Override
	public boolean isRemoved() {
		return false;
	}

	@Override
	public String name() {
		return null;
	}

	@Override
	public ChannelFuture newFailedFuture(Throwable arg0) {
		return new CustomChannelFuture();
	}

	@Override
	public ChannelProgressivePromise newProgressivePromise() {
		return null;
	}

	@Override
	public ChannelPromise newPromise() {
		return new CustomChannelPromise();
	}

	@Override
	public ChannelFuture newSucceededFuture() {
		return new CustomChannelFuture();
	}

	@Override
	public ChannelPipeline pipeline() {
		return new CustomChannelPipeline();
	}

	@Override
	public ChannelHandlerContext read() {
		return this;
	}

	@Override
	public ChannelPromise voidPromise() {
		return new CustomChannelPromise();
	}

	@Override
	public ChannelFuture write(Object arg0) {
		return new CustomChannelFuture();
	}

	@Override
	public ChannelFuture write(Object arg0, ChannelPromise arg1) {
		return new CustomChannelFuture();
	}

	@Override
	public ChannelFuture writeAndFlush(Object arg0) {
		return new CustomChannelFuture();
	}

	@Override
	public ChannelFuture writeAndFlush(Object arg0, ChannelPromise arg1) {
		return new CustomChannelFuture();
	}

	@Override
	public <T> boolean hasAttr(AttributeKey<T> arg0) {
		return false;
	}

}
