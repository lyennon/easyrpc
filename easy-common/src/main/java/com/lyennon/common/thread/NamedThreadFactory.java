package com.lyennon.common.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory implements ThreadFactory {
    private AtomicInteger threadIndex = new AtomicInteger(0);
    private String name;

    public NamedThreadFactory() {
        this("default");
    }

    public NamedThreadFactory(String name) {
        this.name = name;
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r,this.name + "_" + this.threadIndex.incrementAndGet());
    }
}
