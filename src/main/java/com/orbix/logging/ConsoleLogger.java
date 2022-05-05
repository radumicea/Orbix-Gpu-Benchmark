package com.orbix.logging;

public class ConsoleLogger implements ILogger
{
    @Override
    public void write(String GPUName, String benchName,
        long runtime, TimeUnit timeUnit, int score)
    {
        System.out.println("GPU: " + GPUName);
        System.out.println("Benchmark: " + benchName);
        System.out.println("Runtime: " + TimeUnit.toUnit(runtime, timeUnit));
        System.out.println("Score: " + score);
    }

    /**
     * Not necessary for ConsoleLogger.
     */
    @Override
    public void close() { }
}
