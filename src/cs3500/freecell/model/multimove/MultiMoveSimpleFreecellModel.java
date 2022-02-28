package cs3500.freecell.model.multimove;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;

import cs3500.freecell.model.Card;
import cs3500.freecell.model.PileType;
import cs3500.freecell.model.SimpleFreecellModel;

/**
 * To represent a game of freecell that accepts moving multiple cards at the same time.
 */
public class MultiMoveSimpleFreecellModel extends SimpleFreecellModel {

  /**
   * Instantiates an instance of a multi move simple freecell game.
   */
  public MultiMoveSimpleFreecellModel() {
    super();
  }

  @Override
  public void move(PileType source, int pileNumber, int cardIndex,
                   PileType destination, int destPileNumber) {

    // making a map with the functions that add to each different pileType
    HashMap<PileType, BiFunction<List<Card>, Integer, Void>> addTo = new HashMap<>(3);
    addTo.put(PileType.OPEN, this::move2open);
    addTo.put(PileType.FOUNDATION, this::move2foundation);
    addTo.put(PileType.CASCADE, this::move2cascade);

    this.checkValidMoveConditions(source);
    List<Card> cardsBeingMoved = getCard(source, pileNumber, cardIndex);

    if (cardsBeingMoved.size() > 1 && (destination == PileType.FOUNDATION
            || destination == PileType.OPEN)) {
      throw new IllegalArgumentException("Can't add a build into non-cascade pile !");
    }

    addTo.get(destination).apply(cardsBeingMoved, destPileNumber);

    removeBuild(cardIndex, pileNumber, source);
  }

  /**
   * Adds the given set of cards to an open pile.
   *
   * @param cards is the set/build of cards being added to the pile.
   * @param pileIdx index of the pile we are moving cards into.
   * @return null since we are mutating the model.
   */
  private Void move2open(List<Card> cards, Integer pileIdx) {
    if (cards.get(0) == null) {
      throw new IllegalArgumentException("Invalid move, no card at the given pile index");
    }
    this.add2OpenPile(cards.get(0), openPile.get(pileIdx));
    return null;
  }

  /**
   * Adds the given set of cards to an open pile.
   *
   * @param cards is the set/build of cards being added to the pile.
   * @param pileIdx index of the pile we are moving cards into.
   * @return null since we are mutating the model.
   */
  private Void move2foundation(List<Card> cards, Integer pileIdx) {
    this.add2FoundationPile(cards.get(0), foundationPile.get(pileIdx));
    return null;
  }

  // needs implementation
  private Void move2cascade(List<Card> cards, Integer pileIdx) {
    int numOpenEmpty = numEmptyPiles(openPile);
    int numCascadeEmpty = numEmptyPiles(cascadePile);
    double maxCardsForMove = (numOpenEmpty + 1) * Math.pow(2, numCascadeEmpty);
    int targetPileLastIndex = getNumCardsInCascadePile(pileIdx);

    if (targetPileLastIndex == 0 && cards.size() <= maxCardsForMove) {
      cascadePile.get(pileIdx).addAll(cards);
      return null;
    }

    Card topMostCard = cards.get(0);
    Card lastCardInTargetPile = getCascadeCardAt(pileIdx, targetPileLastIndex - 1);
    if (cards.size() > maxCardsForMove
            || !topMostCard.correctOrder(lastCardInTargetPile)) {
      throw new IllegalArgumentException("Invalid move. Invalid build.");
    }

    cascadePile.get(pileIdx).addAll(cards);
    return null;
  }

  /**
   * Checks if the cards are a valid build.
   *
   * @param cardIdx is the index of the topmost card in the pile (closer to index 0 is topmost)
   * @param pileIdx is the pile where the build is located at.
   */
  private void isValidBuild(int cardIdx, int pileIdx) {
    int cascadePileSize = this.getNumCardsInCascadePile(pileIdx);

    // skips the last card in the pile because we don't need to check that card.
    for (int i = cardIdx; i < cascadePileSize - 1; i++) {
      Card cardAbove = this.getCascadeCardAt(pileIdx, i);
      Card cardBelow = this.getCascadeCardAt(pileIdx, i + 1);
      if (cardAbove.compareRank(cardBelow) != 1 || cardAbove.sameColor(cardBelow)) {
        throw new IllegalArgumentException("Invalid build");
      }
    }
  }

  /**
   * Gets the card from the specified.
   *
   * @param source the pile type of the pile we are moving a card from.
   * @param pileNumber is the index of the pile within the group of piles.
   * @param cardIndex is the index of the rad inside the pile.
   * @return card at the specified index.
   */
  private List<Card> getCard(PileType source, int pileNumber, int cardIndex) {
    List<Card> cards = new ArrayList<>();
    if (source == PileType.OPEN) {
      cards.add(this.getOpenCardAt(pileNumber));
    } else {
      isValidBuild(cardIndex, pileNumber);
      int cascadePileSize = this.getNumCardsInCascadePile(pileNumber);
      for (int i = cardIndex; i < cascadePileSize; i++) {
        cards.add(this.getCascadeCardAt(pileNumber, i));
      }
    }
    return cards;
  }

  /**
   * Gets the number of empty open piles.
   */
  private int numEmptyPiles(List<List<Card>> pileGroup) {
    int count = 0;
    for (List<Card> pile : pileGroup) {
      if (pile.size() == 0) {
        count += 1;
      }
    }
    return count;
  }

  /**
   * Removes the cards from the source after a move is completed.
   */
  private void removeBuild(int cardIdx, int pileIdx, PileType source) {
    if (source == PileType.OPEN) {
      removeMovingCard(pileIdx, cardIdx, PileType.OPEN);
    }
    else {
      int sizeOfPile = cascadePile.get(pileIdx).size();
      for (int i = cardIdx; i < sizeOfPile; i++) {
        removeMovingCard(pileIdx, cardIdx, PileType.CASCADE);
      }
    }
  }
}
