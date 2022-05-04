package com.orbix;

import java.util.Random;

import com.aparapi.Kernel;
import com.aparapi.Range;

public class App 
{
    static final Random RANDOM = new Random();

    static final int ROWS = 3_000;
    static final int COLS = 3_000;

    static void testCPU(final byte a[], final byte b[], final byte res[], final int r1, final int c1_r2, final int c2)
    {
        int n = r1 * c2;

        for (int i = 0; i < n; i++)
        {
            int row = i / c2;
            int col = i % c2;
            for (int j = 0; j < c1_r2; j++)
            {
                res[i] += a[row * c1_r2 + j] * b[j * c2 + col];
            }
        }
    }

    static void testGPU(final byte a[], final byte b[], final byte res[], final int r1, final int c1_r2, final int c2)
    {
        Kernel kernel = new Kernel()
        {
            @Override
            public void run()
            {
                int i = getGlobalId();
                int row = i / c2;
                int col = i % c2;
                // Is this ran by the GPU or the CPU??
                for (int j = 0; j < c1_r2; j++)
                {
                    res[i] += a[row * c1_r2 + j] * b[j * c2 + col];
                }
                //
            }
        };

        Range range = Range.create(r1 * c2);
        kernel.execute(range);
    }

    static void printMatrix(final byte a[], int r, int c)
    {
        int n = r * c;

        for (int i = 0; i < n; i++)
        {
            int col = i % c;
            
            System.out.print(a[i] + " ");
            if (col == c - 1)
            {
                System.out.println();
            }
        }

        System.out.println();
    }

    static void warmupCPU(final int r1, final int c1_r2, final int c2)
    {
        final byte a[] = new byte[r1 * c1_r2];
        final byte b[] = new byte[c1_r2 * c2];

        for (int i = 0; i < a.length; i++)
        {
            a[i] = (byte)RANDOM.nextInt();
            b[i] = a[i];
        }

        long start = System.nanoTime();
        for (int i = 0; i < 5; i++)
        {
            testCPU(a, b, new byte[a.length], r1, c1_r2, c2);
        }
        long stop = System.nanoTime();
        System.out.println("CPU warmup: " + (stop - start) / 1_000_000_000.0);
    }

    static void warmupGPU(final int r1, final int c1_r2, final int c2)
    {
        final byte a[] = new byte[r1 * c1_r2];
        final byte b[] = new byte[c1_r2 * c2];

        for (int i = 0; i < a.length; i++)
        {
            a[i] = (byte)RANDOM.nextInt();
            b[i] = a[i];
        }

        long start = System.nanoTime();
        for (int i = 0; i < 5; i++)
        {
            testGPU(a, b, new byte[a.length], r1, c1_r2, c2);
        }
        long stop = System.nanoTime();
        System.out.println("GPU warmup: " + (stop - start) / 1_000_000_000.0);
    }

    public static void main( String[] args )
    {
        warmupCPU(100, 100, 100);        
        warmupGPU(100, 100, 100);

        final byte a[] = new byte[ROWS * COLS];
        final byte b[] = new byte[ROWS * COLS];
        final byte res1[] = new byte[ROWS * COLS];
        final byte res2[] = new byte[ROWS * COLS];

        for (int i = 0; i < a.length; i++)
        {
            a[i] = (byte)RANDOM.nextInt();
            b[i] = a[i];
        }

        long start = System.nanoTime();
        testCPU(a, b, res1, ROWS, ROWS, COLS);
        long stop = System.nanoTime();
        System.out.println("CPU: " + (stop - start) / 1_000_000_000.0);

        start = System.nanoTime();
        testGPU(a, b, res2, ROWS, ROWS, COLS);
        stop = System.nanoTime();
        System.out.println("GPU: " + (stop - start) / 1_000_000_000.0);
    }
}
