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

    private Device GPU;

    private byte a[];
    private byte b[];
    private byte res[];

    /**
     * @param params
     * <code>params[0]</code> must be the name of the GPU to be benchmarked.<br></br>
     */
    @Override
    public void initialize(Object... params)
    {
        GPU = getGPU((String)params[0]);

        a = new byte[R1 * C1_R2];
        b = new byte[C1_R2 * C2];
        res = new byte[R1 * C2];

        initMatrices(a, b);
    }

    private static void initMatrices(byte[] a, byte[] b)
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

    @Override
    public void warmUp() throws Exception
    {
        final int r1 = 100;
        final int c1_r2 = 100;
        final int c2 = 100;

        final byte a[] = new byte[r1 * c1_r2];
        final byte b[] = new byte[c1_r2 * c2];

        initMatrices(a, b);
        runHelper(GPU, r1, c1_r2, c2, a, b, new byte[r1 * c2]);
    }

    @Override
    public void run() throws Exception
    {
        runHelper(GPU, R1, C1_R2, C2, a, b, res);
    }

    // Kernel wants to be as isolated and thread safe as possible. Because of this,
    // the only way it would not throw exception is when used in a static method.
    private static void runHelper(
        final Device GPU,
        final int r1, final int c1_r2, final int c2,
        final byte[] a, final byte[] b, final byte[] res) throws Exception
    {
        Kernel kernel = new Kernel()
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

        int maxGroupSize = kernel.getKernelMaxWorkGroupSize(GPU);
        // MUST BE A FACTOR OF maxGroupSize!!!
        int rangeSize = (int)Math.ceil(r1 * c2 / (float)maxGroupSize) * maxGroupSize;
        // There is a bug in aparapi. Must explicitly state the localWidth
        // (aka groupSize) when explicitly selecting a device, otherwise it won't work!
        Range range = GPU.createRange(rangeSize, maxGroupSize);
        kernel.execute(range);
    }
}
