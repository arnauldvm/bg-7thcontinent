/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.pendragon.j2s.seventhcontinent.stats;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.reflections.Reflections;

import be.pendragon.j2s.seventhcontinent.domain.Deck;
import be.pendragon.j2s.seventhcontinent.plot.Plotter;

/**
 *
 * @author avm
 */
public class StatisticsGenerator {
  private final Deck referenceDeck;

  public StatisticsGenerator(Deck referenceDeck) {
    this.referenceDeck = referenceDeck;
  }
  
  public Statistics getStats(int drawSize, int nDraw) {
    Statistics.Accumulator accumulator = new Statistics.Accumulator();
    for (int i = 0; i<nDraw; i++) {
      int value = referenceDeck.randomChoice(drawSize).value();
      accumulator.accept(value);
    }
    return accumulator.compute();
  }
  
  public static void main(String[] args) throws IOException {
    String title = "Random reference deck (seed 0)";
    System.out.println(title);
    Deck refDeck = Deck.buildRandom(32, new int[] { 1, 1, 1, 1 }, 0.5F, 0L);
    StatisticsGenerator generator = new StatisticsGenerator(refDeck);

    System.out.println("Repeat:");
    System.out.println("3: " + generator.getStats(6, 10000));
    System.out.println("3: " + generator.getStats(6, 10000));
    System.out.println("3: " + generator.getStats(6, 10000));

    Statistics[] stats = new Statistics[6];
    String[] seriesNames = new String[stats.length];
    for (int series=0; series<stats.length; series++) {
      stats[series] = generator.getStats(series, 10000);
      seriesNames[series] = Integer.toString(series);
      System.out.println(seriesNames[series] + ": " + stats[series]);
    }
    Reflections ref = new Reflections(Plotter.class.getPackage().getName());
    final Set<Class<? extends Plotter>> plotterTypes = ref.getSubTypesOf(Plotter.class);
    for (Class<? extends Plotter> plotterType: plotterTypes) {
      Plotter plotter;
      try {
        plotter = plotterType.newInstance();
      } catch (InstantiationException|IllegalAccessException e) {
        e.printStackTrace();
        continue;
      }
      plotter.plot(stats, seriesNames, title, new File("target/reference.jpg"));
    }

    title = "PNP deck";
    System.out.println(title);
    generator = new StatisticsGenerator(Deck.PNP);
    for (int series=0; series<stats.length; series++) {
      stats[series] = generator.getStats(series, 10000);
      seriesNames[series] = Integer.toString(series);
      System.out.println(seriesNames[series] + ": " + stats[series]);
    }
    for (Class<? extends Plotter> plotterType: plotterTypes) {
      Plotter plotter;
      try {
        plotter = plotterType.newInstance();
      } catch (InstantiationException|IllegalAccessException e) {
        e.printStackTrace();
        continue;
      }
      plotter.plot(stats, seriesNames, title, new File("target/pnp.jpg"));
    }
  }
  
}
