package com.orbix.gui.controllers;

public class Database {
    private String name;
    private String gpu;
    private String time;
    private String bench;
    private Long score;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
