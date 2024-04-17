package com.wortcook.util;

/**
 * A counter that can be used to count up to a limit.
 * @param <T> the type of the count and limit
 */
public interface Countable<T extends Comparable<T>>{

    /**
     * Returns the current count.
     * @return
     */
    T getCount();

    /**
     * Returns the limit.
     * @return
     */
    T getLimit();

    /**
     * Resets the count to the beginning, for numerical types this is 0.
     */
    void resetCount();

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
}
