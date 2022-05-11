package com.orbix.testbench;

import com.aparapi.device.OpenCLDevice;
import com.orbix.bench.IBenchmark;
import com.orbix.logging.BenchResult;

import javafx.concurrent.Task;

import java.time.Instant;

public final class TestBench extends Task<BenchResult>
{
    private final OpenCLDevice GPU;
    private final Class<? extends IBenchmark>[] benchClasses;

    private IBenchmark bench;

    @SafeVarargs
    public TestBench(OpenCLDevice GPU, Class<? extends IBenchmark>... benchClasses)
    {
        this.GPU = GPU;
        this.benchClasses = benchClasses;
    }

    @Override
    protected BenchResult call() throws Exception
    {
        try
        {
            double executionTimeMs = 0;

            for (Class<? extends IBenchmark> benchClass : benchClasses)
            {
                bench = benchClass.getDeclaredConstructor().newInstance();
                bench.initialize(GPU);
                
                bench.warmUp();

                Thread t = new Thread()
                {
                    @Override
                    public void run()
                    {
                        bench.run();
                    }
                };

                t.start();
                t.join();

                Exception e = bench.getException();
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
        catch (InterruptedException e)
        {
            if (bench != null)
            {
                bench.cancel();
            }
            return null;
        }
        finally
        {
            if (bench != null)
            {
                bench.cleanUp();
            }
        }
    }
}
