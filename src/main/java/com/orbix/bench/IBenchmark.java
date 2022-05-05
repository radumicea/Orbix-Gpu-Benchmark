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
    void run();
}
