package com.wortcook;

import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class Wort {
    public static final Logger logger = Logger.getLogger(Wort.class.getName());

    public static <T> Optional<T> tryOptional(final Supplier<T> supplier) {
        try {
            return Optional.of(supplier.get());
        } catch (Throwable e) {
            return Optional.empty();
        }
    }

    public static <T> T tryDefault(final Supplier<T> supplier, final T defaultVal) {
        try {
            return supplier.get();
        } catch (Throwable e) {
            return defaultVal;
        }
    }

    public static <T,A> T tryDefault(final Function<A,T> supplier, final A arg, final T defaultVal) {
        try {
            return supplier.apply(arg);
        } catch (Throwable e) {
            return defaultVal;
        }
    }

    public static <T, A1, A2> T tryDefault(final BiFunction<A1, A2, T> supplier, final A1 arg1, final A2 arg2, final T defaultVal) {
        try {
            return supplier.apply(arg1, arg2);
        } catch (Throwable e) {
            return defaultVal;
        }
    }
    
    public static <T> T tryRepeat(final int times ,final Supplier<T> supplier) {
        for(int i = 0 ; i < (times - 1) ; i++) {
            try{
                return supplier.get();
            }catch(Throwable t){
                // ignore
            }
        }
        return supplier.get();
    }

    public static <T> T withLock(final Lock lock, final Supplier<T> supplier) {
        lock.lock();
        try {
            return supplier.get();
        } finally {
            lock.unlock();
        }
    }

    public static void withLock(final Lock lock, final Runnable runnable) {
        lock.lock();
        try {
            runnable.run();
        } finally {
            lock.unlock();
        }
    }
}