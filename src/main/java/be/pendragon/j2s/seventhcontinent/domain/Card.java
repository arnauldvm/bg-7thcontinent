/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.pendragon.j2s.seventhcontinent.domain;

/**
 *
 * @author avm
 */
public class Card {
  public final int fullStarsCount;
  public final boolean hasHalfLeft;
  public final boolean hasHalfRight;

  public Card(int fullStarsCount, boolean hasHalfLeft, boolean hasHalfRight) {
    this.fullStarsCount = fullStarsCount;
    this.hasHalfLeft = hasHalfLeft;
    this.hasHalfRight = hasHalfRight;
  }
  
  /**
   * Parses a card description.
   * @param cardDescription "n", "nL", "nR", or "nLR" where n is a positive integer (L/R means with half left/right)
   */
  public Card(String cardDescription) {
    char[] chars = cardDescription.toCharArray();
    int leftCount = 0;
    int rightCount = 0;
    for (int endIdx=chars.length-1; endIdx>=0; endIdx--) {
      final char lastCh = chars[endIdx];
      if (lastCh=='L') leftCount++;
      else if (lastCh=='R') rightCount++;
      else break;
    }
    if ((leftCount>1)||(rightCount>1)) // NOTE: Might change in the future in case more than one half star by side is possible
      throw new IllegalArgumentException("Expected n[L/R/LR], received " + cardDescription);
    final String valueAsString = cardDescription.substring(0, chars.length-leftCount-rightCount);
    this.hasHalfLeft = leftCount>0;
    this.hasHalfRight = rightCount>0;
    this.fullStarsCount = Integer.parseInt(valueAsString);
  }

  @Override
  public String toString() {
    return String.format("%d%s%s",
            fullStarsCount,
            hasHalfLeft?"L":"",
            hasHalfRight?"R":""
            );
//    return String.format("%2$s%1$s%3$s",
//            StringUtils.repeat('*', fullStarsCount),
//            hasHalfLeft?"<":"",
//            hasHalfRight?">":""
//    );
  }

}
