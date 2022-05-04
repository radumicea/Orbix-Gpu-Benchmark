package com.orbix.bench;

import java.util.Random;

public interface IBenchmark
{
    Random RANDOM = new Random();
    /**
     * First method that should be used once the benchmark was created.
     * @param params
     */
    void initialize(Object... params);
    /**
     * Call the <code>warmUp</code> method after initializing, before calling <code>run</code>.
     */
    void warmUp();
    /**
     * <code>run</code> without parameters is the standard benchmark used to assess performance.
     */
    void run();
    /**
     * <code>run</code> with parameters is a benchmark with custom options used to assess performance in custom use cases.
     * @param params
     */
    void run(Object... params);
    void cancel();
}
