package com.orbix.logging;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class BenchResult {

  public final String utcdateTime;
  private final String dateTime;
  private final String userName;
  private final String GPUName;
  private final String benchName;
  private final long score;

  public BenchResult(
    String utcDateTime,
    String userName,
    String GPUName,
    String benchName,
    long score
  ) {
    this.utcdateTime = utcDateTime;
    dateTime =
      LocalDateTime
        .parse(utcDateTime, DateTimeFormatter.ISO_DATE_TIME)
        .atZone(ZoneId.systemDefault())
        .toString();
    this.userName = userName;
    this.GPUName = GPUName;
    this.benchName = benchName;
    this.score = score;
  }

  public String getDateTime() {
    return dateTime;
  }

  public String getUserName() {
    return userName;
  }

  public String getGPUName() {
    return GPUName;
  }

  public String getBenchName() {
    return benchName;
  }

  public long getScore() {
    return score;
  }

  public String getResult() {
    return (
      "DateTime: " +
      dateTime +
      "\nUser: " +
      userName +
      "\nGPU: " +
      GPUName +
      "\nBenchmark: " +
      benchName +
      "\nScore: " +
      score
    );
  }

  public String getCSVResult() {
    return (
      "\"" +
      dateTime +
      "\"," +
      "\"" +
      userName +
      "\"," +
      "\"" +
      GPUName +
      "\"," +
      "\"" +
      benchName +
      "\"," +
      score +
      "\n"
    );
  }

  public boolean searchElement(String s) {
    return (
      userName.contains(s) ||
      GPUName.contains(s) ||
      benchName.contains(s) ||
      String.valueOf(score).contains(s)
    );
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((GPUName == null) ? 0 : GPUName.hashCode());
    result = prime * result + ((benchName == null) ? 0 : benchName.hashCode());
    result = prime * result + (int) (score ^ (score >>> 32));
    result = prime * result + ((userName == null) ? 0 : userName.hashCode());
    result =
      prime * result + ((utcdateTime == null) ? 0 : utcdateTime.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    BenchResult other = (BenchResult) obj;
    if (GPUName == null) {
      if (other.GPUName != null) return false;
    } else if (!GPUName.equals(other.GPUName)) return false;
    if (benchName == null) {
      if (other.benchName != null) return false;
    } else if (!benchName.equals(other.benchName)) return false;
    if (score != other.score) return false;
    if (userName == null) {
      if (other.userName != null) return false;
    } else if (!userName.equals(other.userName)) return false;
    if (utcdateTime == null) {
      if (other.utcdateTime != null) return false;
    } else if (!utcdateTime.equals(other.utcdateTime)) return false;
    return true;
  }
}
