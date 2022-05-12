package com.orbix.testbench;

import com.orbix.bench.IBenchmark;
import com.orbix.logging.BenchResult;

import java.time.Instant;

import javafx.concurrent.Task;

import com.aparapi.device.OpenCLDevice;

public final class TestBench extends Task<BenchResult>
{
    private final OpenCLDevice GPU;
    private final Class<? extends IBenchmark>[] benchClasses;

    @SafeVarargs
    public TestBench(OpenCLDevice GPU, Class<? extends IBenchmark>... benchClasses)
    {
        this.GPU = GPU;
        this.benchClasses = benchClasses;
    }

    @Override
    protected BenchResult call()
    {
        try
        {
            double executionTimeMs = 0;

            for (Class<? extends IBenchmark> benchClass : benchClasses)
            {
                IBenchmark bench = benchClass.getDeclaredConstructor().newInstance();
                bench.initialize(GPU);
                bench.warmUp();
                bench.run();

                Error e = bench.getError();
                if (e != null)
                {
                    throw e;
                }

                executionTimeMs += bench.getExecutionTimeMs();
                bench.cleanUp();
            }

            // TODO: benchName based on enum. Properly name them and the benchmark classes
            String benchName = (benchClasses.length == 1)
                ? benchClasses[0].getSimpleName()
                : "Standard Benchmark";

            // TODO: Add score
            return new BenchResult(
                Instant.now().toString(),
                System.getProperty("user.name"),
                GPU.getName(),
                benchName,
                executionTimeMs,
                -1);
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            System.exit(-1);
            return null;
        }
    }
}
