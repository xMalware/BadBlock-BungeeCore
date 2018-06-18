package fr.badblock.bungee.utils.logfilters;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.netty.channel.Channel;
import io.netty.channel.ChannelProgressivePromise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class CustomChannelProgressivePromise implements ChannelProgressivePromise {

	@Override
	public boolean tryProgress(long arg0, long arg1) {
		return false;
	}

	@Override
	public boolean setUncancellable() {
		return false;
	}

	@Override
	public boolean tryFailure(Throwable arg0) {
		return false;
	}

	@Override
	public boolean trySuccess(Void arg0) {
		return false;
	}

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
		return null;
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
	public Void get(long arg0, TimeUnit arg1) throws InterruptedException, ExecutionException, TimeoutException {
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
	public Channel channel() {
		return null;
	}

	@Override
	public boolean isVoid() {
		return false;
	}

	@Override
	public boolean trySuccess() {
		return false;
	}

	@Override
	public ChannelProgressivePromise addListener(GenericFutureListener<? extends Future<? super Void>> arg0) {
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ChannelProgressivePromise addListeners(GenericFutureListener<? extends Future<? super Void>>... arg0) {
		return this;
	}

	@Override
	public ChannelProgressivePromise await() throws InterruptedException {
		return this;
	}

	@Override
	public ChannelProgressivePromise awaitUninterruptibly() {
		return this;
	}

	@Override
	public ChannelProgressivePromise removeListener(GenericFutureListener<? extends Future<? super Void>> arg0) {
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ChannelProgressivePromise removeListeners(GenericFutureListener<? extends Future<? super Void>>... arg0) {
		return this;
	}

	@Override
	public ChannelProgressivePromise setFailure(Throwable arg0) {
		return this;
	}

	@Override
	public ChannelProgressivePromise setProgress(long arg0, long arg1) {
		return this;
	}

	@Override
	public ChannelProgressivePromise setSuccess() {
		return this;
	}

	@Override
	public ChannelProgressivePromise setSuccess(Void arg0) {
		return this;
	}

	@Override
	public ChannelProgressivePromise sync() throws InterruptedException {
		return this;
	}

	@Override
	public ChannelProgressivePromise syncUninterruptibly() {
		return this;
	}

	@Override
	public ChannelProgressivePromise unvoid() {
		return this;
	}

}
