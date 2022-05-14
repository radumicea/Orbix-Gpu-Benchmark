package com.orbix.logging;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BenchResult
{
    private static final ObjectMapper objMapper = new ObjectMapper();

    public final String utcDateTime;
    public final String userName;
    public final String GPUName;
    public final String benchName;
    public final long score;

    public BenchResult(String utcDateTime, String userName, String GPUName,
                       String benchName, long score)
    {
        this.utcDateTime = utcDateTime;
        this.userName = userName;
        this.GPUName = GPUName;
        this.benchName = benchName;
        this.score = score;
    }

    @JsonIgnore
    public String getResult()
    {
        return
        "DateTime: " +
        LocalDateTime.parse(utcDateTime, DateTimeFormatter.ISO_DATE_TIME)
                     .atZone(ZoneId.systemDefault()) + 
        "\nUser: " + userName +
        "\nGPU: " + GPUName +
        "\nBenchmark: " + benchName +
        "\nScore: " + score;
    }

    @JsonIgnore
    public String getCSVResult()
    {
        return
        "\"" + LocalDateTime.parse(utcDateTime, DateTimeFormatter.ISO_DATE_TIME)
                            .atZone(ZoneId.systemDefault()) + "\"," +
        "\"" + userName + "\"," +
        "\"" + GPUName + "\"," +
        "\"" + benchName + "\"," +
        score + "\n";
    }

    @JsonIgnore
    public String getJSONResult() throws JsonProcessingException
    {
        return objMapper.writeValueAsString(this);
    }
}
