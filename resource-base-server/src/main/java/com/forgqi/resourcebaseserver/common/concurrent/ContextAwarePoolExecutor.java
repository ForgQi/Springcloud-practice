package com.forgqi.resourcebaseserver.common.concurrent;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class ContextAwarePoolExecutor extends ThreadPoolTaskExecutor {

    @Override
    public void execute(Runnable task) {
        System.out.println("22222222222222222222");
        if (task instanceof CompletableFuture) {
            System.out.println(true);
        }
        System.out.println(task);
        Thread th = Thread.currentThread();

        System.out.println("Tread name:" + th.getName());
        System.out.println(RequestContextHolder.currentRequestAttributes());
        super.execute(new ContextAwareRunnable(task, RequestContextHolder.currentRequestAttributes()));
        System.out.println("33333333333333");
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
