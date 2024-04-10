package com.wortcook.executecycle;

import java.util.Map;
import java.util.HashMap;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

import com.wortcook.util.CircularQueueList;

public class ExecuteCycle implements Callable<Long> {
    private final long maxEpochCount;
    private long epochCount = 0;

    private final CircularQueueList<Consumer<ExecuteCycle>> consumers = new CircularQueueList<>();
    private final Map<String, Consumer<ExecuteCycle>> consumerMap = new HashMap<>();
    private final Map<String, Object> context = new HashMap<>();

    public ExecuteCycle() {
        this(Long.MAX_VALUE);
    }

    public ExecuteCycle(final long maxEpochCount) {
        this.maxEpochCount = maxEpochCount;
    }

    public void addConsumer(final String name, final Consumer<ExecuteCycle> consumer) {
        consumers.add(consumer);
        consumerMap.put(name, consumer);
    }

    public void removeConsumer(final String name) {
        final Consumer<ExecuteCycle> consumer = consumerMap.get(name);
        if (consumer != null) {
            consumers.remove(consumer);
            consumerMap.remove(name);
        }
    }

    public Map<String,Object> getContext() {
        return context;
    }

    public void setContextValue(final String key, final Object value) {
        context.put(key, value);
    }

    public Object getContextValue(final String key) {
        return context.get(key);
    }
    
    @Override
    public Long call() {
        if(epochCount < maxEpochCount) {
            for(int consumerIdx = 0; consumerIdx < consumers.size(); consumerIdx++) {
                consumers.get(consumerIdx).accept(this);
            }
            epochCount++;
        }
        return epochCount;
    }
}
