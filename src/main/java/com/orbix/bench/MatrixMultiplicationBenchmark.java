package com.orbix.bench;

import com.aparapi.Kernel;
import com.aparapi.Range;
import com.aparapi.device.OpenCLDevice;

/**
 * A benchmark testing the parallelization capabilities of a GPU
 * through a highly parallelizable task, namely matrix
 * multiplication.
 */
public final class MatrixMultiplicationBenchmark implements IBenchmark {

  // We want to know the sizes of the matrices
  // such that the benchmark will run for 2s
  private static final double EXPECTED_TIME_MS = 2_000;
  // Admissible error
  private static final double EPSILON = 10 * EXPECTED_TIME_MS / 100;

  // Initial size values
  private static final int R1 = 256;
  private static final int C1_R2 = 256;
  private static final int C2 = 256;

  // Final size values
  private int r1;
  private int c1_r2;
  private int c2;

  private byte a[];
  private byte b[];
  private byte res[];

  private Kernel kernel;
  private OpenCLDevice GPU;

  /**
   * @param params
   * <code>params[0]</code> must be the GPU to be benchmarked.<br></br>
   */
  @Override
  public void initialize(Object... params) {
    GPU = (OpenCLDevice) params[0];
  }

  @Override
  public void run() throws Exception {
    int i = 1;
    double executionTime;

    do {
      r1 = R1 * i;
      c1_r2 = C1_R2 * i;
      c2 = C2 * i;

      initMatrices();
      kernel = getKernel(a, b, res, r1, c1_r2, c2);
      kernel.compile(GPU);
      runHelper(kernel, GPU, r1, c2);
      executionTime = kernel.getExecutionTime() - kernel.getConversionTime();
      kernel.dispose();

      i++;
    } while (EXPECTED_TIME_MS - executionTime > EPSILON);
  }

  private void initMatrices() {
    a = new byte[r1 * c1_r2];
    b = new byte[c1_r2 * c2];
    res = new byte[r1 * c2];

    int i;
    for (i = 0; i < a.length && i < b.length; i++) {
      a[i] = (byte) i;
      b[i] = (byte) (b.length - 1 - i);
    }
    for (int j = i; j < a.length; j++) {
      a[j] = (byte) j;
    }
    for (int j = i; j < b.length; j++) {
      b[j] = (byte) j;
    }
  }

  private static Kernel getKernel(
    final byte[] _a,
    final byte[] _b,
    final byte[] _res,
    final int _r1,
    final int _c1_r2,
    final int _c2
  ) {
    return new Kernel() {
      // O(n^2)
      @Override
      public void run() {
        int i = getGlobalId();
        int row = i / _c2;
        int col = i % _c2;
        for (int j = 0; j < _c1_r2; j++) {
          _res[i] += _a[row * _c1_r2 + j] * _b[j * _c2 + col];
        }
      }
    };
  }

  private static void runHelper(
    Kernel _kernel,
    OpenCLDevice _GPU,
    int _r1,
    int _c2
  ) throws Exception {
    int maxGroupSize = _kernel.getKernelMaxWorkGroupSize(_GPU);
    // MUST BE A FACTOR OF groupSize!!!
    int rangeSize = (int) Math.ceil(_r1 * _c2 / (double) maxGroupSize) *
    maxGroupSize;
    // There is a bug in aparapi. Must explicitly state the localWidth
    // (a multiple of groupSize) when explicitly selecting a device, otherwise it won't work!
    Range range = _GPU.createRange(rangeSize, maxGroupSize);
    _kernel.execute(range);
  }

  /**
   * returns the size in bytes s.t. multiplying two matrices
   * of this size is executed in
   * {@link MatrixMultiplicationBenchmark#EXPECTED_TIME_MS} ms.
   */
  @Override
  public long getResult() {
    return r1 * c2;
  }
}
