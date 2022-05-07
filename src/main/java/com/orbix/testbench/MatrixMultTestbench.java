package com.orbix.testbench;

import com.orbix.bench.IBenchmark;
import com.orbix.bench.MatrixMultBenchmark;
import com.orbix.logging.ConsoleLogger;
import com.orbix.logging.BenchResult;
import com.orbix.logging.CSVLogger;
import com.orbix.logging.ILogger;
import com.orbix.logging.TimeUnit;
import com.orbix.timing.ITimer;
import com.orbix.timing.Timer;

import java.io.IOException;
import java.time.Instant;

/**
 * Tests parallelization capabilities of GPU through a highly
 * parallelizable task, namely matrix multiplication.
 */
public final class MatrixMultTestBench extends AbstractTestBench
{
    private final String logsFileName;
    private final String GPUName;

    private Exception exception;

    public MatrixMultTestBench(String logsFileName, String GPUName)
    {
        this.logsFileName = logsFileName;
        this.GPUName = GPUName;
    }

    @Override
    protected BenchResult call() throws Exception
    {
        IBenchmark bench = new MatrixMultBenchmark();
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
            ITimer timer = new Timer();

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

            timer.start();
            thread.start();
            thread.join();
            long elapsed = timer.stop();

            if (exception != null)
            {
                throw exception;
            }

            benchResult = new BenchResult(
                Instant.now().toString(),
                System.getProperty("user.name"),
                GPUName,
                "Matrix Multiplication",
                TimeUnit.toUnit(elapsed, TimeUnit.SEC),
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
