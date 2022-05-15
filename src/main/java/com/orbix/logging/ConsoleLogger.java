package com.orbix.logging;

public class ConsoleLogger implements ILogger {

  @Override
  public void write(BenchResult benchResult) {
    System.out.println(benchResult.getResult());
  }

  @Override
  public void close() {}
}
