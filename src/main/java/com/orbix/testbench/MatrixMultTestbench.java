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

    public MatrixMultTestBench(String logsFileName, String GPUName)
    {
        this.logsFileName = logsFileName;
        this.GPUName = GPUName;
    }

    @Override
    protected BenchResult call() throws Exception
    {
        IBenchmark b = new MatrixMultBenchmark();
        ILogger log;
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

        b.initialize(GPUName);
        b.warmUp();

        timer.start();
        b.run();
        long elapsed = timer.stop();

        BenchResult benchResult = new BenchResult(
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
        log.close();

        return benchResult;
    }
}
