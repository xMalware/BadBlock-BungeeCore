package fr.badblock.bungee.utils.logfilters;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class CustomChannelFuture implements ChannelFuture {

	@Override
	public boolean await(long arg0) throws InterruptedException {
		return false;
	}

	@Override
	public boolean await(long arg0, TimeUnit arg1) throws InterruptedException {
		return false;
	}

	@Override
	public boolean awaitUninterruptibly(long arg0) {
		return false;
	}

	@Override
	public boolean awaitUninterruptibly(long arg0, TimeUnit arg1) {
		return false;
	}

	@Override
	public boolean cancel(boolean arg0) {
		return false;
	}

	@Override
	public Throwable cause() {
		return new Throwable("ok");
	}

	@Override
	public Void getNow() {
		return null;
	}

	@Override
	public boolean isCancellable() {
		return false;
	}

	@Override
	public boolean isSuccess() {
		return false;
	}

	@Override
	public Void get() throws InterruptedException, ExecutionException {
		return null;
	}

	@Override
	public Void get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return null;
	}

	@Override
	public boolean isCancelled() {
		return false;
	}

	@Override
	public boolean isDone() {
		return false;
	}

	@Override
	public ChannelFuture addListener(GenericFutureListener<? extends Future<? super Void>> arg0) {
		return this;
	}

	@Override
	public ChannelFuture addListeners(
			@SuppressWarnings("unchecked") GenericFutureListener<? extends Future<? super Void>>... arg0) {
		return this;
	}

	@Override
	public ChannelFuture await() throws InterruptedException {
		return this;
	}

	@Override
	public ChannelFuture awaitUninterruptibly() {
		return this;
	}

	@Override
	public Channel channel() {
		return null;
	}

	@Override
	public ChannelFuture removeListener(GenericFutureListener<? extends Future<? super Void>> arg0) {
		return this;
	}

	@Override
	public ChannelFuture removeListeners(
			@SuppressWarnings("unchecked") GenericFutureListener<? extends Future<? super Void>>... arg0) {
		return this;
	}

	@Override
	public ChannelFuture sync() throws InterruptedException {
		return this;
	}

	@Override
	public ChannelFuture syncUninterruptibly() {
		return this;
	}

	@Override
	public boolean isVoid() {
		return false;
	}

}
