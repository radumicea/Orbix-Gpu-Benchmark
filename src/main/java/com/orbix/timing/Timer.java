package com.orbix.timing;

public class Timer implements ITimer
{
    private long elapsed = 0;
    private long recorded = 0;
    private TimerState state = TimerState.STOPPED;

    @Override
    public void start()
    {
        recorded = 0;
        resume();
    }

    @Override
    public long stop()
    {
        if (state.equals(TimerState.RUNNING))
        {
            elapsed = System.nanoTime() - elapsed;
            recorded += elapsed;
        }

        state = TimerState.STOPPED;

        return recorded;
    }

    @Override
    public long pause()
    {
        state = TimerState.PAUSED;
        elapsed = System.nanoTime() - elapsed;
        recorded += elapsed;

        return recorded;
    }

    @Override
    public void resume()
    {
        state = TimerState.RUNNING;
        elapsed = System.nanoTime();
    }

    private static enum TimerState
    {
        RUNNING,
        PAUSED,
        STOPPED;
    }
}
