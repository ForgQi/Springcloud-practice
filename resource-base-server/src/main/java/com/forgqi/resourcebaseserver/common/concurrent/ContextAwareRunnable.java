package com.forgqi.resourcebaseserver.common.concurrent;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class ContextAwareRunnable implements Runnable {
    private Runnable task;
    private RequestAttributes context;

    public ContextAwareRunnable(Runnable task, RequestAttributes context) {
        this.task = task;
        // Keeps a reference to scoped/context information of parent thread.
        // So original parent thread should wait for the background threads.
        // Otherwise you should clone context as @Arun A's answer
        this.context = context;
    }

    @Override
    public void run() {
        if (context != null) {
            RequestContextHolder.setRequestAttributes(context);
        }
        try {
            task.run();
        } finally {
            RequestContextHolder.resetRequestAttributes();
        }
    }
}
