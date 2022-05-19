package com.orbix.gui.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;

public class Database {

    private String user;
    private String gpu;
    private String time;
    private String bench;
    private Long score;

   /* public Database() throws IOException
    {
        /*final ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<Database> rec = objectMapper.readValue(
                new File("records.json"),
                new TypeReference<ArrayList<Database>>() {
                });
        rec.forEach(x -> System.out.println(x.toString()));*/


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getGpu() {
        return gpu;
    }

    public void setGpu(String gpu) {
        this.gpu = gpu;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBench() {
        return bench;
    }

    public void setBench(String bench) {
        this.bench = bench;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }
}