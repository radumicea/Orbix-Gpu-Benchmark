package com.orbix.testbench;

import com.orbix.bench.IBenchmark;
import com.orbix.logging.ConsoleLogger;
import com.orbix.logging.BenchResult;
import com.orbix.logging.CSVLogger;
import com.orbix.logging.ILogger;

import java.io.IOException;
import java.time.Instant;

public final class TestBench extends AbstractTestBench
{
    private final Class<? extends IBenchmark> benchClass;
    private final String logsFileName;
    private final String GPUName;

    private Exception exception;

    public TestBench(Class<? extends IBenchmark> benchClass, String logsFileName, String GPUName)
    {
        this.benchClass = benchClass;
        this.logsFileName = logsFileName;
        this.GPUName = GPUName;
    }

    @Override
    protected BenchResult call() throws Exception
    {
        IBenchmark bench = benchClass.getDeclaredConstructor().newInstance();
        ILogger log = null;
        BenchResult benchResult = null;

        try
        {
            try
            {
                log = new CSVLogger(logsFileName);
            }
            catch (IOException e)
            {
                log = new ConsoleLogger();
                displayFileOpenWarningAlert(logsFileName);
                e.printStackTrace();
            }

            bench.initialize(GPUName);
            bench.warmUp();

            Thread thread = new Thread()
            {
                @Override
                public void run()
                {
                    try
                    {
                        bench.run();
                    }
                    catch (Exception e)
                    {
                        exception = e;
                    }
                }
            };

            thread.start();
            thread.join();

            if (exception != null)
            {
                throw exception;
            }

            benchResult = new BenchResult(
                Instant.now().toString(),
                System.getProperty("user.name"),
                GPUName,
                benchClass.getSimpleName(),
                bench.getExecutionTimeMs(),
                -1);

            try
            {
                log.write(benchResult);
            }
            catch (Exception e)
            {
                new ConsoleLogger().write(benchResult);
                displayFileWriteWarningAlert(logsFileName);
                e.printStackTrace();
            }
        }
        catch (InterruptedException e)
        {
            bench.cancel();
        }

        bench.cleanUp();
        log.close();

        return benchResult;
    }
}
