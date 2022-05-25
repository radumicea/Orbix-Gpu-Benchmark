package com.orbix.bench;

import com.aparapi.Range;
import com.aparapi.device.OpenCLDevice;
import com.aparapi.exception.CompileFailedException;

public class FractalsBenchmark implements IBenchmark {

  // We want to know the nr. of
  // iterations computed in 2s
  private static final double EXPECTED_TIME_MS = 2_000;
  // Admissible error
  private static final double EPSILON = 10 * EXPECTED_TIME_MS / 100;

  // Frame coordinates
  private static final double CX1 = -2;
  private static final double CY1 = -2;
  private static final double CX2 = 2;
  private static final double CY2 = 2;

  // W & H of the frames
  private static final int WIDTH = 1_000;
  private static final int HEIGHT = 1_000;

  private MandelbrotKernel kernel;
  private OpenCLDevice GPU;

  private int iterations;

  /**
   * @param params
   * <code>params[0]</code> must be the GPU to be benchmarked.<br></br>
   * @throws CompileFailedException
   */
  @Override
  public void initialize(Object... params) throws CompileFailedException {
    GPU = (OpenCLDevice) params[0];
  }

  @Override
  public void run() throws Exception {
    // Empirically found to be best
    int localWidth = 8;
    int localHeight = 8;

    /** global sizes must be a multiple of local sizes */
    int globalWidth = (1 + WIDTH / localWidth) * localWidth;
    int globalHeight = (1 + HEIGHT / localHeight) * localHeight;

    Range range = GPU.createRange2D(
      globalWidth,
      globalHeight,
      localWidth,
      localHeight
    );

    iterations = 0;
    double executionTime;

    do {
      iterations += 16_384;

      kernel =
        new MandelbrotKernel(iterations, CX1, CY1, CX2, CY2, WIDTH, HEIGHT);
      kernel.compile(GPU);

      kernel.execute(range);

      executionTime = kernel.getExecutionTime() - kernel.getConversionTime();
      kernel.dispose();
    } while (EXPECTED_TIME_MS - executionTime > EPSILON);
  }

  /**
   * return nr. of iterations computed in
   * {@link FractalsBenchmark#EXPECTED_TIME_MS} ms.
   */
  @Override
  public long getResult() {
    return iterations;
  }
}
