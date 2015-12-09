/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.pendragon.j2s.seventhcontinent.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runners.model.MultipleFailureException;

import static org.junit.Assert.assertTrue;

/**
 *
 * @author avm
 */
public class DeckTest {
  
  public DeckTest() {
  }

  
  private Optional<AssertionError> testSelectValue(int[] valueSelectorBounds, int valueSelector, int expected) {
    int searchResult = Arrays.binarySearch(valueSelectorBounds, valueSelector);
    // exact <-> on boundary: searchResult>=0 and searchResult = prevUpBoundaryIndex => value = prevUpBoundaryIndex-1 = searchResult+1
    // not found <-> inside boundaries: searchResult<0 and searchResult = (-upBoundaryIndex -1) <=> value = upBoundaryIndex = -searchResult -1
    int value = (searchResult>=0) ? (searchResult+1) : (-searchResult-1);
    try {
      assertTrue("For selector " + valueSelector + ": expected " + expected + ", received " + value, value==expected);
    } catch (AssertionError e) {
      System.err.println(e);
      return Optional.of(e);
    }
    return Optional.empty();
  }
  /**
   * Test of buildRandom method, of class Deck.
   */
  @Test
  public void testSearchSelector() {
    System.out.println("searchSelector");
    int[] valueSelectorBounds = new int[] { 3, 5, 8 };
    List<Throwable> errors = new ArrayList<>();
    testSelectValue(valueSelectorBounds, 0, 0).ifPresent(e -> errors.add(e));
    testSelectValue(valueSelectorBounds, 1, 0).ifPresent(e -> errors.add(e));
    testSelectValue(valueSelectorBounds, 2, 0).ifPresent(e -> errors.add(e));
    testSelectValue(valueSelectorBounds, 3, 1).ifPresent(e -> errors.add(e));
    testSelectValue(valueSelectorBounds, 4, 1).ifPresent(e -> errors.add(e));
    testSelectValue(valueSelectorBounds, 5, 2).ifPresent(e -> errors.add(e));
    testSelectValue(valueSelectorBounds, 6, 2).ifPresent(e -> errors.add(e));
    testSelectValue(valueSelectorBounds, 7, 2).ifPresent(e -> errors.add(e));
    if (!errors.isEmpty())
      throw new AssertionError(new MultipleFailureException(errors));
  }
  
}
