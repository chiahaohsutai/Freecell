package cs3500.freecell.model;

/**
 * Represents the suit and rank of a poker card.
 */
public enum Suits {
  HEART("♥"), DIAMOND("♦"), CLUBS("♣"), SPADES("♠");

  private final String suit;

  Suits(String suit) {
    this.suit = suit;
  }

  @Override
  public String toString() {
    return this.suit;
  }
}
