package cs3500.freecell.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * To represent a freecell game that uses {@Cards}.
 */
public class SimpleFreecellModel implements FreecellModel<Card> {
  protected boolean gameActive;
  protected List<List<Card>> cascadePile;
  protected List<List<Card>> openPile;
  protected List<List<Card>> foundationPile;

  /**
   * Initializes a game of Simple Freecell.
   *
   * @returns a instance of a SimpleFrecell game.
   */
  public SimpleFreecellModel() {
    this.gameActive = false;
  }

  @Override
  public List<Card> getDeck() {
    List<Suits> suits = new ArrayList<>(Arrays.asList(Suits.DIAMOND, Suits.CLUBS, Suits.SPADES,
            Suits.HEART));
    List<Card> deck = new ArrayList<>();
    for (int i = 1; i <= suits.size(); i++) {
      for (int j = 1; j <= 13; j++) {
        Card card = new Card(suits.get(i - 1), j);
        deck.add(card);
      }
    }
    return deck;
  }

  @Override
  public void startGame(List<Card> deck, int numCascadePiles, int numOpenPiles, boolean shuffle) {
    this.checkDeckSize(deck);
    this.checkValidPileNumbers(numCascadePiles, numOpenPiles);
    this.checkValidDeck(deck);

    this.gameActive = true;

    this.foundationPile = new ArrayList<>(4);
    addPiles(4, this.foundationPile);
    this.cascadePile = new ArrayList<>(numCascadePiles);
    this.openPile = new ArrayList<>(numOpenPiles);
    addPiles(numCascadePiles, this.cascadePile);
    addPiles(numOpenPiles, this.openPile);

    if (shuffle) {
      Collections.shuffle(deck);
    }

    dealCardsRobinHood(deck, numCascadePiles);
  }

  @Override
  public void move(PileType source, int pileNumber, int cardIndex,
                   PileType destination, int destPileNumber) {

    this.checkValidMoveConditions(source);

    Card card_being_moved = this.checkSourceCardIndex(pileNumber, cardIndex, source);

    switch (destination) {
      case CASCADE:
        add2CascadePile(card_being_moved, this.cascadePile.get(destPileNumber));
        break;
      case OPEN:
        add2OpenPile(card_being_moved, this.openPile.get(destPileNumber));
        break;
      default:
        add2FoundationPile(card_being_moved, this.foundationPile.get(destPileNumber));
        break;
    }

    this.removeMovingCard(pileNumber, cardIndex, source);
  }

  @Override
  public boolean isGameOver() {
    if (!gameActive) {
      return false;
    }
    int foundation_0 = this.foundationPile.get(0).size();
    int foundation_1 = this.foundationPile.get(1).size();
    int foundation_2 = this.foundationPile.get(2).size();
    int foundation_3 = this.foundationPile.get(3).size();
    return foundation_0 == 13 && foundation_1 == 13 && foundation_2 == 13 && foundation_3 == 13;
  }

  @Override
  public int getNumCardsInFoundationPile(int index) {
    this.checkGetCardNumberValid(index, this.foundationPile);
    return this.foundationPile.get(index).size();
  }

  @Override
  public int getNumCascadePiles() {
    return checkNumAtPile(this.cascadePile);
  }

  @Override
  public int getNumCardsInCascadePile(int index) {
    this.checkGetCardNumberValid(index, this.cascadePile);
    return this.cascadePile.get(index).size();
  }

  @Override
  public int getNumCardsInOpenPile(int index) {
    this.checkGetCardNumberValid(index, this.openPile);
    return this.openPile.get(index).size();
  }

  @Override
  public int getNumOpenPiles() {
    return checkNumAtPile(this.openPile);
  }

  @Override
  public Card getFoundationCardAt(int pileIndex, int cardIndex) {
    return getCardAtIndex(pileIndex, cardIndex, this.foundationPile);
  }

  @Override
  public Card getCascadeCardAt(int pileIndex, int cardIndex) {
    return getCardAtIndex(pileIndex, cardIndex, this.cascadePile);
  }

  @Override
  public Card getOpenCardAt(int pileIndex) {
    checkGetCardNumberValid(pileIndex, this.openPile);
    if (this.openPile.get(pileIndex).size() == 0) {
      return null;
    } else {
      return this.openPile.get(pileIndex).get(0);
    }
  }

  /**
   * Deals the cards in a robin hood style fashion.
   *
   * @param deck is a list of cards.
   */
  private void dealCardsRobinHood(List<Card> deck, int numCascadePile) {
    List<Card> deckCopy = new ArrayList<>();
    deckCopy.addAll(deck);
    int deck_size = deckCopy.size();
    int current_pile = 0;
    while (deck_size > 0) {
      this.cascadePile.get(current_pile).add(deckCopy.remove(0));
      deck_size -= 1;
      current_pile += 1;
      if (deck_size <= 0) {
        break;
      }
      if (current_pile >= numCascadePile) {
        current_pile = 0;
      }
    }
  }

  /**
   * Adds the number of piles to the list.
   *
   * @param numberPiles is the number of piles to be added to the list.
   * @param pile        is the group of piles (foundation, cascade, open)
   *                    to which the piles are added to.
   */
  private void addPiles(int numberPiles, List<List<Card>> pile) {
    for (int i = 0; i < numberPiles; i++) {
      pile.add(new ArrayList<>());
    }
  }

  /**
   * Checks if the pile numbers are correct. Minimum 4 cascade and 1 open piles.
   *
   * @param numCascadePiles is the number of cascade piles.
   * @param numOpenPiles    is the number of open piles.
   * @throws IllegalArgumentException if the number of cascade piles is less than 4.
   * @throws IllegalArgumentException if number of open piles is less than 1.
   */
  private void checkValidPileNumbers(int numCascadePiles, int numOpenPiles) {
    if (numCascadePiles < 4) {
      throw new IllegalArgumentException("Incorrect number of cascade piles");
    }
    if (numOpenPiles < 1) {
      throw new IllegalArgumentException("Incorrect number open of piles");
    }
  }

  /**
   * Checks if the deck is a valid deck (no repeats, all valid cards).
   *
   * @param deck is a list of cards.
   * @throws IllegalArgumentException if deck has a repeated card.
   * @throws IllegalArgumentException if deck has an invalid card.
   */
  private void checkValidDeck(List<Card> deck) {
    deck.get(0).validCard();
    for (int i = 0; i < deck.size() - 1; i++) {
      Card current_card = deck.get(i);
      current_card.validCard();
      for (int j = i + 1; j < deck.size(); j++) {
        Card compare = deck.get(j);
        compare.validCard();
        if (current_card.equals(compare)) {
          throw new IllegalArgumentException("There exists duplicates in this deck");
        }
      }
    }
  }

  /**
   * Checks if the deck is the correct size.
   *
   * @param deck is a list of cards.
   * @throws IllegalArgumentException if deck is not 52 cards.
   */
  private void checkDeckSize(List<Card> deck) {
    if (deck.size() != 52) {
      throw new IllegalArgumentException("Deck is not the correct size " +
              "(the deck should be 52 cards)");
    }
  }

  /**
   * Adds a card to the cascade pile following game restrictions.
   *
   * @param pile       is the cascade pile.
   * @param movingCard is the card being moved into the pile.
   * @throws IllegalArgumentException if card being added is not in the correct order.
   */
  private void add2CascadePile(Card movingCard, List<Card> pile) {
    Card destination_card = pile.get(pile.size() - 1);

    if (!(movingCard.correctOrder(destination_card))) {
      throw new IllegalArgumentException("Incorrect Order for cascade pile");
    }

    pile.add(movingCard);
  }

  /**
   * Adds a card to the open pile according to game rules.
   *
   * @param pile       is the open pile.
   * @param movingCard is the card being moved into the pile.
   * @throws IllegalArgumentException if the open pile already has a card in it.
   */
  protected void add2OpenPile(Card movingCard, List<Card> pile) {
    if (pile.size() == 1) {
      throw new IllegalArgumentException("Open piles can hold only one card");
    }
    pile.add(movingCard);
  }

  /**
   * Adds a card to the open pile according to game rules.
   *
   * @param pile        is the foundation pile.
   * @param movingCard is the card being moved into the pile.
   * @throws IllegalArgumentException if the card being added is in the incorrect order.
   * @throws IllegalArgumentException if the pile size is 0 and input is not an ace.
   */
  protected void add2FoundationPile(Card movingCard, List<Card> pile) {
    if (pile.size() > 0) {
      Card destination_card = pile.get(pile.size() - 1);
      if (!(movingCard.validFoundationAddition(destination_card))) {
        throw new IllegalArgumentException("Incorrect Order for foundation pile");
      }
      pile.add(movingCard);
    } else {
      if (pile.size() == 0 && movingCard.initialValue()) {
        pile.add(movingCard);
      } else {
        throw new IllegalArgumentException("Incorrect Order (Not an Ace)");
      }
    }
  }

  /**
   * Checks that the conditions to move a card are fulfilled.
   *
   * @param type is type of pile from which a card is being moved.
   * @throws IllegalArgumentException if pile type is open or foundation.
   * @throws IllegalStateException    if game not active.
   */
  protected void checkValidMoveConditions(PileType type) {
    if (!this.gameActive) {
      throw new IllegalStateException("The game has not started !");
    }
    if (type == PileType.FOUNDATION) {
      throw new IllegalArgumentException("Can't move cards out of this type of pile !");
    }
  }

  /**
   * Checks if the conditions for getting the size of a pile are valid.
   *
   * @param index is the index of the pile within the group of piles (foundation, open, cascade).
   * @throws IllegalArgumentException if index in out of range.
   * @throws IllegalStateException    if game is inactive.
   */
  private void checkGetCardNumberValid(int index, List<List<Card>> pile) {
    if (!this.gameActive) {
      throw new IllegalStateException("Game is not active");
    }
    if (index > (pile.size() - 1) || index < 0) {
      throw new IllegalArgumentException("Invalid index");
    }
  }

  /**
   * Gets the number of piles in the given group of piles (open or cascade).
   *
   * @param piles is a group of piles (open or cascade).
   * @return the number of piles in the group, as an integer, or -1 if the game has not started yet.
   */
  private int checkNumAtPile(List<List<Card>> piles) {
    if (!this.gameActive) {
      return -1;
    }
    return piles.size();
  }

  /**
   * Checks if the pile and number cards are valid inputs.
   *
   * @param pileIndex is the index of the pile.
   * @param cardIndex is the index of the card within the pile.
   * @param pileList  is a group of piles (open, cascade, foundation).
   * @throws IllegalArgumentException if the pileIndex or cardIndex is invalid.
   * @throws IllegalStateException    if the game has not started.
   */
  private Card getCardAtIndex(int pileIndex, int cardIndex, List<List<Card>> pileList) {
    checkGetCardNumberValid(pileIndex, pileList);
    List<Card> pile = pileList.get(pileIndex);
    if (cardIndex >= pile.size() || cardIndex < 0) {
      throw new IllegalArgumentException("Card index is invalid");
    }
    return pile.get(cardIndex);
  }

  /**
   * Gets the last card in the pile if index is correct, deletes the card form the pile as well.
   *
   * @param pileNumber is the index of the pile from where the card comes from.
   * @param cardIndex  is the index of the card within the pile.
   * @param type       is the pile type of the pile.
   * @return the card that is going to be moved.
   * @throws IllegalArgumentException is index is not the last card in the given pile.
   */
  private Card checkSourceCardIndex(int pileNumber, int cardIndex, PileType type) {
    if (type == PileType.CASCADE) {
      return getLastCardFromPile(cardIndex, this.cascadePile.get(pileNumber));
    }
    else {
      return getLastCardFromPile(cardIndex, this.openPile.get(pileNumber));
    }
  }

  /**
   * Gets the last card from the given pile.
   *
   * @param cardIndex is the index of the card being taken.
   * @param pile is the pile from where the card is being taken.
   * @return the card being taken.
   * @throws IllegalArgumentException if the card being taken is not the last one in the pile.
   */
  private Card getLastCardFromPile(int cardIndex, List<Card> pile) {
    if (cardIndex == pile.size() - 1) {
      return pile.get(cardIndex);
    }
    throw new IllegalArgumentException("Card being moved should be the last card in the pile");
  }

  /**
   * Removes the card from the given source pile.
   *
   * @param pileNumber is the index of the pile in the group of piles.
   * @param cardIndex is the index of the crad in the pile.
   * @param type is the Pile type of the group of piles.
   */
  protected void removeMovingCard(int pileNumber, int cardIndex, PileType type) {
    if (type == PileType.CASCADE) {
      List<Card> cPile = this.cascadePile.get(pileNumber);
      cPile.remove(cardIndex);
    }
    else {
      List<Card> oPile = this.openPile.get(pileNumber);
      oPile.remove(cardIndex);
    }
  }
}
