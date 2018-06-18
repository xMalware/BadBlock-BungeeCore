package fr.badblock.bungee.utils.logfilters;

import java.net.SocketAddress;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelProgressivePromise;
import io.netty.channel.ChannelPromise;
import io.netty.util.concurrent.EventExecutorGroup;

public class CustomChannelPipeline implements ChannelPipeline {

	@Override
	public Iterator<Entry<String, ChannelHandler>> iterator() {
		return null;
	}

	@Override
	public ChannelPipeline addAfter(String arg0, String arg1, ChannelHandler arg2) {
		return this;
	}

	@Override
	public ChannelPipeline addAfter(EventExecutorGroup arg0, String arg1, String arg2, ChannelHandler arg3) {
		return this;
	}

	@Override
	public ChannelPipeline addBefore(String arg0, String arg1, ChannelHandler arg2) {
		return this;
	}

	@Override
	public ChannelPipeline addBefore(EventExecutorGroup arg0, String arg1, String arg2, ChannelHandler arg3) {
		return this;
	}

	@Override
	public ChannelPipeline addFirst(ChannelHandler... arg0) {
		return this;
	}

	@Override
	public ChannelPipeline addFirst(String arg0, ChannelHandler arg1) {
		return this;
	}

	@Override
	public ChannelPipeline addFirst(EventExecutorGroup arg0, ChannelHandler... arg1) {
		return this;
	}

	@Override
	public ChannelPipeline addFirst(EventExecutorGroup arg0, String arg1, ChannelHandler arg2) {
		return this;
	}

	@Override
	public ChannelPipeline addLast(ChannelHandler... arg0) {
		return this;
	}

	@Override
	public ChannelPipeline addLast(String arg0, ChannelHandler arg1) {
		return this;
	}

	@Override
	public ChannelPipeline addLast(EventExecutorGroup arg0, ChannelHandler... arg1) {
		return this;
	}

	@Override
	public ChannelPipeline addLast(EventExecutorGroup arg0, String arg1, ChannelHandler arg2) {
		return this;
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
	public ChannelHandlerContext context(ChannelHandler arg0) {
		return new CustomChannelHandlerContext();
	}

	@Override
	public ChannelHandlerContext context(String arg0) {
		return new CustomChannelHandlerContext();
	}

	@Override
	public ChannelHandlerContext context(Class<? extends ChannelHandler> arg0) {
		return new CustomChannelHandlerContext();
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
	public ChannelPipeline fireChannelActive() {
		return this;
	}

	@Override
	public ChannelPipeline fireChannelInactive() {
		return this;
	}

	@Override
	public ChannelPipeline fireChannelRead(Object arg0) {
		return this;
	}

	@Override
	public ChannelPipeline fireChannelReadComplete() {
		return this;
	}

	@Override
	public ChannelPipeline fireChannelRegistered() {
		return this;
	}

	@Override
	public ChannelPipeline fireChannelUnregistered() {
		return this;
	}

	@Override
	public ChannelPipeline fireChannelWritabilityChanged() {
		return this;
	}

	@Override
	public ChannelPipeline fireExceptionCaught(Throwable arg0) {
		return this;
	}

	@Override
	public ChannelPipeline fireUserEventTriggered(Object arg0) {
		return this;
	}

	@Override
	public ChannelHandler first() {
		return null;
	}

	@Override
	public ChannelHandlerContext firstContext() {
		return new CustomChannelHandlerContext();
	}

	@Override
	public ChannelPipeline flush() {
		return this;
	}

	@Override
	public ChannelHandler get(String arg0) {
		return null;
	}

	@Override
	public <T extends ChannelHandler> T get(Class<T> arg0) {
		return null;
	}

	@Override
	public ChannelHandler last() {
		return null;
	}

	@Override
	public ChannelHandlerContext lastContext() {
		return new CustomChannelHandlerContext();
	}

	@Override
	public List<String> names() {
		return null;
	}

	@Override
	public ChannelPipeline read() {
		return this;
	}

	@Override
	public ChannelPipeline remove(ChannelHandler arg0) {
		return this;
	}

	@Override
	public ChannelHandler remove(String arg0) {
		return null;
	}

	@Override
	public <T extends ChannelHandler> T remove(Class<T> arg0) {
		return null;
	}

	@Override
	public ChannelHandler removeFirst() {
		return null;
	}

	@Override
	public ChannelHandler removeLast() {
		return null;
	}

	@Override
	public ChannelPipeline replace(ChannelHandler arg0, String arg1, ChannelHandler arg2) {
		return this;
	}

	@Override
	public ChannelHandler replace(String arg0, String arg1, ChannelHandler arg2) {
		return null;
	}

	@Override
	public <T extends ChannelHandler> T replace(Class<T> arg0, String arg1, ChannelHandler arg2) {
		return null;
	}

	@Override
	public Map<String, ChannelHandler> toMap() {
		return null;
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
	public ChannelFuture newFailedFuture(Throwable arg0) {
		return new CustomChannelFuture();
	}

	@Override
	public ChannelProgressivePromise newProgressivePromise() {
		return new CustomChannelProgressivePromise();
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
	public ChannelPromise voidPromise() {
		return new CustomChannelPromise();
	}

}
