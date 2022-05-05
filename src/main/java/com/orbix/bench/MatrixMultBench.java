package com.orbix.bench;

import com.aparapi.Kernel;
import com.aparapi.Range;
import com.aparapi.device.Device;
import com.aparapi.device.OpenCLDevice;
import com.aparapi.exception.QueryFailedException;

public class MatrixMultBench implements IBenchmark
{
    private static final int STD_R1 = 10_000;
    private static final int STD_C1_R2 = 10_000;
    private static final int STD_C2 = 10_000;

    private int r1;
    private int c1_r2;
    private int c2;

    private byte a[];
    private byte b[];
    private byte res[];

    /**
     * Initialize the matrices to me multiplied. If no parameters are provided, a standard benchmark will be initialized.
     * @param params : three integers, representing the <code>r1</code>, <code>c1_r2</code> and <code>c2</code> of the matrices.
     */
    @Override
    public void initialize(Object... params)
    {
        if (params.length == 0)
        {
            r1 = STD_R1;
            c1_r2 = STD_C1_R2;
            c2 = STD_C2;
        }
        else
        {
            r1 = (int)params[0];
            c1_r2 = (int)params[1];
            c2 = (int)params[2];
        }

        a = new byte[r1 * c1_r2];
        b = new byte[c1_r2 * c2];
        res = new byte[r1 * c2];

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
    public void warmUp()
    {
        final int r1 = 100;
        final int c1_r2 = 100;
        final int c2 = 100;

        final byte a[] = new byte[r1 * c1_r2];
        final byte b[] = new byte[c1_r2 * c2];

        initMatrices(a, b);
        benchRun(a, b, new byte[r1 * c2], r1, c1_r2, c2);
    }

    @Override
    public void run()
    {
        benchRun(a, b, res, r1, c1_r2, c2);
    }

    /**
     * Will simply call <code>run()</code>
     */
    @Override
    public void run(Object... params)
    {
        run();
    }

    private static void benchRun(final byte a[], final byte b[], final byte res[], final int r1, final int c1_r2, final int c2)
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
        
        Device device = OpenCLDevice.listDevices(null).get(0);
        try
        {
            int maxGroupSize = kernel.getKernelMaxWorkGroupSize(device);
            // MUST BE A FACTOR OF maxGroupSize!!!
            int rangeSize = (int)Math.ceil(r1 * c2 / (float)maxGroupSize) * maxGroupSize;
            // There is a bug in aparapi. Must explicitly state the localWidth
            // (aka groupSize) when explicitly selecting a device, otherwise it won't work!
            Range range = device.createRange(rangeSize, maxGroupSize);
            kernel.execute(range);
        }
        catch (QueryFailedException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void cancel()
    {
        
    }
}
