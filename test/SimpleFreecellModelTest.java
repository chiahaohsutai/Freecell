import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cs3500.freecell.model.Card;
import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.PileType;
import cs3500.freecell.model.SimpleFreecellModel;
import cs3500.freecell.model.Suits;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * To test the Simple Freecell Model and its functionalities.
 */
public class SimpleFreecellModelTest {

  FreecellModel<Card> model_1 = new SimpleFreecellModel();
  FreecellModel<Card> model_2;
  FreecellModel<Card> model_3;

  // All 52 cards of the deck:
  Card d_1 = new Card(Suits.DIAMOND, 1);
  Card d_2 = new Card(Suits.DIAMOND, 2);
  Card d_3 = new Card(Suits.DIAMOND, 3);
  Card d_4 = new Card(Suits.DIAMOND, 4);
  Card d_5 = new Card(Suits.DIAMOND, 5);
  Card d_6 = new Card(Suits.DIAMOND, 6);
  Card d_7 = new Card(Suits.DIAMOND, 7);
  Card d_8 = new Card(Suits.DIAMOND, 8);
  Card d_9 = new Card(Suits.DIAMOND, 9);
  Card d_10 = new Card(Suits.DIAMOND, 10);
  Card d_11 = new Card(Suits.DIAMOND, 11);
  Card d_12 = new Card(Suits.DIAMOND, 12);
  Card d_13 = new Card(Suits.DIAMOND, 13);

  Card c_1 = new Card(Suits.CLUBS, 1);
  Card c_2 = new Card(Suits.CLUBS, 2);
  Card c_3 = new Card(Suits.CLUBS, 3);
  Card c_4 = new Card(Suits.CLUBS, 4);
  Card c_5 = new Card(Suits.CLUBS, 5);
  Card c_6 = new Card(Suits.CLUBS, 6);
  Card c_7 = new Card(Suits.CLUBS, 7);
  Card c_8 = new Card(Suits.CLUBS, 8);
  Card c_9 = new Card(Suits.CLUBS, 9);
  Card c_10 = new Card(Suits.CLUBS, 10);
  Card c_11 = new Card(Suits.CLUBS, 11);
  Card c_12 = new Card(Suits.CLUBS, 12);
  Card c_13 = new Card(Suits.CLUBS, 13);

  Card s_1 = new Card(Suits.SPADES, 1);
  Card s_2 = new Card(Suits.SPADES, 2);
  Card s_3 = new Card(Suits.SPADES, 3);
  Card s_4 = new Card(Suits.SPADES, 4);
  Card s_5 = new Card(Suits.SPADES, 5);
  Card s_6 = new Card(Suits.SPADES, 6);
  Card s_7 = new Card(Suits.SPADES, 7);
  Card s_8 = new Card(Suits.SPADES, 8);
  Card s_9 = new Card(Suits.SPADES, 9);
  Card s_10 = new Card(Suits.SPADES, 10);
  Card s_11 = new Card(Suits.SPADES, 11);
  Card s_12 = new Card(Suits.SPADES, 12);
  Card s_13 = new Card(Suits.SPADES, 13);

  Card h_1 = new Card(Suits.HEART, 1);
  Card h_2 = new Card(Suits.HEART, 2);
  Card h_3 = new Card(Suits.HEART, 3);
  Card h_4 = new Card(Suits.HEART, 4);
  Card h_5 = new Card(Suits.HEART, 5);
  Card h_6 = new Card(Suits.HEART, 6);
  Card h_7 = new Card(Suits.HEART, 7);
  Card h_8 = new Card(Suits.HEART, 8);
  Card h_9 = new Card(Suits.HEART, 9);
  Card h_10 = new Card(Suits.HEART, 10);
  Card h_11 = new Card(Suits.HEART, 11);
  Card h_12 = new Card(Suits.HEART, 12);
  Card h_13 = new Card(Suits.HEART, 13);

  Card wrongRank = new Card(Suits.HEART, 1222); // Invalid Card (Wrong rank)

  // Initialize the cascade, open and foundation piles
  private void initModel() {
    this.model_2 = new SimpleFreecellModel();
    this.model_2.startGame(this.model_2.getDeck(), 4, 1, false);
  }

  // The deck of 52 cards
  List<Card> deck;
  List<Card> deck_copy;
  List<Card> correct_order_deck;

  private void initValidDeck() {
    this.deck = new ArrayList<>(Arrays.asList(this.d_1, this.d_2, this.d_3, this.d_4,
            this.d_5, this.d_6, this.d_7, this.d_8, this.d_9, this.d_10, this.d_11, this.d_12,
            this.d_13, this.c_1, this.c_2, this.c_3, this.c_4, this.c_5, this.c_6, this.c_7,
            this.c_8, this.c_9, this.c_10, this.c_11, this.c_12, this.c_13, this.s_1, this.s_2,
            this.s_3, this.s_4, this.s_5, this.s_6, this.s_7, this.s_8, this.s_9, this.s_10,
            this.s_11, this.s_12, this.s_13, this.h_1, this.h_2, this.h_3, this.h_4, this.h_5,
            this.h_6, this.h_7, this.h_8, this.h_9, this.h_10, this.h_11, this.h_12, this.h_13));
    this.deck_copy = new ArrayList<>();
    this.deck_copy.addAll(this.deck);
  }

  // makes a deck in a simple order (takes advantage of robin hood style dealing of cards)
  private void initModelCorrectDeck() {
    this.correct_order_deck = new ArrayList<>(Arrays.asList(this.d_13, this.c_13, s_13, this.h_13,
            this.d_12, this.c_12, s_12, this.h_12, this.d_11, this.c_11, s_11, this.h_11,
            this.d_10, this.c_10, s_10, this.h_10, this.d_9, this.c_9, s_9, this.h_9,
            this.d_8, this.c_8, s_8, this.h_8, this.d_7, this.c_7, s_7, this.h_7,
            this.d_6, this.c_6, s_6, this.h_6, this.d_5, this.c_5, s_5, this.h_5,
            this.d_4, this.c_4, s_4, this.h_4, this.d_3, this.c_3, s_3, this.h_3,
            this.d_2, this.c_2, s_2, this.h_2, this.d_1, this.c_1, s_1, this.h_1));
    this.model_3 = new SimpleFreecellModel();
    this.model_3.startGame(this.correct_order_deck, 4, 1,
            false);
  }

  // There is a repeated card in this deck
  List<Card> invalidDeck = new ArrayList<>(Arrays.asList(this.d_1, this.d_2, this.d_3, this.d_4,
          this.d_5, this.d_6, this.d_7, this.d_8, this.d_9, this.d_10, this.d_11, this.d_12,
          this.d_13, this.c_1, this.c_2, this.c_3, this.c_4, this.c_5, this.c_6, this.c_7, this.c_8,
          this.c_9, this.c_10, this.c_11, this.c_12, this.c_13, this.s_1, this.s_2, this.s_3,
          this.s_4, this.s_5, this.s_6, this.s_7, this.s_8, this.s_9, this.s_10, this.s_11,
          this.s_12, this.s_13, this.h_1, this.h_2, this.h_3, this.h_4, this.h_5, this.h_6,
          this.h_7, this.h_8, this.h_13, this.h_10, this.h_11, this.h_12, this.h_13));

  // This deck is too small
  List<Card> invalidDeckSize = new ArrayList<>(Arrays.asList(this.d_1, this.d_2, this.d_3, this.d_4,
          this.d_5, this.d_6, this.d_7, this.d_8, this.d_9, this.d_10, this.d_11, this.d_12,
          this.d_13, this.c_1, this.c_2, this.c_3, this.c_4, this.c_5, this.c_6));

  // This deck is has an invalid card - Invalid Rank
  List<Card> invalidCardInDeckRank = new ArrayList<>(Arrays.asList(this.d_1, this.d_2, this.d_3,
          this.d_4, this.d_5, this.d_6, this.d_7, this.d_8, this.d_9, this.d_10, this.d_11,
          this.d_12, this.d_13, this.c_1, this.c_2, this.c_3, this.c_4, this.c_5, this.c_6,
          this.c_7, this.c_8, this.c_9, this.c_10, this.c_11, this.c_12, this.c_13, this.s_1,
          this.s_2, this.s_3, this.s_4, this.s_5, this.s_6, this.s_7, this.s_8, this.s_9,
          this.s_10, this.s_11, this.s_12, this.s_13, this.h_1, this.h_2, this.h_3, this.h_4,
          this.h_5, this.h_6, this.h_7, this.h_8, this.h_13, this.h_10, this.h_11,
          this.wrongRank, this.h_13));

  @Test
  public void getDeckTest() {
    initValidDeck();
    assertEquals(52, model_1.getDeck().size());
    assertEquals(this.deck, this.model_1.getDeck());
  }

  @Test
  public void shuffleDeckTest() {
    initValidDeck();
    assertEquals(this.deck, this.deck_copy);
    model_1.startGame(this.deck, 4, 1, true);
    assertNotEquals(this.deck, this.deck_copy);
  }

  @Test(expected = IllegalArgumentException.class)
  public void disallowInvalidOpenPileNumbers() {
    initValidDeck();
    this.model_1.startGame(this.deck, 5, 0, false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void disallowInvalidCascadePileNumbers() {
    initValidDeck();
    this.model_1.startGame(this.deck, 3, 2, false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void disallowRepeatsInDeck() {
    this.model_1.startGame(this.invalidDeck, 4, 1, false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void disallowIncorrectSizeDeck() {
    this.model_1.startGame(this.invalidDeckSize, 5, 2, false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void disallowInvalidCardRankDeck() {
    this.model_1.startGame(this.invalidCardInDeckRank, 5,
            2, false);
  }

  @Test
  public void checkNumOfCascadePilesChanged() {
    initModel();
    this.model_2.startGame(this.model_2.getDeck(), 4, 1, false);
    assertEquals(4, model_2.getNumCascadePiles());
    assertEquals(1, model_2.getNumOpenPiles());
    this.model_2.startGame(this.model_2.getDeck(), 5, 2, false);
    assertEquals(5, this.model_2.getNumCascadePiles());
    assertEquals(2, this.model_2.getNumOpenPiles());
  }

  @Test(expected = IllegalStateException.class)
  public void disallowMovingWhileInactiveGame() {
    this.model_1.move(PileType.FOUNDATION, 1, 2,
            PileType.OPEN, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void disallowMovingFoundationPile() {
    this.initModel();
    this.model_2.move(PileType.FOUNDATION, 1, 2,
            PileType.OPEN, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void moveCardIntoNonEmptyOpen() {
    this.initModel();
    this.model_2.move(PileType.CASCADE, 1, 0, PileType.OPEN, 0);
    this.model_2.move(PileType.CASCADE, 2, 0, PileType.OPEN, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void moveWrongOrderCardIntoFoundation() {
    this.initModel();
    this.model_2.move(PileType.CASCADE, 0, 0,
            PileType.FOUNDATION, 1);
    this.model_2.move(PileType.CASCADE, 0, 1,
            PileType.FOUNDATION, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonAceCardIntoEmptyFoundation() {
    this.initModel();
    this.model_2.move(PileType.CASCADE, 1, 0,
            PileType.FOUNDATION, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void moveNoneLastCard() {
    this.initModel();
    this.model_2.move(PileType.CASCADE, 0, 0, PileType.OPEN, 0);
  }

  @Test
  public void cardIsDeletedAfterMoving() {
    this.initModel();
    assertEquals(13, this.model_2.getNumCardsInCascadePile(0));
    this.model_2.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);
    assertEquals(12, this.model_2.getNumCardsInCascadePile(0));
  }


  @Test
  public void gameOverTest() {
    assertEquals(false, this.model_1.isGameOver());
    this.initModelCorrectDeck();
    assertEquals(false, this.model_3.isGameOver());
    for (int i = 0; i < 4; i++) {
      for (int j = 12; j >= 0; j -= 1) {
        this.model_3.move(PileType.CASCADE, i, j, PileType.FOUNDATION, i);
      }
    }
    assertEquals(true, this.model_3.isGameOver());
  }

  @Test(expected = IllegalStateException.class)
  public void moveWhileGameNotActive() {
    this.model_1.move(PileType.CASCADE, 0, 0, PileType.OPEN, 0);
  }

  @Test(expected = IllegalStateException.class)
  public void getNumCardsAtFoundationWhileInActiveGame() {
    this.model_1.getNumCardsInFoundationPile(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getNumCardsAtFoundationInvalidIdxLowerLimit() {
    this.initModel();
    this.model_2.getNumCardsInFoundationPile(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getNumCardsAtFoundationInvalidIdxUpperLimit() {
    this.initModel();
    this.model_2.getNumCardsInFoundationPile(7);
  }

  @Test
  public void getNumCardsInFoundationPileTest() {
    this.initModelCorrectDeck();
    this.model_3.move(PileType.CASCADE, 3, 12, PileType.FOUNDATION, 0);
    assertEquals(1, this.model_3.getNumCardsInFoundationPile(0));
  }

  @Test
  public void getNumCascadePilesWhileInActiveGame() {
    assertEquals(-1, this.model_1.getNumCascadePiles());
    this.initModel();
    assertEquals(4, this.model_2.getNumCascadePiles());
  }

  @Test(expected = IllegalArgumentException.class)
  public void getNumCardsAtCascadeInvalidIdxLowerLimit() {
    this.initModel();
    this.model_2.getNumCardsInCascadePile(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getNumCardsAtCascadeInvalidIdxUpperLimit() {
    this.initModel();
    this.model_2.getNumCardsInCascadePile(5);
  }

  @Test(expected = IllegalStateException.class)
  public void getNumCardsAtCascadePileWhileInactiveGame() {
    this.model_1.getNumCardsInCascadePile(0);
  }

  @Test
  public void getNumCardsAtCascadePileTest() {
    this.initModel();
    assertEquals(13, this.model_2.getNumCardsInCascadePile(2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void getNumCardAtOpenPileInvalidIndex() {
    this.initModel();
    this.model_2.getNumCardsInOpenPile(1);
  }

  @Test(expected = IllegalStateException.class)
  public void getNumCardsAtOpenPileWhileInactiveGame() {
    this.model_1.getNumCardsInOpenPile(0);
  }

  @Test
  public void getNumCardsAtOpenPileTest() {
    initModel();
    assertEquals(0, this.model_2.getNumCardsInOpenPile(0));
  }

  @Test
  public void getNumOpenPilesTest() {
    assertEquals(-1, this.model_1.getNumOpenPiles());
    initModel();
    assertEquals(1, this.model_2.getNumOpenPiles());
  }

  @Test(expected = IllegalStateException.class)
  public void getFoundationCardAtInActiveGame() {
    this.model_1.getFoundationCardAt(0, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getFoundationCardOutOfRange() {
    this.initModel();
    this.model_2.getFoundationCardAt(5, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getFoundationCardOutOfRangeCard() {
    this.initModel();
    this.model_2.getFoundationCardAt(0, 1);
  }

  @Test
  public void getFoundationCardTest() {
    this.initModelCorrectDeck();
    this.model_3.move(PileType.CASCADE, 0, 12,
            PileType.FOUNDATION, 0);
    assertEquals("A♦", this.model_3.getFoundationCardAt(0, 0).toString());
  }

  @Test(expected = IllegalStateException.class)
  public void getCascadeCardAtInActiveGame() {
    this.model_1.getCascadeCardAt(0, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getCascadeCardOutOfRange() {
    this.initModel();
    this.model_2.getCascadeCardAt(-1, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getCascadeCardOutOfRangeCard() {
    this.initModel();
    this.model_2.getCascadeCardAt(0, 27);
  }

  @Test
  public void getCascadeCardTest() {
    this.initModel();
    assertEquals("A♦", this.model_2.getCascadeCardAt(0, 0).toString());
  }

  @Test(expected = IllegalStateException.class)
  public void getOpenCardInActiveGame() {
    this.model_1.getOpenCardAt(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getOpenCardPileOutOfRange() {
    this.initModel();
    this.model_2.getOpenCardAt(2);
  }

  @Test
  public void getOpenCardTest() {
    this.initModelCorrectDeck();
    assertEquals(null, this.model_3.getOpenCardAt(0));
    this.model_3.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);
    assertEquals("A♦", this.model_3.getOpenCardAt(0).toString());
  }

  @Test
  public void testStartGameBeingRestart() {
    this.initModelCorrectDeck();
    assertEquals(0, this.model_3.getNumCardsInFoundationPile(0));
    assertEquals(0, this.model_3.getNumCardsInFoundationPile(1));
    assertEquals(0, this.model_3.getNumCardsInFoundationPile(2));
    assertEquals(0, this.model_3.getNumCardsInFoundationPile(3));
    assertEquals(13, this.model_3.getNumCardsInCascadePile(0));
    assertEquals(13, this.model_3.getNumCardsInCascadePile(1));
    assertEquals(13, this.model_3.getNumCardsInCascadePile(2));
    assertEquals(13, this.model_3.getNumCardsInCascadePile(3));
    assertEquals(0, this.model_3.getNumCardsInOpenPile(0));
    this.model_3.move(PileType.CASCADE, 0, 12, PileType.FOUNDATION, 0);
    this.model_3.move(PileType.CASCADE, 1, 12, PileType.FOUNDATION, 1);
    this.model_3.move(PileType.CASCADE, 2, 12, PileType.FOUNDATION, 2);
    this.model_3.move(PileType.CASCADE, 3, 12, PileType.FOUNDATION, 3);
    this.model_3.move(PileType.CASCADE, 3, 11, PileType.OPEN, 0);
    assertEquals(1, this.model_3.getNumCardsInFoundationPile(0));
    assertEquals(1, this.model_3.getNumCardsInFoundationPile(1));
    assertEquals(1, this.model_3.getNumCardsInFoundationPile(2));
    assertEquals(1, this.model_3.getNumCardsInFoundationPile(3));
    assertEquals(12, this.model_3.getNumCardsInCascadePile(0));
    assertEquals(12, this.model_3.getNumCardsInCascadePile(1));
    assertEquals(12, this.model_3.getNumCardsInCascadePile(2));
    assertEquals(11, this.model_3.getNumCardsInCascadePile(3));
    assertEquals(1, this.model_3.getNumCardsInOpenPile(0));
    this.model_3.startGame(this.model_3.getDeck(),4,1, false);
    assertEquals(0, this.model_3.getNumCardsInFoundationPile(0));
    assertEquals(0, this.model_3.getNumCardsInFoundationPile(1));
    assertEquals(0, this.model_3.getNumCardsInFoundationPile(2));
    assertEquals(0, this.model_3.getNumCardsInFoundationPile(3));
    assertEquals(13, this.model_3.getNumCardsInCascadePile(0));
    assertEquals(13, this.model_3.getNumCardsInCascadePile(1));
    assertEquals(13, this.model_3.getNumCardsInCascadePile(2));
    assertEquals(13, this.model_3.getNumCardsInCascadePile(3));
    assertEquals(0, this.model_3.getNumCardsInOpenPile(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidCascadePileMove() {
    this.initModelCorrectDeck();
    this.model_3.move(PileType.CASCADE, 0, 12, PileType.CASCADE, 1);
  }

  @Test
  public void moveIntoNoneEmptyFoundation() {
    this.initModelCorrectDeck();
    assertEquals(0, this.model_3.getNumCardsInFoundationPile(0));
    this.model_3.move(PileType.CASCADE, 0, 12, PileType.FOUNDATION, 0);
    this.model_3.move(PileType.CASCADE, 0, 11, PileType.FOUNDATION, 0);
    assertEquals(2, model_3.getNumCardsInFoundationPile(0));
    this.model_3.move(PileType.CASCADE, 0, 10, PileType.FOUNDATION, 0);
    assertEquals(3, this.model_3.getNumCardsInFoundationPile(0));
  }

  @Test
  public void moveCascadeIntoCascade() {
    this.initModelCorrectDeck();
    this.model_3.move(PileType.CASCADE, 0, 12, PileType.FOUNDATION, 0);
    this.model_3.move(PileType.CASCADE, 1, 12, PileType.CASCADE, 0);
    assertEquals(13, model_3.getNumCardsInCascadePile(0));
  }
}