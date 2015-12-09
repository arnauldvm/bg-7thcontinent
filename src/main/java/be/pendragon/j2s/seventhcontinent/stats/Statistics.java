/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.pendragon.j2s.seventhcontinent.stats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.StatUtils;

/**
 *
 * @author avm
 */
public class Statistics {
  public final double average;
  public final int min;
  public final int max;
  public final long count;
  private final double[] percentiles;

  public Statistics(double average, int min, int max, long count, double[] percentiles) {
    this.average = average;
    this.min = min;
    this.max = max;
    this.count = count;
    this.percentiles = percentiles;
  }
  
  public double getPercentile(int pct) {
    return percentiles[pct-1];
  }

  public double[] getPercentiles() {
    return Arrays.copyOf(percentiles, percentiles.length);
  }
  
  @Override
  public String toString() {
    return String.format("Avg=%f, Min=%d, Max=%d, Count=%d, Pct25=%f, Pct50=%f, Pct75=%f, Pct=%s",
            average, min, max, count, percentiles[24], percentiles[49], percentiles[74], ArrayUtils.toString(percentiles));
  }
  
  public static class Accumulator {
    private final IntSummaryStatistics intStatsAccumulator;
    private final List<Double> values;

    public Accumulator() {
        this.intStatsAccumulator = new IntSummaryStatistics();
        this.values = new ArrayList<>();
    }

    public void accept(int value) {
      intStatsAccumulator.accept(value);
      values.add((double) value);
    }
    
    public Statistics compute() {
      double[] percentiles = new double[100];
      final double[] valuesArray = ArrayUtils.toPrimitive(this.values.toArray(new Double[0]));
      for (int pct=1; pct<=100; pct++) {
        percentiles[pct-1] = StatUtils.percentile(valuesArray, pct);
      }
      return new Statistics(
              intStatsAccumulator.getAverage(),
              intStatsAccumulator.getMin(),
              intStatsAccumulator.getMax(),
              intStatsAccumulator.getCount(),
              percentiles
      );
    }
  }
  
}
