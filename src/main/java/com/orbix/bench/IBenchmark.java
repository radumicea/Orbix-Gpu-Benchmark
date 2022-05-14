package com.orbix.bench;

public interface IBenchmark
{
    /**
     * First method that should be used once the benchmark was created.
     * @param params
     */
    void initialize(Object... params) throws Exception;
    void run() throws Exception;
    long getResult();
}
