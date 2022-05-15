package com.orbix.bench;

import com.aparapi.Kernel;
import com.aparapi.Range;
import com.aparapi.device.OpenCLDevice;
import com.aparapi.exception.CompileFailedException;

/**
 * A benchmark testing the data transfer capabilities of a GPU.
 */
public final class DataTransferBenchmark implements IBenchmark {

  // We want to know the data size
  // that will be transmitted in 2s
  private static final double EXPECTED_TIME_MS = 2_000;
  // Admissible error
  private static final double EPSILON = 10 * EXPECTED_TIME_MS / 100;

  private static final int _256MB = 268_435_456;
  private static final byte[] BUF = new byte[_256MB];

  static {
    for (int i = 0; i < _256MB; i++) {
      BUF[i] = (byte) i;
    }
  }

  private Kernel kernel;
  private OpenCLDevice GPU;

  private long loops;

  /**
   * @param params
   * <code>params[0]</code> must be the GPU to be benchmarked.<br></br>
   * @throws CompileFailedException
   */
  @Override
  public void initialize(Object... params) throws CompileFailedException {
    kernel = getKernel(BUF, _256MB);
    GPU = (OpenCLDevice) params[0];
    kernel.compile(GPU);
  }

  private static Kernel getKernel(final byte[] arr, final int len) {
    return new Kernel() {
      // Only dereference the first and last elements
      // in order to ensure the entire buffer is being
      // transferred to the GPU. (Prevent any possible
      // sort of leazy loading or anything similar.)
      @Override
      public void run() {
        byte x = arr[0];
        byte y = arr[len - 1];
      }
    };
  }

  @Override
  public void run() throws Exception {
    int groupSize = kernel.getKernelPreferredWorkGroupSizeMultiple(GPU);
    // Range should be 0 because we don't have to do a specific
    // task in a loop inside kernel.execute. In fact we don't
    // really need to do anything. kernel.execute will load the
    // entire BUF, LOOPS times into the VRAM and we must measure
    // it's runtime.
    // However, when we execute on a specific device, aparapi requires
    // we specify the group size and have a non-zero multiple of groupSize
    // as localWidth.
    Range range = GPU.createRange(groupSize, groupSize);

    kernel.execute(range);
    double executionTime =
      kernel.getExecutionTime() - kernel.getConversionTime();
    loops = 1;

    while (EXPECTED_TIME_MS - executionTime > EPSILON) {
      kernel.execute(range);
      executionTime += kernel.getExecutionTime();
      loops++;
    }

    kernel.dispose();
  }

  /**
   * return nr. bytes moved in
   * {@link DataTransferBenchmark#EXPECTED_TIME_MS} ms.
   */
  @Override
  public long getResult() {
    return _256MB * loops;
  }
}
