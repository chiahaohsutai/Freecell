import org.junit.Test;

import cs3500.freecell.model.Card;
import cs3500.freecell.model.Suits;

import static org.junit.Assert.assertEquals;

/**
 * To test the card class and its functionalities.
 */
public class CardTest {
  Card diamond_1 = new Card(Suits.DIAMOND, 1);
  Card diamond_10 = new Card(Suits.DIAMOND, 10);
  Card diamond_1_copy = new Card(Suits.DIAMOND, 1);
  Card clubs_J = new Card(Suits.CLUBS, 11);
  Card spades_Q = new Card(Suits.SPADES, 12);
  Card hearts_K = new Card(Suits.HEART, 13);
  Card hearts_Q = new Card(Suits.HEART, 12);
  Card hearts_4 = new Card(Suits.HEART, 4);
  Card rankGreater = new Card(Suits.SPADES, 14);
  Card rankLess = new Card(Suits.HEART, 0);

  @Test
  public void toStringTest() {
    assertEquals("A♦", this.diamond_1.toString());
    assertEquals("J♣", this.clubs_J.toString());
    assertEquals("Q♠", this.spades_Q.toString());
    assertEquals("K♥", this.hearts_K.toString());
    assertEquals("4♥", this.hearts_4.toString());
  }

  @Test
  public void equalsTest() {
    assertEquals(true, this.diamond_1.equals(this.diamond_1_copy));
    assertEquals(false, this.diamond_1.equals(this.clubs_J));
  }

  @Test(expected = IllegalArgumentException.class)
  public void disallowRankLess() {
    this.rankLess.validCard();
  }

  @Test(expected = IllegalArgumentException.class)
  public void disallowRankGreater() {
    this.rankGreater.validCard();
  }

  @Test
  public void correctOrderTest() {
    assertEquals(true, this.diamond_10.correctOrder(clubs_J));
    assertEquals(false, this.clubs_J.correctOrder(this.diamond_1));
    assertEquals(false, this.diamond_1.correctOrder(this.hearts_4));
    assertEquals(false, this.clubs_J.correctOrder(this.spades_Q));
    assertEquals(false, this.spades_Q.correctOrder(this.clubs_J));
  }

  @Test
  public void validFoundationAdditionTest() {
    assertEquals(true, this.hearts_K.validFoundationAddition(this.hearts_Q));
    assertEquals(false, this.hearts_Q.validFoundationAddition(this.hearts_K));
    assertEquals(false, this.hearts_4.validFoundationAddition(this.hearts_Q));
    assertEquals(false, this.spades_Q.validFoundationAddition(this.diamond_1));
  }

  @Test
  public void initialValueTest() {
    assertEquals(true, this.diamond_1.initialValue());
    assertEquals(false, this.hearts_K.initialValue());
  }

  @Test(expected = IllegalArgumentException.class)
  public void disallowInvalidSuit() {
    Card invalidCard = new Card(null, 12);
  }

  @Test
  public void testGetRank() {
    assertEquals(1, this.diamond_1.getRank());
    assertEquals(12, this.spades_Q.getRank());
  }

  @Test
  public void testGetSuit() {
    assertEquals(Suits.DIAMOND, this.diamond_1.getSuit());
    assertEquals(Suits.SPADES, this.spades_Q.getSuit());
  }

  @Test
  public void testCompare() {
    assertEquals(1, this.spades_Q.compareRank(this.diamond_1));
    assertEquals(-1, this.diamond_1.compareRank(this.clubs_J));
    assertEquals(0, this.spades_Q.compareRank(this.hearts_Q));
  }

  @Test
  public void testSameColor() {
    assertEquals(true, this.spades_Q.sameColor(this.clubs_J));
    assertEquals(true, this.diamond_1.sameColor(this.hearts_Q));
    assertEquals(false, this.diamond_10.sameColor(this.clubs_J));
  }
}