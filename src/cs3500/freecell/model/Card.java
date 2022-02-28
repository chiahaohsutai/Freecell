package cs3500.freecell.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * To represent a card that can be used in  the game of freecell.
 */
public class Card {
  private final Suits suit;
  private final int rank;

  /**
   * Represents a standard poker card from a deck of 52 cards.
   *
   * @param suit is the suit of the card.
   * @param rank is the numeric value of the card.
   */
  public Card(Suits suit, int rank) {
    if (suit == null) {
      throw new IllegalArgumentException("Invalid suit");
    }
    this.suit = suit;
    this.rank = rank;
  }

  @Override
  public String toString() {
    if (this.rank == 1) {
      return "A" + this.suit;
    }
    else if (this.rank == 11) {
      return "J" + this.suit;
    }
    else if (this.rank == 12) {
      return "Q" + this.suit;
    }
    else if (this.rank == 13) {
      return "K" + this.suit;
    }
    else {
      return this.rank + this.suit.toString();
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Card)) {
      return false;
    }
    Card card = (Card)o;
    return this.suit == card.suit && this.rank == card.rank;
  }

  @Override
  public int hashCode() {
    return this.suit.toString().CASE_INSENSITIVE_ORDER.hashCode() + this.rank;
  }

  /**
   * Checks if the card is a valid standard poker card.
   *
   * @throws IllegalArgumentException if suit is invalid or rank is invalid.
   */
  public void validCard() {
    if (this.rank <= 0 || this.rank >= 14) {
      throw new IllegalArgumentException("Invalid rank");
    }
  }

  /**
   * Checks if {@this} card can go after the given card.
   *
   * @param pileCard is the card that comes before {@this} card.
   * @return true if {@this} card come after pileCard, otherwise false.
   */
  public boolean correctOrder(Card pileCard) {
    ArrayList<Suits> red_suits = new ArrayList<>(Arrays.asList(Suits.HEART, Suits.DIAMOND));
    boolean current_card_suit_red = red_suits.contains(this.suit);
    boolean input_card_suit_red = red_suits.contains(pileCard.suit);

    if ((current_card_suit_red && !input_card_suit_red)
            || (!current_card_suit_red && input_card_suit_red)) {
      return this.rank == pileCard.rank - 1;
    }
    else {
      return false;
    }
  }

  /**
   * Checks if {@this} card can be added to the foundation pile.
   *
   * @param foundationCard is the card that comes before {@this} card.
   * @return true if {@this} card goes after foundationCard.
   */
  public boolean validFoundationAddition(Card foundationCard) {
    return (this.suit == foundationCard.suit && (this.rank - 1) == foundationCard.rank);
  }

  /**
   * Checks if the card is an ace of any suit.
   *
   * @return true if the card is an ace.
   */
  public boolean initialValue() {
    return this.rank == 1;
  }

  /**
   * Gets the rank of the card.
   *
   * @return rank of {@this} card.
   */
  public int getRank() {
    return this.rank;
  }

  /**
   * Gets the suit of the card.
   *
   * @return suit of {@this} card.
   */
  public Suits getSuit() {
    return this.suit;
  }

  /**
   * Checks if this card has a lower rank higher rank or the same rank as another card.
   *
   * @param card card being compared to.
   * @return 1 if {@this} has a higer rank, -1 if lower, 0 if equal.
   */
  public int compareRank(Card card) {
    return Integer.compare(this.rank, card.rank);
  }

  /**
   * Checks if the cards have the same color suit.
   *
   * @param card the card we are comparing to.
   * @return true if the cards have the same color suit.
   */
  public boolean sameColor(Card card) {
    List<Suits> redSuits = new ArrayList<>(Arrays.asList(Suits.HEART, Suits.DIAMOND));
    if (redSuits.contains(this.suit) && redSuits.contains(card.suit)) {
      return true;
    }
    else {
      return !redSuits.contains(this.suit) && !redSuits.contains(card.suit);
    }
  }
}
