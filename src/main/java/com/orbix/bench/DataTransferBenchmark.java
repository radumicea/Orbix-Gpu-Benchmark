package com.orbix.bench;

import com.aparapi.Kernel;
import com.aparapi.Range;
import com.aparapi.device.OpenCLDevice;

/**
 * A benchmark testing the data transfer capabilities of a GPU.
 * <br></br>
 * THE INTEGRATED GPU DOES NOT HAVE ITS OWN MEMORY.
 * IT SIMPLY TAKES A CHUCK FROM THE MAIN MEMORY AND CALLS IT HIS.
 * BECAUSE OF THIS, THE RUNTIME IS EXTREMELY FAST.
 * IT SHOULD BE HEAVILY PENALIZED WHEN COMPUTING THE SCORE BASED
 * ON THIS BENCHMARK.
 * (Now, should it? The dedicated GPU also makes use of shared memory;
 * you could argue it's just that the iGPU is a lot smarter in this regard.)
 */
public final class DataTransferBenchmark implements IBenchmark
{
    private static final int LOOPS = 100;
    private static final int _512MB = 536_870_912;
    private static final byte[] BUF;
    static
    {
        BUF = new byte[_512MB];
        for (int i = 0; i < _512MB; i++)
        {
            BUF[i] = (byte)i;
        }
    }

    private Kernel kernel;
    private OpenCLDevice GPU;

    private boolean running;
    private double executionTime;

    /**
     * @param params
     * <code>params[0]</code> must be the name of the GPU to be benchmarked.<br></br>
     */
    @Override
    public void initialize(Object... params)
    {
        running = false;
        kernel = getKernel(BUF, _512MB);
        GPU = (OpenCLDevice)params[0];
    }

    private static Kernel getKernel(final byte[] arr, final int len)
    {
        return new Kernel()
        {
            // Only dereference the first and last elements
            // in order to ensure the entire buffer is being
            // transferred to the GPU. (Prevent any possible
            // sort of leazy loading or anything similar.)
            @Override
            public void run()
            {
                byte x = arr[0];
                byte y = arr[arr.length - 1];
            }
        };
    }

    @Override
    public void warmUp() throws Exception
    {
        kernel.compile(GPU);
        runHelper(LOOPS / 5, true);
    }

    @Override
    public void run() throws Exception
    {
        running = true;
        executionTime = 0;
        runHelper(LOOPS, false);
    }

    private void runHelper(int loops, boolean warmup) throws Exception
    {
        int groupSize = kernel.getKernelPreferredWorkGroupSizeMultiple(GPU);
        // Range should be 0 because we don't have to do a specific
        // task in a loop inside kernel.execute. In fact we don't
        // really need to do anything. kernel.execute will load the
        // entire BUF, loops times into the VRAM and we must measure
        // it's runtime.
        // However, when we execute on a specific device, aparapi requires
        // we specify the group size and have a non-zero multiple of groupSize
        // as localWidth.
        Range range = GPU.createRange(groupSize, groupSize);
        
        for (int i = 0; i < loops && (running || warmup); i++)
        {
            kernel.execute(range);
            executionTime += kernel.getProfileReportLastThread(GPU)
                                   .get().getExecutionTime();
        }
    }

    @Override
    public double getExecutionTimeMs()
    {
        return executionTime;
    }

    @Override
    public void cancel()
    {
        running = false;
    }

    @Override
    public void cleanUp()
    {
        running = false;
        kernel.dispose();
        executionTime = 0;
    }
}
