package com.orbix.bench;

import com.aparapi.Kernel;
import com.aparapi.Range;
import com.aparapi.device.Device;

/**
 * A benchmark testing the parallelization capabilities of a GPU
 * through a highly parallelizable task, namely matrix
 * multiplication.
 */
public final class MatrixMultBenchmark extends AbstractGPUBenchmark
{
    private static final int R1 = 10_000;
    private static final int C1_R2 = 10_000;
    private static final int C2 = 10_000;

    private static final byte a[] = new byte[R1 * C1_R2];
    private static final byte b[] = new byte[C1_R2 * C2];
    private static final byte res[] = new byte[R1 * C2];

    static
    {
        int i = 0;
        for (i = 0; i < a.length && i < b.length; i++)
        {
            a[i] = (byte)RANDOM.nextInt();
            b[i] = a[i];
        }
        for (int j = i; j < a.length; j++)
        {
            a[j] = (byte)RANDOM.nextInt();
        }
        for (int j = i; j < b.length; j++)
        {
            b[j] = (byte)RANDOM.nextInt();
        }
    }

    private Kernel kernel;
    private Device GPU;

    /**
     * @param params
     * <code>params[0]</code> must be the name of the GPU to be benchmarked.<br></br>
     */
    @Override
    public void initialize(Object... params)
    {
        kernel = getKernel(R1, C1_R2, C2, a, b, res);
        GPU = getGPU((String)params[0]);
    }

    private static Kernel getKernel(final int r1, final int c1_r2, final int c2,
                                    final byte[] a, final byte[] b, final byte[] res)
    {
        return new Kernel()
        {
            @Override
            public void run()
            {
                int i = getGlobalId();
                int row = i / c2;
                int col = i % c2;
                for (int j = 0; j < c1_r2; j++)
                {
                    res[i] += a[row * c1_r2 + j] * b[j * c2 + col];
                }
            }
        };
    }

    @Override
    public void warmUp() throws Exception
    {
        kernel.compile(GPU);
    }

    @Override
    public void run() throws Exception
    {
        int maxGroupSize = kernel.getKernelMaxWorkGroupSize(GPU);
        // MUST BE A FACTOR OF groupSize!!!
        int rangeSize = (int)Math.ceil(R1 * C2 / (float)maxGroupSize) * maxGroupSize;
        // There is a bug in aparapi. Must explicitly state the localWidth
        // (a multiple of groupSize) when explicitly selecting a device, otherwise it won't work!
        Range range = GPU.createRange(rangeSize, maxGroupSize);
        kernel.execute(range);
    }

    /**
     * Will most likely not work properly.
     * There's nothing we can do about it.
     */
    @Override
    public void cancel()
    {
        kernel.cancelMultiPass();
    }

    @Override
    public void cleanUp()
    {
        kernel.dispose();
    }

    public double getExecutionTimeMs()
    {
        return kernel.getProfileReportLastThread(GPU).get().getExecutionTime();
    }
}
