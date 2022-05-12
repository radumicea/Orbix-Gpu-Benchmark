package com.orbix.bench;

public interface IBenchmark
{
    /**
     * First method that should be used once the benchmark was created.
     * @param params
     */
    void initialize(Object... params);
    /**
     * Call the <code>warmUp</code> method after initializing, before calling <code>run</code>.
     * @throws Exception
     */
    void warmUp() throws Exception;
    /**
     * @throws Exception
     */
    void run() throws Exception;
    /**
     * Returns the execution time in ms.
     * @throws Exception
     */
    double getExecutionTimeMs();
    /**
     * User after <code>run()</code>.
     */
    void cleanUp();
    /**
     * <b>ONLY USED AFTER <code>run()</code></b>
     * @return possible Errors
     */
    Error getError();
}
