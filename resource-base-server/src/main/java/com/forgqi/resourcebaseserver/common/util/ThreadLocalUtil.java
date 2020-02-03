package com.forgqi.resourcebaseserver.common.util;

import org.springframework.data.util.CastUtils;

public class ThreadLocalUtil {
    private final static ThreadLocal<Object> HOLDER = new ThreadLocal<>();

    public static <T> void set(T object) {
        HOLDER.set(object);
    }

    public static <T> T get() {
        return CastUtils.cast(HOLDER.get());
    }

    public static void remove() {
        HOLDER.remove();
    }
}
