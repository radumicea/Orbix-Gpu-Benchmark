package com.orbix.logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileLogger implements ILogger
{
    private final File f;
    private final FileWriter fw;

    /**
     * @param s : name of the file.
     * @throws IOException
     */
    public FileLogger(String s) throws IOException
    {
        f = new File(s + ".csv");
        if (!f.exists())
        {
            f.createNewFile();
            fw = new FileWriter(f, true);
            fw.write("User,GPU,Benchmark,Runtime,Score\n");
        }
        else
        {
            fw = new FileWriter(f, true);
        }
    }

    @Override
    public void write(String GPUName, String benchName,
        long runtime, TimeUnit timeUnit, int score)
    {
        try
        {
            fw.write("\"" + System.getProperty("user.name") + "\",");
            fw.write("\"" + GPUName + "\",");
            fw.write("\"" + benchName + "\",");
            fw.write(TimeUnit.toUnit(runtime, timeUnit) + ",");
            fw.write(String.valueOf(score) + "\n");
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
