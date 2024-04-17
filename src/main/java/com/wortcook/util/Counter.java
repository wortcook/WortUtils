package com.wortcook.util;

/**
 * A counter that can be used to count up to a limit.
 * @param <T> the type of the count and limit
 */
public interface Counter<T extends Comparable<T>> extends Countable<T>{
    /**
     * Increments the count, for numerical types this is equivalent to adding 1.
     */
    void count();

    /**
     * Create a new Integer counter with the given limit.
     * @return
     */
    public static Counter<Integer> of(final int theLimit) {
        return new Counter<Integer>() {
            private int count = 0;
            private int limit = theLimit;

            @Override
            public Integer getCount() {
                return count;
            }

            @Override
            public Integer getLimit() {
                return limit;
            }

            @Override
            public void resetCount() {
                count = 0;
            }

            @Override
            public void count() {
                count++;
            }
        };
    }
}
