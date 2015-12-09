/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.pendragon.j2s.seventhcontinent.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


/**
 *
 * @author avm
 */
public class Deck implements Iterable<Card> {
  
  private List<Card> cards;

  public Deck(List<Card> cards) {
    this.cards = Collections.unmodifiableList(cards);
  }
  
  public Deck(Card[] cards) {
    this(Arrays.asList(cards));
  }
  
  public static Deck parse(String[] cardsDescriptions) {
    List<Card> cards = new ArrayList<>(cardsDescriptions.length);
    for (String cardDescription: cardsDescriptions)
      cards.add(new Card(cardDescription));
    return new Deck(cards);
  }
  
  public static Deck parse(String joinedCardsDescription) {
    return parse(joinedCardsDescription.split(" "));
  }
  
  public static final Deck PNP = parse("0L 0R 0LR 1 2 1R 1L 1L 1R 0R");
  
  /**
   * Builds a random deck.
   * @param size The size of the deck
   * @param weights Proportion of cards with 0, 1, 2, 3 ... cards
   * @param halfRatio Ratio (between 0 and 1) of cards with half stars (equal repartition between left and right)
   * @param seed Seed for the random generators
   */
  public static Deck buildRandom(int size, int[] weights, float halfRatio, long seed) {
    Random seedRandomizer = new Random(seed);
    Random valueRandomizer = new Random(seedRandomizer.nextLong());
    Random halfRandomizer = new Random(seedRandomizer.nextLong());
    Random leftRightRandomizer = new Random(seedRandomizer.nextLong());
    Card[] cards = new Card[size];
    int totalWeight = 0;
    int[] valueSelectorBounds = new int[weights.length];
    for (int idx=0; idx<weights.length; idx++) {
      if (weights[idx]<0) throw new IllegalArgumentException("weights must be >=0");
      totalWeight += weights[idx];
      valueSelectorBounds[idx] = totalWeight;
    }
    for (int idx = 0; idx<size; idx++) {
      int valueSelector = valueRandomizer.nextInt(totalWeight);
      int searchResult = Arrays.binarySearch(valueSelectorBounds, valueSelector);
      int value = (searchResult>=0) ? (searchResult+1) : (-searchResult-1);
      boolean hasHalf = halfRandomizer.nextFloat()<halfRatio;
      boolean leftHalf = true;
      if (hasHalf) leftHalf = leftRightRandomizer.nextBoolean();
      cards[idx] = new Card(value, hasHalf&&leftHalf, hasHalf&&!leftHalf);
    }
    return new Deck(cards);
  }
  
  public Deck shuffle() {
    List<Card> cardsCopy = new ArrayList<>(this.cards);
    Collections.shuffle(cardsCopy);
    return new Deck(cardsCopy);
  }
  
  public Deck randomChoice(int n) {
    if (n>cards.size()) throw new IllegalArgumentException(String.format("n cards (%d) must be <= deck size (%d)", n, cards.size()));
    return(new Deck(shuffle().cards.subList(0, n))); // TODO : This is not efficient!
  }
  
  public int value() {
    int sumValue = cards.stream().mapToInt(c -> c.fullStarsCount).sum();
    int halfLeftStarsCount = (int) cards.stream().filter(c -> c.hasHalfLeft).count();
    int halfRightStarsCount = (int) cards.stream().filter(c -> c.hasHalfRight).count();
    return sumValue + Integer.min(halfLeftStarsCount, halfRightStarsCount);
  }
  
  @Override
  public Iterator<Card> iterator() {
    return cards.iterator();
  }
  
  @Override
  public String toString() {
    return value() + " = " + cards.stream().map(Card::toString).collect(Collectors.joining(" "));
  }

  public static void main(String[] args) {
    Deck refDeck = Deck.buildRandom(32, new int[] { 1, 1, 1, 1 }, 0.5F, 0L);
    System.out.println(refDeck);
    Deck shufDeck = refDeck.shuffle();
    System.out.println(shufDeck);
    
    System.out.println(refDeck.randomChoice(3));
    System.out.println(refDeck.randomChoice(3));
    System.out.println(refDeck.randomChoice(3));

    System.out.println(Deck.PNP);
  }
  
}
