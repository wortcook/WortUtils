package com.wortcook;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class Wort {
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


}
