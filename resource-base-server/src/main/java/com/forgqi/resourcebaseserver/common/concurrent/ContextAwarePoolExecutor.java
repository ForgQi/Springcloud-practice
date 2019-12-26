package com.forgqi.resourcebaseserver.common.concurrent;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class ContextAwarePoolExecutor extends ThreadPoolTaskExecutor {

    @Override
    public void execute(Runnable task) {

        Thread th = Thread.currentThread();

        super.execute(new ContextAwareRunnable(task, RequestContextHolder.currentRequestAttributes()));
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return super.submit(new ContextAwareCallable(task, RequestContextHolder.currentRequestAttributes()));
    }

    @Override
    public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
        return super.submitListenable(new ContextAwareCallable(task, RequestContextHolder.currentRequestAttributes()));
    }

    @Override
    public Future<?> submit(Runnable task) {
        return super.submit(new ContextAwareRunnable(task, RequestContextHolder.currentRequestAttributes()));
    }

    @Override
    public ListenableFuture<?> submitListenable(Runnable task) {
        return super.submitListenable(new ContextAwareRunnable(task, RequestContextHolder.currentRequestAttributes()));
    }
}
