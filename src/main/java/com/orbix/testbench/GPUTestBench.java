package com.orbix.testbench;

import java.io.IOException;

import com.orbix.bench.IBenchmark;
import com.orbix.bench.MatrixMultBenchmark;
import com.orbix.logging.ConsoleLogger;
import com.orbix.logging.BenchResult;
import com.orbix.logging.CSVLogger;
import com.orbix.logging.ILogger;
import com.orbix.logging.TimeUnit;
import com.orbix.timing.ITimer;
import com.orbix.timing.Timer;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

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
     * @throws Exception
     */
    public static BenchResult runMatrixMultBench(Object... params) throws Exception
    {
        IBenchmark b = new MatrixMultBenchmark();
        ILogger log;
        try
        {
            log = new CSVLogger("logs");
        }
        catch (IOException e)
        {
            log = new ConsoleLogger();
            displayFileWarningAlert();
            e.printStackTrace();
        }
        ITimer timer = new Timer();

        b.initialize(params);
        b.warmUp();

        timer.start();
        b.run();
        long elapsed = timer.stop();

        BenchResult benchResult = new BenchResult(
                        System.getProperty("user.name"),
                        (String)params[0],
                        "Matrix Multiplication",
                        TimeUnit.toUnit(elapsed, TimeUnit.SEC),
                        -1);

        log.write(benchResult);
        log.close();

        return benchResult;
    }

    private static void displayFileWarningAlert()
    {
        Alert a = new Alert(AlertType.WARNING,
                            "Can not open the logs file. Will write to the console instead.",
                            ButtonType.OK);
        a.setTitle("File Open Warning");
        a.setHeaderText(null);
        a.show();
    }
}
