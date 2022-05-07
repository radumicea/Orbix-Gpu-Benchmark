package com.orbix.logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CSVLogger implements ILogger
{
    private final File f;
    private final FileWriter fw;

    /**
     * @param s : name of the file.
     * @throws IOException
     */
    public CSVLogger(String s) throws IOException
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
    public void write(BenchResult benchResult)
    {
        try
        {
            fw.write(benchResult.getCSVResult());
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
