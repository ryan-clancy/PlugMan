package com.rylinaux.plugman.utilities;

public class Profiler {

    private long start = 0;

    private long end = 0;

    private String name;

    public Profiler(String name) {
        this.name = name;
    }

    public void start() {
        start = System.currentTimeMillis();
    }

    public void end() {
        end = System.currentTimeMillis();
    }

    public String toString() {
        return (String.format("Operation (%s) took %sms.", name, (end - start)));
    }
}