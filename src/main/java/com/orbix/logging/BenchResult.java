package com.orbix.logging;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BenchResult
{
    private static final ObjectMapper objMapper = new ObjectMapper();

    private final String userName;
    private final String GPUName;
    private final String benchName;
    private final String runTime;
    private final int score;

    public String getUserName()
    {
        return userName;
    }

    public String getGPUName()
    {
        return GPUName;
    }

    public String getBenchName()
    {
        return benchName;
    }

    public String getRunTime()
    {
        return runTime;
    }

    public int getScore()
    {
        return score;
    }

    public BenchResult(String userName, String GPUName,
                       String benchName, String runTime, int score)
    {
        this.userName = userName;
        this.GPUName = GPUName;
        this.benchName = benchName;
        this.runTime = runTime;
        this.score = score;
    }

    @JsonIgnore
    public String getResult()
    {
        return
        "User: " + userName +
        "\nGPU: " + GPUName +
        "\nBenchmark: " + benchName +
        "\nRuntime: " + runTime +
        "\nScore: " + score;
    }

    @JsonIgnore
    public String getCSVResult()
    {
        return
        "\"" + userName + "\"," +
        "\"" + GPUName + "\"," +
        "\"" + benchName + "\"," +
        runTime + "," +
        score + "\n";
    }

    @JsonIgnore
    public String getJSONResult()
    {
        try
        {
            return objMapper.writeValueAsString(this);
        }
        catch (JsonProcessingException e)
        {
            e.printStackTrace();
            return getCSVResult();
        }
    }
}
