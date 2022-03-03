import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import cs3500.freecell.model.Card;
import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.PileType;
import cs3500.freecell.model.SimpleFreecellModel;
import cs3500.freecell.model.Suits;
import cs3500.freecell.model.multimove.MultiMoveSimpleFreecellModel;
import cs3500.freecell.view.FreecellTextView;
import cs3500.freecell.view.FreecellView;

import static org.junit.Assert.assertEquals;

/**
 * To test the freecell View implementations.
 */
public class FreecellTextViewTest {
  FreecellModel<Card> model;
  FreecellView view;
  Appendable ap;
  List<Card> orderedDeck;

  /**
   * Initializes the simple freecell model.
   */
  private void init() {
    this.model = new SimpleFreecellModel();
    this.view = new FreecellTextView(model);
  }

  /**
   * Initializes the view for a game of freecell.
   */
  private void initView() {
    this.ap = new StringBuilder();
    this.model = new SimpleFreecellModel();
    this.view = new FreecellTextView(model, ap);
  }

  /**
   * Starts the game of simple freecell.
   */
  private void initModel() {
    this.model.startGame(model.getDeck(),
            4, 4, false);
  }

  /**
   * Initializes the multi freecell model.
   */
  private void initMulti() {
    this.model = new MultiMoveSimpleFreecellModel();
    this.ap = new StringBuilder();
    this.view = new FreecellTextView(model, ap);
  }

  /**
   * Starts a game of multi freecell.
   */
  private void startMulti() {
    this.model.startGame(model.getDeck(),
            4, 4, false);
  }

  /**
   * Makes an ordered deck of cards. Ordered means that the cascade piles will have ordered sets
   * when the game is started. Makes a game with an ordered set of cards.
   */
  private void initDeck() {
    List<Suits> suits = new ArrayList<>(Arrays.asList(Suits.HEART,
            Suits.SPADES, Suits.DIAMOND, Suits.CLUBS));
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

  @Test
  public void toStringTestForView() {
    this.init();
    assertEquals("", view.toString());
    this.initModel();
    assertEquals("F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" + "O2:\n" + "O3:\n" +
            "O4:\n" + "C1: A♦, 5♦, 9♦, K♦, 4♣, 8♣, Q♣, 3♠, 7♠, J♠, 2♥, 6♥, 10♥\n" +
            "C2: 2♦, 6♦, 10♦, A♣, 5♣, 9♣, K♣, 4♠, 8♠, Q♠, 3♥, 7♥, J♥\n" +
            "C3: 3♦, 7♦, J♦, 2♣, 6♣, 10♣, A♠, 5♠, 9♠, K♠, 4♥, 8♥, Q♥\n" +
            "C4: 4♦, 8♦, Q♦, 3♣, 7♣, J♣, 2♠, 6♠, 10♠, A♥, 5♥, 9♥, K♥", view.toString());
    model.move(PileType.CASCADE, 3, 12, PileType.OPEN, 0);
    model.move(PileType.CASCADE, 3, 11, PileType.OPEN, 1);
    model.move(PileType.CASCADE, 3, 10, PileType.OPEN, 2);
    model.move(PileType.CASCADE, 3, 9, PileType.FOUNDATION, 1);
    assertEquals("F1:\n" + "F2: A♥\n" + "F3:\n" + "F4:\n" + "O1: K♥\n" + "O2: 9♥\n" +
            "O3: 5♥\n" + "O4:\n" + "C1: A♦, 5♦, 9♦, K♦, 4♣, 8♣, Q♣, 3♠, 7♠, J♠, 2♥, 6♥, 10♥\n" +
            "C2: 2♦, 6♦, 10♦, A♣, 5♣, 9♣, K♣, 4♠, 8♠, Q♠, 3♥, 7♥, J♥\n" +
            "C3: 3♦, 7♦, J♦, 2♣, 6♣, 10♣, A♠, 5♠, 9♠, K♠, 4♥, 8♥, Q♥\n" +
            "C4: 4♦, 8♦, Q♦, 3♣, 7♣, J♣, 2♠, 6♠, 10♠", view.toString());
  }

  @Test
  public void testRenderImage() throws IOException {
    String expected = "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" + "O2:\n" +
            "O3:\n" + "O4:\n" + "C1: A♦, 5♦, 9♦, K♦, 4♣, 8♣, Q♣, 3♠, 7♠, J♠, 2♥, 6♥, 10♥\n" +
            "C2: 2♦, 6♦, 10♦, A♣, 5♣, 9♣, K♣, 4♠, 8♠, Q♠, 3♥, 7♥, J♥\n" +
            "C3: 3♦, 7♦, J♦, 2♣, 6♣, 10♣, A♠, 5♠, 9♠, K♠, 4♥, 8♥, Q♥\n" +
            "C4: 4♦, 8♦, Q♦, 3♣, 7♣, J♣, 2♠, 6♠, 10♠, A♥, 5♥, 9♥, K♥\n";
    this.initView();
    this.initModel();
    view.renderBoard();
    assertEquals(expected, ap.toString());
  }

  @Test
  public void testRenderMessage() throws IOException {
    String expected = "No way in ....";
    this.initView();
    this.initModel();
    view.renderMessage("No way in ....");
    assertEquals(expected, ap.toString());
  }

  @Test(expected = IOException.class)
  public void faillingRenderImage() throws IOException {
    Appendable failling = new FailingAppendable(0);
    FreecellModel<Card> model = new SimpleFreecellModel();
    model.startGame(model.getDeck(), 4, 1, false);
    FreecellView view = new FreecellTextView(model, failling);
    view.renderBoard();
  }

  @Test(expected = IOException.class)
  public void faillingRenderMessage() throws IOException {
    Appendable failling = new FailingAppendable(0);
    FreecellModel<Card> model = new SimpleFreecellModel();
    model.startGame(model.getDeck(), 7, 10, false);
    FreecellView view = new FreecellTextView(model, failling);
    view.renderMessage("Invalid move !");
  }

  @Test
  public void toStringTestForViewMulti() {
    this.initMulti();
    assertEquals("", view.toString());
    this.startMulti();
    assertEquals("F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" + "O2:\n" + "O3:\n" +
            "O4:\n" + "C1: A♦, 5♦, 9♦, K♦, 4♣, 8♣, Q♣, 3♠, 7♠, J♠, 2♥, 6♥, 10♥\n" +
            "C2: 2♦, 6♦, 10♦, A♣, 5♣, 9♣, K♣, 4♠, 8♠, Q♠, 3♥, 7♥, J♥\n" +
            "C3: 3♦, 7♦, J♦, 2♣, 6♣, 10♣, A♠, 5♠, 9♠, K♠, 4♥, 8♥, Q♥\n" +
            "C4: 4♦, 8♦, Q♦, 3♣, 7♣, J♣, 2♠, 6♠, 10♠, A♥, 5♥, 9♥, K♥", view.toString());
    model.move(PileType.CASCADE, 3, 12, PileType.OPEN, 0);
    model.move(PileType.CASCADE, 3, 11, PileType.OPEN, 1);
    model.move(PileType.CASCADE, 3, 10, PileType.OPEN, 2);
    model.move(PileType.CASCADE, 3, 9, PileType.FOUNDATION, 1);
    assertEquals("F1:\n" + "F2: A♥\n" + "F3:\n" + "F4:\n" + "O1: K♥\n" + "O2: 9♥\n" +
            "O3: 5♥\n" + "O4:\n" + "C1: A♦, 5♦, 9♦, K♦, 4♣, 8♣, Q♣, 3♠, 7♠, J♠, 2♥, 6♥, 10♥\n" +
            "C2: 2♦, 6♦, 10♦, A♣, 5♣, 9♣, K♣, 4♠, 8♠, Q♠, 3♥, 7♥, J♥\n" +
            "C3: 3♦, 7♦, J♦, 2♣, 6♣, 10♣, A♠, 5♠, 9♠, K♠, 4♥, 8♥, Q♥\n" +
            "C4: 4♦, 8♦, Q♦, 3♣, 7♣, J♣, 2♠, 6♠, 10♠", view.toString());
  }

  @Test
  public void testRenderImageMulti() throws IOException {
    this.initMulti();
    this.startMulti();
    String expected = "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" + "O2:\n" +
            "O3:\n" + "O4:\n" + "C1: A♦, 5♦, 9♦, K♦, 4♣, 8♣, Q♣, 3♠, 7♠, J♠, 2♥, 6♥, 10♥\n" +
            "C2: 2♦, 6♦, 10♦, A♣, 5♣, 9♣, K♣, 4♠, 8♠, Q♠, 3♥, 7♥, J♥\n" +
            "C3: 3♦, 7♦, J♦, 2♣, 6♣, 10♣, A♠, 5♠, 9♠, K♠, 4♥, 8♥, Q♥\n" +
            "C4: 4♦, 8♦, Q♦, 3♣, 7♣, J♣, 2♠, 6♠, 10♠, A♥, 5♥, 9♥, K♥\n";
    view.renderBoard();
    assertEquals(expected, ap.toString());
  }

  @Test
  public void testRenderMessageMulti() throws IOException {
    String expected = "No way in ....";
    this.initMulti();
    this.startMulti();
    view.renderMessage("No way in ....");
    assertEquals(expected, ap.toString());
  }

  @Test(expected = IOException.class)
  public void faillingRenderImageMulti() throws IOException {
    Appendable failling = new FailingAppendable(0);
    FreecellModel<Card> model = new MultiMoveSimpleFreecellModel();
    model.startGame(model.getDeck(), 4, 1, false);
    FreecellView view = new FreecellTextView(model, failling);
    view.renderBoard();
  }

  @Test(expected = IOException.class)
  public void faillingRenderMessageMulti() throws IOException {
    Appendable failling = new FailingAppendable(0);
    FreecellModel<Card> model = new MultiMoveSimpleFreecellModel();
    model.startGame(model.getDeck(), 7, 10, false);
    FreecellView view = new FreecellTextView(model, failling);
    view.renderMessage("Invalid move !");
  }

  @Test
  public void displayABuild() {
    String expected = "F1: A♥, 2♥, 3♥\n" + "F2: A♠, 2♠\n" + "F3: A♦\n" + "F4:\n" + "O1:\n" +
            "O2:\n" + "O3:\n" + "O4:\n" +
            "C1: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♠, 2♦, A♣\n" +
            "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠\n" +
            "C3: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦\n" +
            "C4: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣";
    initDeck();
    model = new MultiMoveSimpleFreecellModel();
    model.startGame(orderedDeck, 4, 4, false);
    view = new FreecellTextView(model);
    makeBuild(null);
    assertEquals(expected, view.toString());
  }
}