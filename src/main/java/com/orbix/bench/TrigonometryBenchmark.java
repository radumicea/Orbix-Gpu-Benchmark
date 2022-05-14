package com.orbix.bench;

import com.aparapi.Kernel;
import com.aparapi.Range;
import com.aparapi.device.OpenCLDevice;
import com.aparapi.exception.CompileFailedException;

/**
 * A benchmark testing the trigonometric functions
 * computation capabilities of a GPU.
 */
public final class TrigonometryBenchmark implements IBenchmark
{
    // We want to know the nr. of computations
    // such that the benchmark will run for 2s
    private static final double EXPECTED_TIME_MS = 2_000;
    // Admissible error
    private static final double EPSILON = 10 * EXPECTED_TIME_MS / 100;

    private Kernel kernel;
    private OpenCLDevice GPU;

    private int loops;

    /**
     * @param params
     * <code>params[0]</code> must be the GPU to be benchmarked.<br></br>
     * @throws CompileFailedException
     */
    @Override
    public void initialize(Object... params) throws CompileFailedException
    {
        kernel = getKernel();
        GPU = (OpenCLDevice)params[0];
        kernel.compile(GPU);
    }

    private static Kernel getKernel()
    {
        return new Kernel()
        {
            @Override
            public void run()
            {
                int i = getGlobalId();
                acos(i);
                asin(i);
                atan(i);
                sin(i);
            }
        };
    }

    @Override
    public void run() throws Exception
    {
        double executionTime;
        loops = 1_048_576;

        runHelper(kernel, GPU, loops);

        executionTime = kernel.getExecutionTime() - kernel.getConversionTime();

        do 
        {
            loops += 1_048_576;

            runHelper(kernel, GPU, loops);

            executionTime += kernel.getExecutionTime();

        } while (EXPECTED_TIME_MS - executionTime > EPSILON);

        kernel.dispose();
    }

    private static void runHelper(Kernel _kernel, OpenCLDevice _GPU, int _loops)
        throws Exception
    {
        int maxGroupSize = _kernel.getKernelMaxWorkGroupSize(_GPU);
        // MUST BE A FACTOR OF groupSize!!!
        int rangeSize = (int)Math.ceil(_loops / (double)maxGroupSize) * maxGroupSize;
        // There is a bug in aparapi. Must explicitly state the localWidth
        // (a multiple of groupSize) when explicitly selecting a device, otherwise it won't work!
        Range range = _GPU.createRange(rangeSize, maxGroupSize);

        _kernel.execute(range);
    } 

    /**
     * returns the nr of operations executed in
     * {@link TrigonometryBenchmark#EXPECTED_TIME_MS} ms.
     */
    @Override
    public long getResult()
    {
        return loops;
    }
}
