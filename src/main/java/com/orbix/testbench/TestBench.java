package com.orbix.testbench;

import com.aparapi.device.OpenCLDevice;
import com.orbix.bench.IBenchmark;
import com.orbix.logging.BenchResult;

import javafx.concurrent.Task;

import java.time.Instant;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

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

                Callable<Void> benchRunTask = new Callable<Void>()
                {
                    @Override
                    public Void call() throws Exception
                    {
                        bench.run();
                        return null;
                    }
                };

                ExecutorService executorService = Executors.newSingleThreadExecutor(
                    new ThreadFactory()
                    {
                        @Override
                        public Thread newThread(Runnable r)
                        {
                            Thread t = new Thread(r);
                            t.setDaemon(true);
                            return t;
                        }
                    });

                Future<Void> futureBenchRun = executorService.submit(benchRunTask);
                while (!futureBenchRun.isDone());
                futureBenchRun.get();

                executorService.shutdown();
                while (!executorService.isShutdown());

                executionTimeMs += bench.getExecutionTimeMs();
                bench.cleanUp();
                // TODO: bench.cleanUp is finally
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
