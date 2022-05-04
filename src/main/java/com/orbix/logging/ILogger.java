package com.orbix.logging;

public interface ILogger
{
    void write(long l);
    void write(String s);
    void write(Object... objects);
    /**
     * Writes the <b>measured</b> time in a certain time <b>unit</b>.
     * @param measured
     * @param unit
     */
    void writeTime(double measured, TimeUnit unit);
    /**
     * Writes the <b>string</b>, then the <b>measured</b> time in a certain time <b>unit</b>.
     * @param string
     * @param measured
     * @param unit
     */
    void writeTime(String string, double measured, TimeUnit unit);
    /**
     * Flushes and closes the logger.
     */
    void close();
}
