package com.orbix.logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.orbix.gui.AlertDisplayer;

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
            fw.write("DateTime,User,GPU,Benchmark,Runtime(ms),Score\n");
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
            new ConsoleLogger().write(benchResult);
            AlertDisplayer.displayWarning(
                "File Write Warning",
                null,
                "Can not write to the " + f.getName() +
                    " file. Will write to the console instead.");
            e.printStackTrace();
        }
    }

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
