package com.orbix.testbench;

import com.orbix.bench.IBenchmark;
import com.orbix.bench.MatrixMultBench;
import com.orbix.logging.ConsoleLogger;
import com.orbix.logging.ILogger;
import com.orbix.logging.TimeUnit;
import com.orbix.timing.ITimer;
import com.orbix.timing.Timer;

/**
 * Tests parallelization capabilities of GPU through a highly parallelizable task, namely matrix multiplication.
 */
public class MatrixMultTestbench
{
    public static void main(String[] args)
    {
        IBenchmark b = new MatrixMultBench();
        ILogger log = new ConsoleLogger();
        ITimer timer = new Timer();

        b.initialize();
        b.warmUp();

        timer.start();
        b.run();
        long elapsed = timer.stop();

        log.write("It took: ", TimeUnit.toUnit(elapsed, TimeUnit.SEC));
    }
}
