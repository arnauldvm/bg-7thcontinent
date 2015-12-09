/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.pendragon.j2s.seventhcontinent.plot.jfreechart;

import be.pendragon.j2s.seventhcontinent.plot.Plotter;

import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import be.pendragon.j2s.seventhcontinent.stats.Statistics;

/**
 *
 * @author avm
 */
public class JFreeChartPlotter implements Plotter {
//  private static final Logger LOG = Logger.getLogger(JFreeChartPlotter.class.getName());

  private int width;
  private int height;

  public JFreeChartPlotter() {
    this(640, 480);
  }

  public JFreeChartPlotter(int width, int height) {
    this.width = width;
    this.height = height;
  }

  @Override
  public void setWidth(int width) {
    this.width = width;
  }

  @Override
  public void setHeight(int height) {
    this.height = height;
  }

  @Override
  public void plot(Statistics[] stats, String[] seriesNames, String title, File outputFile) {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    for (int series=0; series<stats.length; series++) {
      for (int pct=1; pct<=100; pct++) {
        double pctValue = stats[series].getPercentile(pct); // # of stars
        if (series>0) {
          pctValue = pctValue - stats[series-1].getPercentile(pct); // deltas are stackables
        }
        dataset.addValue(pctValue, seriesNames[series], new Integer(pct));
      }
    }
    final JFreeChart barChart = ChartFactory.createStackedBarChart(
            title, "%", "# stars", dataset);
    try {
      ChartUtilities.saveChartAsJPEG(outputFile, barChart, width, height);
    } catch (IOException ex) {
//      LOG.log(Level.SEVERE, "Plotting chart to file", ex);
      throw new RuntimeException("Plotting with JFreeChart chart to file", ex);
    }
  }
  
}
