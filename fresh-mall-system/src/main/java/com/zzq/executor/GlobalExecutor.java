package com.zzq.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GlobalExecutor {

    private static final ExecutorService executor = Executors.newFixedThreadPool(1);

    public static void execute(Runnable runnable) {
        executor.execute(runnable);
    }

}
