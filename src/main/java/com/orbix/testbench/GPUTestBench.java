package com.orbix.testbench;

import java.io.IOException;

import com.orbix.bench.IBenchmark;
import com.orbix.bench.MatrixMultBenchmark;
import com.orbix.logging.ConsoleLogger;
import com.orbix.logging.FileLogger;
import com.orbix.logging.ILogger;
import com.orbix.logging.TimeUnit;
import com.orbix.timing.ITimer;
import com.orbix.timing.Timer;

/**
 * Tests parallelization capabilities of GPU through a highly parallelizable task, namely matrix multiplication.
 */
public final class GPUTestBench
{
    private GPUTestBench() { }

    /**
     * @param params
     * <code>params[0]</code> must be the name of the GPU to be benchmarked.<br></br>
     * optional: three integers, representing the <code>r1</code>, <code>c1_r2</code>
     * and <code>c2</code> of the matrices.
     * if the three integers are not provided, a standard benchmark will take place.
     */
    public static void runMatrixMultBench(Object... params)
    {
        IBenchmark b = new MatrixMultBenchmark();
        ILogger log;
        try
        {
            log = new FileLogger("logs");
        }
        catch (IOException e)
        {
            log = new ConsoleLogger();
            e.printStackTrace();
        }
        ITimer timer = new Timer();

        b.initialize(params);
        b.warmUp();

        timer.start();
        b.run();
        long elapsed = timer.stop();

        log.write((String)params[0], "Matrix Multiplication", elapsed, TimeUnit.SEC, -1);
        log.close();
    }
}
