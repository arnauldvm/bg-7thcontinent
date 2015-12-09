/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.pendragon.j2s.seventhcontinent.plot;

import be.pendragon.j2s.seventhcontinent.stats.Statistics;

import java.io.File;

/**
 *
 * @author avm
 */
public interface Plotter {
  
  public void plot(Statistics[] stats, String[] seriesNames, String title, File outputFile);

  public void setHeight(int height);

  public void setWidth(int width);
  
}
