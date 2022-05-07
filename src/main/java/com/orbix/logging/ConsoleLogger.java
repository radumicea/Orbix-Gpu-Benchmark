package com.orbix.logging;

public class ConsoleLogger implements ILogger
{
    @Override
    public void write(BenchResult benchResult)
    {
        System.out.println(benchResult.getResult());
    }

    /**
     * Not necessary for ConsoleLogger.
     */
    @Override
    public void close() { }
}
