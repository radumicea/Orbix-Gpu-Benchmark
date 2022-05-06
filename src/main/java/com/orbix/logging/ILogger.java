package com.orbix.logging;

public interface ILogger
{
    void write(String GPUName, String benchName, long runtime, TimeUnit timeUnit, int score);
    /**
     * Used after we are done with the logger to make sure we flush everything.
     */
    void close();
}
