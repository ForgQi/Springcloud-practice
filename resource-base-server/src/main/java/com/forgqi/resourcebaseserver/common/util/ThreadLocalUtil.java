package com.forgqi.resourcebaseserver.common.util;

public class ThreadLocalUtil {
    private final static ThreadLocal<Object> HOLDER = new ThreadLocal<>();

    public static <T> void set(T object) {
        HOLDER.set(object);
    }

    public static Object get() {
        return HOLDER.get();
    }

    public static void remove() {
        HOLDER.remove();
    }
}
