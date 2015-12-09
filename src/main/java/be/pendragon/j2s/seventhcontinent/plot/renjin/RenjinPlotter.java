/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.pendragon.j2s.seventhcontinent.plot.renjin;

import be.pendragon.j2s.seventhcontinent.plot.Plotter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.renjin.script.RenjinScriptEngine;
import org.renjin.script.RenjinScriptEngineFactory;
import org.renjin.sexp.AttributeMap;
import org.renjin.sexp.DoubleArrayVector;
import org.renjin.sexp.IntArrayVector;
import org.renjin.sexp.ListVector;
import org.renjin.sexp.SEXP;
import org.renjin.sexp.StringVector;

import be.pendragon.j2s.seventhcontinent.stats.Statistics;
import javax.script.ScriptException;

/**
 *
 * @author avm
 */
public class RenjinPlotter implements Plotter {

  private int width;
  private int height;
  private final RenjinScriptEngine engine;

  public RenjinPlotter() {
    this(640, 480);
  }

  public RenjinPlotter(int width, int height) {
    this.width = width;
    this.height = height;
    this.engine = new RenjinScriptEngineFactory().getScriptEngine();
  }

  @Override
  public void setWidth(int width) {
    this.width = width;
  }

  @Override
  public void setHeight(int height) {
    this.height = height;
  }
  
  private SEXP toSexp(int anInt) {
    return new IntArrayVector(anInt);
  }

  private SEXP toSexp(long aLong) {
    return new DoubleArrayVector((double) aLong);
  }

  private SEXP toSexp(double... doubles) {
    return new DoubleArrayVector(doubles);
  }

  private SEXP toSexp(Statistics stats) {
    List<SEXP> asList = new ArrayList<>();
    List<String> names = new ArrayList<>();
    asList.add(toSexp(stats.average)); names.add("average");
    asList.add(toSexp(stats.min)); names.add("min");
    asList.add(toSexp(stats.max)); names.add("max");
    asList.add(toSexp(stats.count)); names.add("count");
    asList.add(toSexp(stats.getPercentiles())); names.add("percentiles");
    final StringVector.Builder attributesBuilder = new StringVector.Builder();
    attributesBuilder.addAll(names);
    return new ListVector(
      asList,
      new AttributeMap.Builder().setNames(attributesBuilder.build()).build()
    );
  }

  @Override
  public void plot(Statistics[] stats, String[] seriesNames, String title, File outputFile) {
    engine.put("width", width);
    engine.put("height", height);
    SEXP[] rStats = new SEXP[stats.length];
    for (int idx = 0; idx<stats.length; idx++) {
      rStats[idx] = toSexp(stats[idx]);
    }
    engine.put("stats", new ListVector(rStats));
    engine.put("seriesNames", seriesNames);
    engine.put("title", title);
    engine.put("outputFile", outputFile.toString());
    try {
      try (final InputStream scriptInputStream = RenjinPlotter.class.getResourceAsStream("plot.r")) {
        engine.eval(new InputStreamReader(scriptInputStream));
      } catch (ScriptException ex) {
        throw new RuntimeException("Plotting with Renjin chart to file", ex);
      }
    } catch (IOException ex) {
      throw new RuntimeException("Plotting with Renjin chart to file", ex);
    }
  }

}
