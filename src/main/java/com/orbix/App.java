package com.orbix;

import com.aparapi.Kernel;
import com.aparapi.Range;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        final float inA[] = { 1.2F, 2.3F, 1F, 8F, 11.5F };
        final float inB[] = { 2.2F, 8.3F, 9F, 10F, 2.444443F };
        assert (inA.length == inB.length);
        final float result[] = new float[inA.length];

        Kernel kernel = new Kernel() {
            @Override
            public void run() {
                int i = getGlobalId();
                result[i] = inA[i] + inB[i];
            }
        };

        Range range = Range.create(result.length);
        kernel.execute(range);

        for (float f : result)
        {
            System.out.println(f);
        }
    }
}
