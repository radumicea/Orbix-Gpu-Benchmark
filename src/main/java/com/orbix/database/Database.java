package com.orbix.database;

import java.util.Objects;

public class Database {

    private String user;
    private String gpu;
    private String time;
    private String bench;
    private Long score;

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



    public boolean searchElement(String s) {
        return (this.user.contains(s) || this.gpu.contains(s)
                || this.bench.contains(s) || String.valueOf(this.score).contains(s));
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, gpu, time, bench, score);
    }
}