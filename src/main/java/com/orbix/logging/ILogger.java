package com.orbix.logging;

public interface ILogger
{
    void write(BenchResult benchResult) throws Exception;
    /**
     * Used after we are done with the logger to make sure we flush everything.
     */
    void close();
}
