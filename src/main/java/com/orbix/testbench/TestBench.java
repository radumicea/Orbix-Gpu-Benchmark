package com.orbix.testbench;

import com.aparapi.device.OpenCLDevice;
import com.orbix.bench.BenchmarkingMethods;
import com.orbix.bench.DataTransferBenchmark;
import com.orbix.bench.FractalsBenchmark;
import com.orbix.bench.IBenchmark;
import com.orbix.bench.MatrixMultiplicationBenchmark;
import com.orbix.bench.TrigonometryBenchmark;
import com.orbix.logging.BenchResult;
import java.time.Instant;
import javafx.concurrent.Task;

import javax.swing.*;

@SuppressWarnings("unchecked")
public final class TestBench extends Task<BenchResult> {

  private final OpenCLDevice GPU;
  private final BenchmarkingMethods benchMethod;
  private final Class<? extends IBenchmark>[] benchClasses;

  public TestBench(OpenCLDevice GPU, BenchmarkingMethods benchMethod) {
    this.GPU = GPU;
    this.benchMethod = benchMethod;

    switch (benchMethod) {
      case StandardBenchmark:
        {
          benchClasses = new Class[4];
          benchClasses[0] = DataTransferBenchmark.class;
          benchClasses[1] = TrigonometryBenchmark.class;
          benchClasses[2] = MatrixMultiplicationBenchmark.class;
          benchClasses[3] = FractalsBenchmark.class;
          break;
        }
      default:
        {
          benchClasses = new Class[1];
          try {
            benchClasses[0] =
              (Class<? extends IBenchmark>) Class.forName(
                "com.orbix.bench." + benchMethod
              );
          } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
          }
        }
    }
  }

  @Override
  protected BenchResult call() {
    JFrame progressFrame = new JFrame("Standard Benchmark Progress");
    JPanel progressPanel = new JPanel();
    JProgressBar progressBar = new JProgressBar();
    try {
      double score = 0;
      progressBar.setValue(0);
      progressBar.setString("The " + benchClasses[0].getDeclaredConstructor()+ " is running");
      progressBar.setStringPainted(true);
      progressPanel.add(progressBar);
      progressFrame.add(progressPanel);
      if (benchClasses.length == 4){
        progressFrame.setSize(200, 200);
        progressFrame.pack();
        progressFrame.setLocationRelativeTo(null);
      }
      int value = 0;
      String benchName = "";
      for (Class<? extends IBenchmark> benchClass : benchClasses) {
        IBenchmark bench = benchClass.getDeclaredConstructor().newInstance();
        bench.initialize(GPU);
        if (benchClasses.length == 4)
        {


          switch(benchClass.getName()){
            case "com.orbix.bench.DataTransferBenchmark":
              benchName = "DataTransferBenchmark";
              break;
            case "com.orbix.bench.TrigonometryBenchmark":
              benchName = "TrigonometryBenchmark";
              value += 5;
              break;
            case "com.orbix.bench.MatrixMultiplicationBenchmark":
              benchName = "MatrixMultiplicationBenchmark";
              value += 30;
              break;
            case "com.orbix.bench.FractalsBenchmark":
              benchName = "FractalsBenchmark";
              value += 50;
              break;

          }
          progressBar.setString("The " + benchName + " is running");
          progressFrame.setVisible(true);
          progressBar.setValue(value);
        }
        bench.run();
        score += getScore(bench.getResult(), benchClass);
      }
      progressBar.setString("All the benchmarks were run");
      value += 15;
      progressBar.setValue(value);
      progressFrame.setVisible(false);

      return new BenchResult(
        Instant.now().toString(),
        System.getProperty("user.name"),
        GPU.getName(),
        benchMethod.toString(),
        Math.round(score)
      );
    } catch (Throwable t) {
      progressFrame.setVisible(false);
      t.printStackTrace();
      System.exit(-1);
      return null;
    }
  }

  /**
   * Using FractalsBenchmark and a Vega RX 8 iGPU as a base, I
   * found that 1 FractalsBenchmark score point is worth:
   * <br></br>
   * 6e-6 {@link DataTransferBenchmark} points<br></br>
   * 446e-6 {@link TrigonometryBenchmark} points<br></br>
   * 0.013314 {@link MatrixMultiplicationBenchmark} points
   */
  private double getNormalizedScore(
    long result,
    Class<? extends IBenchmark> benchClass
  ) {
    if (benchClass.equals(DataTransferBenchmark.class)) {
      return result * 6e-6d;
    } else if (benchClass.equals(TrigonometryBenchmark.class)) {
      return result * 446e-6d;
    } else if (benchClass.equals(MatrixMultiplicationBenchmark.class)) {
      return result * 0.013314;
    } else if (benchClass.equals(FractalsBenchmark.class)) {
      return result;
    }

    return 0;
  }

  /**
   * {@link DataTransferBenchmark} is worth 5% of the std benchmark score
   * <br></br>
   * {@link TrigonometryBenchmark} is worth 30% of the std benchmark score
   * <br></br>
   * {@link MatrixMultiplicationBenchmark} is worth 50% of the std benchmark score
   * <br></br>
   * {@link FractalsBenchmark} is worth 15% of the std benchmark score
   */
  private double getScore(long result, Class<? extends IBenchmark> benchClass) {
    double score = getNormalizedScore(result, benchClass);

    if (benchMethod != BenchmarkingMethods.StandardBenchmark) {
      return score;
    }

    if (benchClass.equals(DataTransferBenchmark.class)) {
      return score / 100d * 5d;
    } else if (benchClass.equals(TrigonometryBenchmark.class)) {
      return score / 100d * 30d;
    } else if (benchClass.equals(MatrixMultiplicationBenchmark.class)) {
      return score / 100d * 50d;
    } else if (benchClass.equals(FractalsBenchmark.class)) {
      return score / 100d * 15d;
    }

    return 0;
  }
}
