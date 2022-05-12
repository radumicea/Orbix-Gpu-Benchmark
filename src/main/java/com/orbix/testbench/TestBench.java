package com.orbix.testbench;

import com.orbix.bench.BenchmarkingMethods;
import com.orbix.bench.DataTransferBenchmark;
import com.orbix.bench.IBenchmark;
import com.orbix.bench.MatrixMultiplicationBenchmark;
import com.orbix.logging.BenchResult;

import java.time.Instant;

import javafx.concurrent.Task;

import com.aparapi.device.OpenCLDevice;

@SuppressWarnings("unchecked")
public final class TestBench extends Task<BenchResult>
{
    private final OpenCLDevice GPU;
    private final BenchmarkingMethods benchMethod;
    private final Class<? extends IBenchmark>[] benchClasses;

    public TestBench(OpenCLDevice GPU, BenchmarkingMethods benchMethod)
    {
        this.GPU = GPU;
        this.benchMethod = benchMethod;

        switch (benchMethod)
        {
            case StandardBenchmark:
            {
                // TODO: Others
                benchClasses = new Class[2];
                benchClasses[0] = DataTransferBenchmark.class;
                benchClasses[1] = MatrixMultiplicationBenchmark.class;
                break;
            }
            default:
            {
                benchClasses = new Class[1];
                try
                {
                    benchClasses[0] = (Class<? extends IBenchmark>)
                        Class.forName("com.orbix.bench." + benchMethod);
                }
                catch (ClassNotFoundException e)
                {
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
        }
    }

    @Override
    protected BenchResult call()
    {
        try
        {
            double score = 0;

            for (Class<? extends IBenchmark> benchClass : benchClasses)
            {
                IBenchmark bench = benchClass.getDeclaredConstructor().newInstance();
                bench.initialize(GPU);
                bench.run();

                score += getScore(bench.getResult(), benchClass);
            }

            // TODO: Add score
            return new BenchResult(
                Instant.now().toString(),
                System.getProperty("user.name"),
                GPU.getName(),
                benchMethod.toString(),
                (int)Math.round(score));
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            System.exit(-1);
            return null;
        }
    }

    private double getScore(double result, Class<? extends IBenchmark> benchClass)
    {
        switch (benchMethod)
        {
            case StandardBenchmark:
            {
                // Data transfer is 10%
                if (benchClass.equals(DataTransferBenchmark.class))
                {
                    return result * 0.1;
                }
                else
                {
                    return result * 0.9;
                }
            }
            
            default:
            {
                // If not std, no need to normalize for std.
                // Give pretty result.
                if (benchClass.equals(DataTransferBenchmark.class))
                {
                    return result;
                }
                else
                {
                    return result;
                }
            }
        }
    }
}
