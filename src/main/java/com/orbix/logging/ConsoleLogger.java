package com.orbix.logging;

public class ConsoleLogger implements ILogger
{
    @Override
    public void write(long l)
    {
        System.out.println(l);
    }

    @Override
    public void write(String s)
    {
        System.out.println(s);
    }

    @Override
    public void write(Object... objects)
    {
        for (Object object : objects)
        {
            System.out.print(object);
        }
        System.out.println();
    }

    @Override
    public void writeTime(double measured, TimeUnit unit)
    {
        System.out.println(TimeUnit.toUnit(measured, unit));
    }

    @Override
    public void writeTime(String string, double measured, TimeUnit unit)
    {
        System.out.println(string + TimeUnit.toUnit(measured, unit));
    }

    /**
     * Not necessary for ConsoleLogger.
     */
    @Override
    public void close() { }
}
