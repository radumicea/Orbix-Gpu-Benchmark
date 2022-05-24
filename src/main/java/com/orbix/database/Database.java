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


    //@Override
    public boolean equalElement(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        //String toCompare= new Stri
        //return Objects.equals(user, toCompare) || Objects.equals(gpu, toCompare) || Objects.eq
        String toCompare= (String)o;
        return toCompare.equals(getGpu());
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, gpu, time, bench, score);
    }
}