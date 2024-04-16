package com.wortcook.util;

/**
 * A counter that can be used to count up to a limit.
 * @param <T> the type of the count and limit
 */
public interface Counter<T extends Comparable<T>>{

    /**
     * Returns the current count.
     * @return
     */
    public T getCount();

    /**
     * Returns the limit.
     * @return
     */
    public T getLimit();

    /**
     * Resets the count to the beginning, for numerical types this is 0.
     */
    public void resetCount();

    /**
     * Increments the count, for numerical types this is equivalent to adding 1.
     */
    public void count();

    /**
     * Returns true if the count is under the limit.
     * @return
     */
    default boolean isUnder() {
        return getCount().compareTo(getLimit()) < 0;
    }

    /**
     * Returns true if the count is over the limit.
     * @return
     */
    default boolean isOver() {
        return getCount().compareTo(getLimit()) > 0;
    }

    /**
     * Returns true if the count is at the limit.
     * @return
     */
    default boolean isAt() {
        return getCount().compareTo(getLimit()) == 0;
    }

    /**
     * Returns true if the count is at or under the limit.
     * @return
     */
    default boolean isAtOrUnder() {
        return getCount().compareTo(getLimit()) <= 0;
    }

    /**
     * Returns true if the count is at or over the limit.
     * @return
     */
    default boolean isAtOrOver() {
        return getCount().compareTo(getLimit()) >= 0;
    }

    /**
     * Create a new Integer counter with the given limit.
     * @return
     */
    public static Counter<Integer> of(Integer theLimit){
        return Counter.of((int)theLimit);
    }

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
