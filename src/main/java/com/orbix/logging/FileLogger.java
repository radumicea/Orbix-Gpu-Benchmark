package com.orbix.logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileLogger implements ILogger
{
    private final File f;
    private final FileWriter fw;

    /**
     * Will create an empty file to be written into.
     * @param s : name of the file. If such file already exists, it will be cleared.
     * @throws IOException
     */
    public FileLogger(String s) throws IOException
    {
        f = new File(s);
        f.delete();
        f.createNewFile();
        fw = new FileWriter(f);
    }

    @Override
    public void write(long l)
    {
        try
        {
            fw.write(Long.toString(l));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void write(String s)
    {
        try
        {
            fw.write(s);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void write(Object... objects)
    {
        for (Object object : objects)
        {
            try
            {
                fw.write(String.valueOf(object));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void writeTime(double measured, TimeUnit unit)
    {
        try
        {
            fw.write(TimeUnit.toUnit(measured, unit));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void writeTime(String string, double measured, TimeUnit unit)
    {
        try
        {
            fw.write(string + TimeUnit.toUnit(measured, unit));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Must be called after we are done writing in order to make sure everything has been flushed.
     */
    @Override
    public void close()
    {
        try
        {
            fw.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
