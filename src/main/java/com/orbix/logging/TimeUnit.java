package com.orbix.logging;

public enum TimeUnit
{
    NANO,
    MICRO,
    MILLI,
    SEC;

    public static String toUnit(double nanoSeconds, TimeUnit unit)
    {
        switch (unit)
        {
            case MICRO:
                return nanoSeconds / 1_000.0 + "us";

            case MILLI:
                return nanoSeconds / 1_000_000.0 + "ms";

            case SEC:
                return nanoSeconds / 1_000_000_000.0 + "s";

            default:
                return nanoSeconds + "ns";
        }
    }
}
