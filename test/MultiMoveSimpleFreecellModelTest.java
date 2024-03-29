import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import cs3500.freecell.model.Card;
import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.PileType;
import cs3500.freecell.model.Suits;
import cs3500.freecell.model.multimove.MultiMoveSimpleFreecellModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * To test the functionality of the Multi Move Freecell game.
 * Checks that all combinations of moves are valid.
 */
public class MultiMoveSimpleFreecellModelTest {

  private List<Card> orderedDeck;
  private FreecellModel<Card> model;
  private List<Suits> suits = new ArrayList<>(Arrays.asList(Suits.HEART,
          Suits.SPADES, Suits.DIAMOND, Suits.CLUBS));

  /**
   * Makes an ordered deck of cards. Ordered means that the cascade piles will have ordered sets
   * when the game is started. Makes a game with an ordered set of cards.
   */
  private void init() {
    orderedDeck = new ArrayList<>(52);
    int suitIndex = 0;
    int initialRank = 13;
    while (orderedDeck.size() < 52) {
      orderedDeck.add(new Card(suits.get(suitIndex), initialRank));
      suitIndex += 1;
      if (suitIndex > 3) {
        suitIndex = 0;
        initialRank -= 1;
      }
    }
    model = new MultiMoveSimpleFreecellModel();
    model.startGame(orderedDeck, 4, 1, false);
  }

  /**
   * Makes a game with an unordered deck of cards.
   */
  private void initNotOrdered() {
    model = new MultiMoveSimpleFreecellModel();
    model.startGame(model.getDeck(), 4, 1, false);
  }

  /**
   * Makes a game with a valid build in a cascade pile (index).
   */
  private void initBuild(Function<Void, Void> build) {
    init();
    model.startGame(orderedDeck, 4, 5, false);
    build.apply(null);
  }

  /**
   * Mutates a game with to form a valid build in a cascade pile (index).
   */
  private Void makeBuild(Void v) {
    // makes card available for a build
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 3 - i; j++) {
        model.move(PileType.CASCADE, j, model.getNumCardsInCascadePile(j) - 1,
                PileType.FOUNDATION, j);
      }
    }
    // makes a build
    for (int k = 1; k < 4; k++) {
      model.move(PileType.CASCADE, k, model.getNumCardsInCascadePile(k) - 1,
              PileType.CASCADE, 0);
    }
    return v;
  }

  /**
   * Mutates a game to form a build and one empty cascade pile.
   */
  private Void makeBuildEmptyPile(Void v) {
    // make and empty cascade pile
    for (int i = 12; i >= 0; i--) {
      model.move(PileType.CASCADE, 3, i, PileType.FOUNDATION, 3);
    }
    // make cards available to make a build
    for (int j = 0; j < 3; j++) {
      for (int k = 0; k <= 2 - j; k++) {
        model.move(PileType.CASCADE, k, model.getNumCardsInCascadePile(k) - 1,
                PileType.FOUNDATION, k);
      }
    }
    // making a 4 card build
    for (int h = 1; h < 3; h++) {
      model.move(PileType.CASCADE, h, model.getNumCardsInCascadePile(h) - 1,
              PileType.CASCADE, 0);
    }
    model.move(PileType.CASCADE, 1,
            model.getNumCardsInCascadePile(1) - 1, PileType.OPEN, 0);
    model.move(PileType.CASCADE, 0, 9, PileType.CASCADE, 1);
    model.move(PileType.CASCADE, 0,
            model.getNumCardsInCascadePile(0) - 1, PileType.OPEN, 1);
    return v;
  }

  // No move while inactive game or move out of the foundation.

  @Test(expected = IllegalStateException.class)
  public void disallowMoveWhileGameInActive() {
    model = new MultiMoveSimpleFreecellModel();
    model.move(PileType.CASCADE, 0, 12, PileType.FOUNDATION, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidMoveFromFoundationCascade() {
    init();
    model.move(PileType.FOUNDATION, 0, 0, PileType.CASCADE, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidMoveFromFoundationOpen() {
    init();
    model.move(PileType.FOUNDATION, 0, 0, PileType.OPEN, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidMoveFromFoundationFoundation() {
    init();
    model.move(PileType.FOUNDATION, 0, 0, PileType.FOUNDATION, 0);
  }

  // Invalid move Cascade into Foundation

  @Test(expected = IllegalArgumentException.class)
  public void invalidMoveSingleCardCascadeToEmptyFoundation() {
    initNotOrdered();
    model.move(PileType.CASCADE, 0, 12, PileType.FOUNDATION, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidMoveMultiCascadeToEmptyFoundation() {
    init();
    model.move(PileType.CASCADE, 0, 11, PileType.FOUNDATION, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidMoveSingleCardCascadeToFoundation() {
    init();
    model.move(PileType.CASCADE, 0, 12, PileType.FOUNDATION, 0);
    model.move(PileType.CASCADE, 1, 12, PileType.FOUNDATION, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidMoveMultiCardCascadeToFoundation() {
    init();
    model.move(PileType.CASCADE, 0, 12, PileType.FOUNDATION, 0);
    model.move(PileType.CASCADE, 0, 10, PileType.FOUNDATION, 0);
  }

  // Invalid move Open into Foundation

  @Test(expected = IllegalArgumentException.class)
  public void invalidMoveOpenToEmptyFoundation() {
    initNotOrdered();
    model.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);
    model.move(PileType.OPEN, 0, 0, PileType.FOUNDATION, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidMoveOpenToFoundation() {
    initNotOrdered();
    model.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);
    model.move(PileType.OPEN, 0, 0, PileType.FOUNDATION, 0);
  }

  // Invalid move Cascade into Open

  @Test(expected = IllegalArgumentException.class)
  public void invalidMoveSingleCardCascadeIntoNonEmptyOpen() {
    initNotOrdered();
    model.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);
    model.move(PileType.CASCADE, 0, 11, PileType.OPEN, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidMoveMultiCardCascadeIntoNonEmptyOpen() {
    initNotOrdered();
    model.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);
    model.move(PileType.CASCADE, 0, 10, PileType.OPEN, 0);
  }

  // Invalid move Open into Open

  @Test(expected = IllegalArgumentException.class)
  public void invalidMoveOpenIntoNonEmptyOpen() {
    model = new MultiMoveSimpleFreecellModel();
    model.startGame(model.getDeck(), 4, 3, false);
    model.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);
    model.move(PileType.CASCADE, 0, 11, PileType.OPEN, 1);
    model.move(PileType.OPEN, 0, 0, PileType.OPEN, 1);
  }

  // Invalid move Open into Cascade

  @Test(expected = IllegalArgumentException.class)
  public void invalidMoveOpenIntoCascade() {
    initNotOrdered();
    model.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);
    model.move(PileType.OPEN, 0, 0, PileType.CASCADE, 0);
  }

  // Invalid move Cascade into Cascade

  @Test(expected = IllegalArgumentException.class)
  public void invalidMoveSingleCardCascadeIntoCascade() {
    initNotOrdered();
    model.move(PileType.CASCADE, 0, 12, PileType.CASCADE, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidMoveMultiCardCascadeInvalidBuildIntoCascade() {
    model = new MultiMoveSimpleFreecellModel();
    model.startGame(model.getDeck(), 4, 5, false);
    model.move(PileType.CASCADE, 0, 9, PileType.CASCADE, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidMoveMultiCardCascadeValidBuildIntoCascade() {
    initBuild(this::makeBuild);
    model.move(PileType.CASCADE, 0, 9, PileType.CASCADE, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidMoveMultiCardCascadeIntoCascadeNoEnoughEmptySpace() {
    init();
    model.startGame(orderedDeck, 4, 1, false);
    makeBuild(null);
    model.move(PileType.CASCADE, 1, 9, PileType.OPEN, 0);
    model.move(PileType.CASCADE, 0, 9, PileType.CASCADE, 1);
  }

  // Moving card Cascade into Open

  @Test
  public void moveCascadeIntoOpen() {
    initNotOrdered();
    assertEquals(0, model.getNumCardsInOpenPile(0));
    assertEquals(13, model.getNumCardsInCascadePile(0));
    model.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);
    assertEquals(1, model.getNumCardsInOpenPile(0));
    assertEquals(12, model.getNumCardsInCascadePile(0));
  }

  // Moving card Cascade into Foundation

  @Test
  public void moveCascadeIntoEmptyFoundation() {
    init();
    assertEquals(0, model.getNumCardsInFoundationPile(0));
    assertEquals(13, model.getNumCardsInCascadePile(0));
    model.move(PileType.CASCADE, 0, 12, PileType.FOUNDATION, 0);
    assertEquals(1, model.getNumCardsInFoundationPile(0));
    assertEquals(12, model.getNumCardsInCascadePile(0));
  }

  @Test
  public void moveCascadeIntoNonEmptyFoundation() {
    init();
    model.move(PileType.CASCADE, 0, 12, PileType.FOUNDATION, 0);
    assertEquals(1, model.getNumCardsInFoundationPile(0));
    assertEquals(12, model.getNumCardsInCascadePile(0));
    model.move(PileType.CASCADE, 0, 11, PileType.FOUNDATION, 0);
    assertEquals(2, model.getNumCardsInFoundationPile(0));
    assertEquals(11, model.getNumCardsInCascadePile(0));
  }

  // Moving card Cascade in Cascade

  @Test
  public void moveMultiCardCascadeIntoCascade() {
    initBuild(this::makeBuild);
    model.move(PileType.CASCADE, 1, 9, PileType.OPEN, 0);
    assertEquals(9, model.getNumCardsInCascadePile(1));
    assertEquals(13, model.getNumCardsInCascadePile(0));
    model.move(PileType.CASCADE, 0, 9, PileType.CASCADE, 1);
    assertEquals(13, model.getNumCardsInCascadePile(1));
    assertEquals(9, model.getNumCardsInCascadePile(0));
  }

  @Test
  public void moveSingleCardCascadeIntoCascade() {
    init();
    model.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);
    assertEquals(12, model.getNumCardsInCascadePile(0));
    assertEquals(13, model.getNumCardsInCascadePile(1));
    model.move(PileType.CASCADE, 1, 12, PileType.CASCADE, 0);
    assertEquals(13, model.getNumCardsInCascadePile(0));
    assertEquals(12, model.getNumCardsInCascadePile(1));
  }

  @Test
  public void moveMultiCardCascadeIntoCascadeWithEmptyCascade() {
    init();
    model.startGame(orderedDeck, 4, 3, false);
    makeBuildEmptyPile(null);
    assertEquals(8, model.getNumCardsInCascadePile(0));
    assertEquals(12, model.getNumCardsInCascadePile(1));
    model.move(PileType.CASCADE, 1, 8, PileType.CASCADE, 0);
    assertEquals(12, model.getNumCardsInCascadePile(0));
    assertEquals(8, model.getNumCardsInCascadePile(1));
  }

  @Test
  public void moveMultiCardCascadeToEmptyCascade() {
    init();
    model.startGame(orderedDeck, 4, 3, false);
    makeBuildEmptyPile(null);
    assertEquals(0, model.getNumCardsInCascadePile(3));
    assertEquals(12, model.getNumCardsInCascadePile(1));
    model.move(PileType.CASCADE, 1, 8, PileType.CASCADE, 3);
    assertEquals(4, model.getNumCardsInCascadePile(3));
    assertEquals(8, model.getNumCardsInCascadePile(1));
  }

  // move Open into Foundation

  @Test
  public void moveOpenIntoEmptyFoundation() {
    init();
    model.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);
    assertEquals(1, model.getNumCardsInOpenPile(0));
    assertEquals(0, model.getNumCardsInFoundationPile(0));
    model.move(PileType.OPEN, 0, 0, PileType.FOUNDATION, 0);
    assertEquals(0, model.getNumCardsInOpenPile(0));
    assertEquals(1, model.getNumCardsInFoundationPile(0));
  }

  @Test
  public void moveOpenIntoNonEmptyFoundation() {
    init();
    model.move(PileType.CASCADE, 0, 12, PileType.FOUNDATION, 0);
    model.move(PileType.CASCADE, 0, 11, PileType.OPEN, 0);
    assertEquals(1, model.getNumCardsInOpenPile(0));
    assertEquals(1, model.getNumCardsInFoundationPile(0));
    model.move(PileType.OPEN, 0, 0, PileType.FOUNDATION, 0);
    assertEquals(0, model.getNumCardsInOpenPile(0));
    assertEquals(2, model.getNumCardsInFoundationPile(0));
  }

  @Test
  public void restartGame() {
    init();
    assertEquals(0, this.model.getNumCardsInFoundationPile(0));
    assertEquals(0, this.model.getNumCardsInFoundationPile(1));
    assertEquals(0, this.model.getNumCardsInFoundationPile(2));
    assertEquals(0, this.model.getNumCardsInFoundationPile(3));
    assertEquals(13, this.model.getNumCardsInCascadePile(0));
    assertEquals(13, this.model.getNumCardsInCascadePile(1));
    assertEquals(13, this.model.getNumCardsInCascadePile(2));
    assertEquals(13, this.model.getNumCardsInCascadePile(3));
    assertEquals(0, this.model.getNumCardsInOpenPile(0));
    this.model.move(PileType.CASCADE, 0, 12, PileType.FOUNDATION, 0);
    this.model.move(PileType.CASCADE, 1, 12, PileType.FOUNDATION, 1);
    this.model.move(PileType.CASCADE, 2, 12, PileType.FOUNDATION, 2);
    this.model.move(PileType.CASCADE, 3, 12, PileType.FOUNDATION, 3);
    this.model.move(PileType.CASCADE, 3, 11, PileType.OPEN, 0);
    assertEquals(1, this.model.getNumCardsInFoundationPile(0));
    assertEquals(1, this.model.getNumCardsInFoundationPile(1));
    assertEquals(1, this.model.getNumCardsInFoundationPile(2));
    assertEquals(1, this.model.getNumCardsInFoundationPile(3));
    assertEquals(12, this.model.getNumCardsInCascadePile(0));
    assertEquals(12, this.model.getNumCardsInCascadePile(1));
    assertEquals(12, this.model.getNumCardsInCascadePile(2));
    assertEquals(11, this.model.getNumCardsInCascadePile(3));
    assertEquals(1, this.model.getNumCardsInOpenPile(0));
    this.model.startGame(this.model.getDeck(),4,1, false);
    assertEquals(0, this.model.getNumCardsInFoundationPile(0));
    assertEquals(0, this.model.getNumCardsInFoundationPile(1));
    assertEquals(0, this.model.getNumCardsInFoundationPile(2));
    assertEquals(0, this.model.getNumCardsInFoundationPile(3));
    assertEquals(13, this.model.getNumCardsInCascadePile(0));
    assertEquals(13, this.model.getNumCardsInCascadePile(1));
    assertEquals(13, this.model.getNumCardsInCascadePile(2));
    assertEquals(13, this.model.getNumCardsInCascadePile(3));
    assertEquals(0, this.model.getNumCardsInOpenPile(0));
  }

  @Test(expected = IllegalStateException.class)
  public void getNumCardsAtFoundationWhileInActiveGame() {
    model = new MultiMoveSimpleFreecellModel();
    this.model.getNumCardsInFoundationPile(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getNumCardsAtFoundationInvalidIdxLowerLimit() {
    init();
    this.model.getNumCardsInFoundationPile(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getNumCardsAtFoundationInvalidIdxUpperLimit() {
    init();
    this.model.getNumCardsInFoundationPile(7);
  }

  @Test
  public void getNumCardsInFoundationPileTest() {
    init();
    this.model.move(PileType.CASCADE, 3, 12, PileType.FOUNDATION, 0);
    assertEquals(1, this.model.getNumCardsInFoundationPile(0));
  }

  @Test
  public void getNumCascadePilesWhileInActiveGame() {
    model = new MultiMoveSimpleFreecellModel();
    assertEquals(-1, this.model.getNumCascadePiles());
    init();
    assertEquals(4, this.model.getNumCascadePiles());
  }

  @Test(expected = IllegalArgumentException.class)
  public void getNumCardsAtCascadeInvalidIdxLowerLimit() {
    init();
    this.model.getNumCardsInCascadePile(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getNumCardsAtCascadeInvalidIdxUpperLimit() {
    init();
    this.model.getNumCardsInCascadePile(5);
  }

  @Test(expected = IllegalStateException.class)
  public void getNumCardsAtCascadePileWhileInactiveGame() {
    model = new MultiMoveSimpleFreecellModel();
    this.model.getNumCardsInCascadePile(0);
  }

  @Test
  public void getNumCardsAtCascadePileTest() {
    init();
    assertEquals(13, this.model.getNumCardsInCascadePile(2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void getNumCardAtOpenPileInvalidIndex() {
    init();
    this.model.getNumCardsInOpenPile(1);
  }

  @Test(expected = IllegalStateException.class)
  public void getNumCardsAtOpenPileWhileInactiveGame() {
    model = new MultiMoveSimpleFreecellModel();
    this.model.getNumCardsInOpenPile(0);
  }

  @Test
  public void getNumCardsAtOpenPileTest() {
    init();
    assertEquals(0, this.model.getNumCardsInOpenPile(0));
  }

  @Test
  public void getNumOpenPilesTest() {
    model = new MultiMoveSimpleFreecellModel();
    assertEquals(-1, this.model.getNumOpenPiles());
    init();
    assertEquals(1, this.model.getNumOpenPiles());
  }

  @Test(expected = IllegalStateException.class)
  public void getFoundationCardAtInActiveGame() {
    model = new MultiMoveSimpleFreecellModel();
    this.model.getFoundationCardAt(0, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getFoundationCardOutOfRange() {
    init();
    this.model.getFoundationCardAt(5, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getFoundationCardOutOfRangeCard() {
    init();
    this.model.getFoundationCardAt(0, 1);
  }

  @Test
  public void getFoundationCardTest() {
    init();
    this.model.move(PileType.CASCADE, 0, 12,
            PileType.FOUNDATION, 0);
    assertEquals("A♥", this.model.getFoundationCardAt(0, 0).toString());
  }

  @Test(expected = IllegalStateException.class)
  public void getCascadeCardAtInActiveGame() {
    model = new MultiMoveSimpleFreecellModel();
    this.model.getCascadeCardAt(0, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getCascadeCardOutOfRange() {
    init();
    this.model.getCascadeCardAt(-1, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getCascadeCardOutOfRangeCard() {
    init();
    this.model.getCascadeCardAt(0, 27);
  }

  @Test
  public void getCascadeCardTest() {
    init();
    assertEquals("K♥", this.model.getCascadeCardAt(0, 0).toString());
  }

  @Test(expected = IllegalStateException.class)
  public void getOpenCardInActiveGame() {
    model = new MultiMoveSimpleFreecellModel();
    this.model.getOpenCardAt(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getOpenCardPileOutOfRange() {
    init();
    this.model.getOpenCardAt(2);
  }

  @Test
  public void getOpenCardTest() {
    init();
    assertEquals(null, this.model.getOpenCardAt(0));
    this.model.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);
    assertEquals("A♥", this.model.getOpenCardAt(0).toString());
  }

  @Test
  public void gameOverTest() {
    model = new MultiMoveSimpleFreecellModel();
    assertEquals(false, this.model.isGameOver());
    this.init();
    assertEquals(false, this.model.isGameOver());
    for (int i = 0; i < 4; i++) {
      for (int j = 12; j >= 0; j -= 1) {
        this.model.move(PileType.CASCADE, i, j, PileType.FOUNDATION, i);
      }
    }
    assertEquals(true, this.model.isGameOver());
  }

  @Test
  public void getDeckTest() {
    init();
    assertEquals(52, model.getDeck().size());
  }

  @Test
  public void shuffleDeckTest() {
    init();
    List<Card> cards = new ArrayList<>();
    cards.addAll(orderedDeck);
    model.startGame(orderedDeck, 4, 1, true);
    assertNotEquals(cards, orderedDeck);
  }

  @Test(expected = IllegalArgumentException.class)
  public void disallowInvalidOpenPileNumbers() {
    init();
    this.model.startGame(orderedDeck, 5, 0, false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void disallowInvalidCascadePileNumbers() {
    init();
    this.model.startGame(orderedDeck, 3, 2, false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void disallowRepeatsInDeck() {
    init();
    Card dupe = orderedDeck.get(1);
    orderedDeck.remove(0);
    orderedDeck.add(dupe);
    this.model.startGame(orderedDeck, 4, 1, false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void disallowIncorrectSizeDeck() {
    init();
    orderedDeck.remove(0);
    this.model.startGame(orderedDeck, 5, 2, false);
  }

  @Test
  public void checkNumOfCascadePilesChanged() {
    init();
    this.model.startGame(this.model.getDeck(), 4, 1, false);
    assertEquals(4, model.getNumCascadePiles());
    assertEquals(1, model.getNumOpenPiles());
    this.model.startGame(this.model.getDeck(), 5, 2, false);
    assertEquals(5, this.model.getNumCascadePiles());
    assertEquals(2, this.model.getNumOpenPiles());
  }
}