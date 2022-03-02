import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cs3500.freecell.controller.SimpleFreecellController;
import cs3500.freecell.model.Card;
import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.SimpleFreecellModel;
import cs3500.freecell.model.Suits;
import cs3500.freecell.model.multimove.MultiMoveSimpleFreecellModel;

import static org.junit.Assert.assertEquals;

/**
 * JUnit test cases for the freecell controller.
 */
public class FreecellControllerTest {

  private List<Card> orderedDeck;
  private List<Suits> suits = new ArrayList<>(Arrays.asList(Suits.HEART,
          Suits.SPADES, Suits.CLUBS, Suits.DIAMOND));
  private String winSequenece;

  /**
   * Makes an ordered deck of cards. Ordered means that the cascade piles will have ordered sets
   * when the game is started.
   */
  private void initOrderedDeck() {
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
  }

  /**
   * Makes a input sequence that leads to winning (based on orderedDeck order).
   */
  private void win() {
    List<String> seq = new ArrayList<>();
    for (int i = 1; i < 5; i++) {
      for (int j = 13; j > 0; j--) {
        seq.add("C" + i + " " + j + " " + "F" + i);
      }
    }
    winSequenece = String.join(" ", seq);
  }

  /**
   * Makes a input sequence that leads to winning (based on orderedDeck order).
   */
  private void halfwin() {
    List<String> seq = new ArrayList<>();
    for (int i = 1; i < 3; i++) {
      for (int j = 13; j > 0; j--) {
        seq.add("C" + i + " " + j + " " + "F" + i);
      }
    }
    seq.add("wqehihfie");
    seq.add("weiiefwhfiwe");
    for (int i = 3; i < 5; i++) {
      for (int j = 13; j > 0; j--) {
        seq.add("C" + i + " " + j + " " + "F" + i);
      }
    }
    winSequenece = String.join(" ", seq);
  }

  @Test(expected = IllegalArgumentException.class)
  public void disallowAnyNullModel() {
    SimpleFreecellController controller = new SimpleFreecellController(null,
            new StringReader("C1"), new StringBuilder());
  }

  @Test(expected = IllegalArgumentException.class)
  public void disallowAnyNullReadable() {
    SimpleFreecellController controller = new SimpleFreecellController(new SimpleFreecellModel(),
            null, new StringBuilder());
  }

  @Test(expected = IllegalArgumentException.class)
  public void disallowAnyNullAppendable() {
    SimpleFreecellController controller = new SimpleFreecellController(new SimpleFreecellModel(),
            new StringReader("C1"), null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void disallowNullDeck() throws IOException {
    SimpleFreecellController controller = new SimpleFreecellController(new SimpleFreecellModel(),
            new StringReader("C1"), new StringBuilder());
    controller.playGame(null, 4, 1,false);
  }

  @Test
  public void disallowInvalidCascadePile() throws IOException {
    Appendable text = new StringBuilder();
    FreecellModel<Card> model = new SimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model,
            new StringReader("C1"), text);
    controller.playGame(model.getDeck(), 1, 1,false);
    assertEquals("Could not start game.", text.toString());
  }

  @Test
  public void disallowInvalidOpenPile() throws IOException {
    Appendable text = new StringBuilder();
    FreecellModel<Card> model = new SimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model,
            new StringReader("C1"), text);
    controller.playGame(model.getDeck(), 4, 0,false);
    assertEquals("Could not start game.", text.toString());
  }

  @Test
  public void testForQuit() throws IOException {
    String expectedResult = "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: A♦, 5♦, 9♦, K♦, 4♣, 8♣, Q♣, 3♠, 7♠, J♠, 2♥, 6♥, 10♥\n" +
            "C2: 2♦, 6♦, 10♦, A♣, 5♣, 9♣, K♣, 4♠, 8♠, Q♠, 3♥, 7♥, J♥\n" +
            "C3: 3♦, 7♦, J♦, 2♣, 6♣, 10♣, A♠, 5♠, 9♠, K♠, 4♥, 8♥, Q♥\n" +
            "C4: 4♦, 8♦, Q♦, 3♣, 7♣, J♣, 2♠, 6♠, 10♠, A♥, 5♥, 9♥, K♥\n" +
            "Game quit prematurely.";
    Appendable text = new StringBuilder();
    Readable rd = new StringReader("Q");
    FreecellModel<Card> model = new SimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model,
            rd, text);
    controller.playGame(model.getDeck(), 4, 1, false);
    assertEquals(expectedResult, text.toString());
  }

  @Test
  public void testInputForSourceTooShort() throws IOException {
    String expectedResult = "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: A♦, 5♦, 9♦, K♦, 4♣, 8♣, Q♣, 3♠, 7♠, J♠, 2♥, 6♥, 10♥\n" +
            "C2: 2♦, 6♦, 10♦, A♣, 5♣, 9♣, K♣, 4♠, 8♠, Q♠, 3♥, 7♥, J♥\n" +
            "C3: 3♦, 7♦, J♦, 2♣, 6♣, 10♣, A♠, 5♠, 9♠, K♠, 4♥, 8♥, Q♥\n" +
            "C4: 4♦, 8♦, Q♦, 3♣, 7♣, J♣, 2♠, 6♠, 10♠, A♥, 5♥, 9♥, K♥\n" +
            "Index must be an integer !\n" +
            "Game quit prematurely.";
    Appendable text = new StringBuilder();
    Readable rd = new StringReader("C Q Q Q Q");
    FreecellModel<Card> model = new SimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model,
            rd, text);
    controller.playGame(model.getDeck(), 4, 1, false);
    assertEquals(expectedResult, text.toString());
  }

  @Test
  public void testInputForSourceButNoInteger() {
    String expectedResult = "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: A♦, 5♦, 9♦, K♦, 4♣, 8♣, Q♣, 3♠, 7♠, J♠, 2♥, 6♥, 10♥\n" +
            "C2: 2♦, 6♦, 10♦, A♣, 5♣, 9♣, K♣, 4♠, 8♠, Q♠, 3♥, 7♥, J♥\n" +
            "C3: 3♦, 7♦, J♦, 2♣, 6♣, 10♣, A♠, 5♠, 9♠, K♠, 4♥, 8♥, Q♥\n" +
            "C4: 4♦, 8♦, Q♦, 3♣, 7♣, J♣, 2♠, 6♠, 10♠, A♥, 5♥, 9♥, K♥\n" +
            "Index must be an integer !\n" +
            "Game quit prematurely.";
    Appendable text = new StringBuilder();
    Readable rd = new StringReader("CC 30 Q");
    FreecellModel<Card> model = new SimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model, rd, text);
    controller.playGame(model.getDeck(), 4, 1, false);
    assertEquals(expectedResult, text.toString());
  }

  @Test
  public void goodSourceInputCascade() {
    String expectedResult = "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: A♦, 5♦, 9♦, K♦, 4♣, 8♣, Q♣, 3♠, 7♠, J♠, 2♥, 6♥, 10♥\n" +
            "C2: 2♦, 6♦, 10♦, A♣, 5♣, 9♣, K♣, 4♠, 8♠, Q♠, 3♥, 7♥, J♥\n" +
            "C3: 3♦, 7♦, J♦, 2♣, 6♣, 10♣, A♠, 5♠, 9♠, K♠, 4♥, 8♥, Q♥\n" +
            "C4: 4♦, 8♦, Q♦, 3♣, 7♣, J♣, 2♠, 6♠, 10♠, A♥, 5♥, 9♥, K♥\n" +
            "Game quit prematurely.";
    Appendable text = new StringBuilder();
    Readable rd = new StringReader("C1 Q");
    FreecellModel<Card> model = new SimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model, rd, text);
    controller.playGame(model.getDeck(), 4, 1, false);
    assertEquals(expectedResult, text.toString());
  }

  @Test
  public void goodSourceInputAfterMultipleTries() {
    String expectedResult = "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: A♦, 5♦, 9♦, K♦, 4♣, 8♣, Q♣, 3♠, 7♠, J♠, 2♥, 6♥, 10♥\n" +
            "C2: 2♦, 6♦, 10♦, A♣, 5♣, 9♣, K♣, 4♠, 8♠, Q♠, 3♥, 7♥, J♥\n" +
            "C3: 3♦, 7♦, J♦, 2♣, 6♣, 10♣, A♠, 5♠, 9♠, K♠, 4♥, 8♥, Q♥\n" +
            "C4: 4♦, 8♦, Q♦, 3♣, 7♣, J♣, 2♠, 6♠, 10♠, A♥, 5♥, 9♥, K♥\n" +
            "Invalid pile type !\n" + "Invalid pile type !\n" + "Index must be an integer !\n" +
            "Index must be an integer !\n" + "Invalid pile type !\n" + "Game quit prematurely.";
    Appendable text = new StringBuilder();
    Readable rd = new StringReader("ehduuhw euwdue Cdiejiejei a 44 1 uhgu Q");
    FreecellModel<Card> model = new SimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model, rd, text);
    controller.playGame(model.getDeck(), 4, 1, false);
    assertEquals(expectedResult, text.toString());
  }

  @Test
  public void goodSourceInputOpen() {
    String expectedResult = "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: A♦, 5♦, 9♦, K♦, 4♣, 8♣, Q♣, 3♠, 7♠, J♠, 2♥, 6♥, 10♥\n" +
            "C2: 2♦, 6♦, 10♦, A♣, 5♣, 9♣, K♣, 4♠, 8♠, Q♠, 3♥, 7♥, J♥\n" +
            "C3: 3♦, 7♦, J♦, 2♣, 6♣, 10♣, A♠, 5♠, 9♠, K♠, 4♥, 8♥, Q♥\n" +
            "C4: 4♦, 8♦, Q♦, 3♣, 7♣, J♣, 2♠, 6♠, 10♠, A♥, 5♥, 9♥, K♥\n" +
            "Game quit prematurely.";
    Appendable text = new StringBuilder();
    Readable rd = new StringReader("O1 Q");
    FreecellModel<Card> model = new SimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model, rd, text);
    controller.playGame(model.getDeck(), 4, 1, false);
    assertEquals(expectedResult, text.toString());
  }

  @Test
  public void goodSourceInputFoundation() {
    String expectedResult = "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: A♦, 5♦, 9♦, K♦, 4♣, 8♣, Q♣, 3♠, 7♠, J♠, 2♥, 6♥, 10♥\n" +
            "C2: 2♦, 6♦, 10♦, A♣, 5♣, 9♣, K♣, 4♠, 8♠, Q♠, 3♥, 7♥, J♥\n" +
            "C3: 3♦, 7♦, J♦, 2♣, 6♣, 10♣, A♠, 5♠, 9♠, K♠, 4♥, 8♥, Q♥\n" +
            "C4: 4♦, 8♦, Q♦, 3♣, 7♣, J♣, 2♠, 6♠, 10♠, A♥, 5♥, 9♥, K♥\n" +
            "Game quit prematurely.";
    Appendable text = new StringBuilder();
    Readable rd = new StringReader("F1 Q");
    FreecellModel<Card> model = new SimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model, rd, text);
    controller.playGame(model.getDeck(), 4, 1, false);
    assertEquals(expectedResult, text.toString());
  }

  @Test
  public void testInputForCardIdxNotNumber() {
    String expected = "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: A♦, 5♦, 9♦, K♦, 4♣, 8♣, Q♣, 3♠, 7♠, J♠, 2♥, 6♥, 10♥\n" +
            "C2: 2♦, 6♦, 10♦, A♣, 5♣, 9♣, K♣, 4♠, 8♠, Q♠, 3♥, 7♥, J♥\n" +
            "C3: 3♦, 7♦, J♦, 2♣, 6♣, 10♣, A♠, 5♠, 9♠, K♠, 4♥, 8♥, Q♥\n" +
            "C4: 4♦, 8♦, Q♦, 3♣, 7♣, J♣, 2♠, 6♠, 10♠, A♥, 5♥, 9♥, K♥\n" +
            "Index must be an integer !\n" +
            "Game quit prematurely.";
    Appendable text = new StringBuilder();
    Readable rd = new StringReader("C1 ccccc Q");
    FreecellModel<Card> model = new SimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model, rd, text);
    controller.playGame(model.getDeck(), 4, 1, false);
    assertEquals(expected, text.toString());
  }

  @Test
  public void testDestinationBadInputs() {
    String expected = "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: A♦, 5♦, 9♦, K♦, 4♣, 8♣, Q♣, 3♠, 7♠, J♠, 2♥, 6♥, 10♥\n" +
            "C2: 2♦, 6♦, 10♦, A♣, 5♣, 9♣, K♣, 4♠, 8♠, Q♠, 3♥, 7♥, J♥\n" +
            "C3: 3♦, 7♦, J♦, 2♣, 6♣, 10♣, A♠, 5♠, 9♠, K♠, 4♥, 8♥, Q♥\n" +
            "C4: 4♦, 8♦, Q♦, 3♣, 7♣, J♣, 2♠, 6♠, 10♠, A♥, 5♥, 9♥, K♥\n" +
            "Invalid pile type !\n" + "Invalid pile type !\n" +
            "Invalid pile type !\n" + "Game quit prematurely.";
    Appendable text = new StringBuilder();
    Readable rd = new StringReader("C1 10 1 fewije icenwnie q");
    FreecellModel<Card> model = new SimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model, rd, text);
    controller.playGame(model.getDeck(), 4, 1, false);
    assertEquals(expected, text.toString());
  }

  @Test
  public void testDestinationBadInputsIndex() {
    String expected = "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: A♦, 5♦, 9♦, K♦, 4♣, 8♣, Q♣, 3♠, 7♠, J♠, 2♥, 6♥, 10♥\n" +
            "C2: 2♦, 6♦, 10♦, A♣, 5♣, 9♣, K♣, 4♠, 8♠, Q♠, 3♥, 7♥, J♥\n" +
            "C3: 3♦, 7♦, J♦, 2♣, 6♣, 10♣, A♠, 5♠, 9♠, K♠, 4♥, 8♥, Q♥\n" +
            "C4: 4♦, 8♦, Q♦, 3♣, 7♣, J♣, 2♠, 6♠, 10♠, A♥, 5♥, 9♥, K♥\n" +
            "Index must be an integer !\n" + "Index must be an integer !\n" +
            "Index must be an integer !\n" + "Game quit prematurely.";
    Appendable text = new StringBuilder();
    Readable rd = new StringReader("C1 10 F fewije icenwnie q");
    FreecellModel<Card> model = new SimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model, rd, text);
    controller.playGame(model.getDeck(), 4, 1, false);
    assertEquals(expected, text.toString());
  }

  @Test
  public void testMovingtoOpenPile() {
    String expected = "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: A♦, 5♦, 9♦, K♦, 4♣, 8♣, Q♣, 3♠, 7♠, J♠, 2♥, 6♥, 10♥\n" +
            "C2: 2♦, 6♦, 10♦, A♣, 5♣, 9♣, K♣, 4♠, 8♠, Q♠, 3♥, 7♥, J♥\n" +
            "C3: 3♦, 7♦, J♦, 2♣, 6♣, 10♣, A♠, 5♠, 9♠, K♠, 4♥, 8♥, Q♥\n" +
            "C4: 4♦, 8♦, Q♦, 3♣, 7♣, J♣, 2♠, 6♠, 10♠, A♥, 5♥, 9♥, K♥\n" +
            "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1: 10♥\n" +
            "C1: A♦, 5♦, 9♦, K♦, 4♣, 8♣, Q♣, 3♠, 7♠, J♠, 2♥, 6♥\n" +
            "C2: 2♦, 6♦, 10♦, A♣, 5♣, 9♣, K♣, 4♠, 8♠, Q♠, 3♥, 7♥, J♥\n" +
            "C3: 3♦, 7♦, J♦, 2♣, 6♣, 10♣, A♠, 5♠, 9♠, K♠, 4♥, 8♥, Q♥\n" +
            "C4: 4♦, 8♦, Q♦, 3♣, 7♣, J♣, 2♠, 6♠, 10♠, A♥, 5♥, 9♥, K♥\n" +
            "Game quit prematurely.";
    Appendable text = new StringBuilder();
    Readable rd = new StringReader("C1 13 O1 q");
    FreecellModel<Card> model = new SimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model, rd, text);
    controller.playGame(model.getDeck(), 4, 1, false);
    assertEquals(expected, text.toString());
  }

  @Test
  public void testMovingtoFoundationFromOpen() {
    initOrderedDeck();
    String expected = "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "C3: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C4: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1: A♥\n" +
            "C1: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥\n" +
            "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "C3: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C4: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "F1: A♥\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥\n" +
            "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "C3: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C4: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "Game quit prematurely.";
    Appendable text = new StringBuilder();
    Readable rd = new StringReader("C1 13 O1 O1 1 F1 Q");
    FreecellModel<Card> model = new SimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model, rd, text);
    controller.playGame(orderedDeck, 4, 1, false);
    assertEquals(expected, text.toString());
  }

  @Test
  public void testMovingtoFoundationFromCascade() {
    initOrderedDeck();
    String expected = "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "C3: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C4: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "F1: A♥\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥\n" +
            "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "C3: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C4: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "Game quit prematurely.";
    Appendable text = new StringBuilder();
    Readable rd = new StringReader("C1 13 F1 Q");
    FreecellModel<Card> model = new SimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model, rd, text);
    controller.playGame(orderedDeck, 4, 1, false);
    assertEquals(expected, text.toString());
  }

  @Test
  public void testMovingtoCascadeFromOpen() {
    initOrderedDeck();
    String expected = "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" + "O2:\n" +
            "C1: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "C3: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C4: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1: A♥\n" + "O2:\n" +
            "C1: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥\n" +
            "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "C3: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C4: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1: A♥\n" + "O2: A♠\n" +
            "C1: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥\n" +
            "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠\n" +
            "C3: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C4: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" + "O2: A♠\n" +
            "C1: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥\n" +
            "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♥\n" +
            "C3: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C4: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "Game quit prematurely.";
    Appendable text = new StringBuilder();
    Readable rd = new StringReader("C1 13 O1 C2 13 O2 O1 1 C2 Q");
    FreecellModel<Card> model = new SimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model, rd, text);
    controller.playGame(orderedDeck, 4, 2, false);
    assertEquals(expected, text.toString());
  }

  @Test
  public void movingOutOfFoundation() {
    initOrderedDeck();
    String expected = "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" + "O2:\n" +
            "C1: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "C3: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C4: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "F1: A♥\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" + "O2:\n" +
            "C1: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥\n" +
            "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "C3: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C4: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "Invalid move !\n" +
            "F1: A♥\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" + "O2:\n" +
            "C1: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥\n" +
            "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "C3: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C4: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "Game quit prematurely.";
    Appendable text = new StringBuilder();
    Readable rd = new StringReader("C1 13 F1 F1 1 O1 q");
    FreecellModel<Card> model = new SimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model, rd, text);
    controller.playGame(orderedDeck, 4, 2, false);
    assertEquals(expected, text.toString());
  }

  @Test
  public void movingOpenToOpen() {
    initOrderedDeck();
    String expected = "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" + "O2:\n" +
            "C1: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "C3: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C4: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1: A♥\n" + "O2:\n" +
            "C1: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥\n" +
            "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "C3: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C4: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" + "O2: A♥\n" +
            "C1: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥\n" +
            "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "C3: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C4: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "Game quit prematurely.";
    Appendable text = new StringBuilder();
    Readable rd = new StringReader("C1 13 O1 O1 1 O2 q");
    FreecellModel<Card> model = new SimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model, rd, text);
    controller.playGame(orderedDeck, 4, 2, false);
    assertEquals(expected, text.toString());
  }

  @Test
  public void movingToCascadeFromCascade() {
    initOrderedDeck();
    String expected = "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n" +
            "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "C3: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C4: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1: A♥\n" +
            "C1: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥\n" +
            "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n" +
            "C3: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C4: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1: A♥\n" +
            "C1: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♠\n" +
            "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠\n" +
            "C3: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n" +
            "C4: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n" +
            "Game quit prematurely.";
    Appendable text = new StringBuilder();
    Readable rd = new StringReader("C1 13 O1 C2 13 C1 q");
    FreecellModel<Card> model = new SimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model, rd, text);
    controller.playGame(orderedDeck, 4, 1, false);
    assertEquals(expected, text.toString());
  }

  @Test(expected = IllegalStateException.class)
  public void failingRenderBoard() {
    Appendable failing = new FailingAppendable(0);
    Readable inputs = new StringReader("C1");
    FreecellModel<Card> model = new SimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model, inputs, failing);
    controller.playGame(model.getDeck(), 4, 1, false);
  }

  @Test(expected = IllegalStateException.class)
  public void faillingMessageInSource() {
    Appendable failling = new FailingAppendable(1);
    Readable inputs = new StringReader("D1");
    FreecellModel<Card> model = new SimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model, inputs, failling);
    controller.playGame(model.getDeck(), 4, 1, false);
  }

  @Test(expected = IllegalStateException.class)
  public void faillingMessageInSourcePileIdx() {
    Appendable failling = new FailingAppendable(1);
    Readable inputs = new StringReader("CC");
    FreecellModel<Card> model = new SimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model, inputs, failling);
    controller.playGame(model.getDeck(), 4, 1, false);
  }

  @Test(expected = IllegalStateException.class)
  public void faillingMessageCardIdx() {
    Appendable failling = new FailingAppendable(1);
    Readable inputs = new StringReader("C1 3000");
    FreecellModel<Card> model = new SimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model, inputs, failling);
    controller.playGame(model.getDeck(), 4, 1, false);
  }

  @Test(expected = IllegalStateException.class)
  public void faillingDestinationPile() {
    Appendable failling = new FailingAppendable(1);
    Readable inputs = new StringReader("C1 12 R1");
    FreecellModel<Card> model = new SimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model, inputs, failling);
    controller.playGame(model.getDeck(), 4, 1, false);
  }

  @Test(expected = IllegalStateException.class)
  public void faillingDestinationPileIdx() {
    Appendable failling = new FailingAppendable(1);
    Readable inputs = new StringReader("C1 12 F101010");
    FreecellModel<Card> model = new SimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model, inputs, failling);
    controller.playGame(model.getDeck(), 4, 1, false);
  }

  @Test(expected = IllegalStateException.class)
  public void faillingForQuit() {
    Appendable failling = new FailingAppendable(1);
    Readable inputs = new StringReader("q");
    FreecellModel<Card> model = new SimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model, inputs, failling);
    controller.playGame(model.getDeck(), 4, 1, false);
  }

  @Test
  public void SourceNotValidSinceLowerCase() {
    String expected = "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: A♦, 5♦, 9♦, K♦, 4♣, 8♣, Q♣, 3♠, 7♠, J♠, 2♥, 6♥, 10♥\n" +
            "C2: 2♦, 6♦, 10♦, A♣, 5♣, 9♣, K♣, 4♠, 8♠, Q♠, 3♥, 7♥, J♥\n" +
            "C3: 3♦, 7♦, J♦, 2♣, 6♣, 10♣, A♠, 5♠, 9♠, K♠, 4♥, 8♥, Q♥\n" +
            "C4: 4♦, 8♦, Q♦, 3♣, 7♣, J♣, 2♠, 6♠, 10♠, A♥, 5♥, 9♥, K♥\n" +
            "Invalid pile type !\n" + "Invalid pile type !\n" +
            "Invalid pile type !\n" + "Game quit prematurely.";
    Appendable text = new StringBuilder();
    Readable rd = new StringReader("c1 f1 o1 q");
    FreecellModel<Card> model = new SimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model, rd, text);
    controller.playGame(model.getDeck(), 4, 1, false);
    assertEquals(expected, text.toString());
  }

  @Test
  public void WinGame() {
    initOrderedDeck();
    win();
    Appendable text = new StringBuilder();
    Readable rd = new StringReader(winSequenece);
    FreecellModel<Card> model = new SimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model, rd, text);
    assertEquals(false, model.isGameOver());
    controller.playGame(orderedDeck, 4, 1, false);
    assertEquals(true, model.isGameOver());
  }

  @Test(expected = IllegalStateException.class)
  public void testNoSuchElement1() {
    Appendable text = new StringBuilder();
    Readable rd = new StringReader("C1");
    FreecellModel<Card> model = new SimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model, rd, text);
    controller.playGame(model.getDeck(), 4, 1, false);
  }

  @Test(expected = IllegalStateException.class)
  public void testNoSuchElement2() {
    Appendable text = new StringBuilder();
    Readable rd = new StringReader("C1 12");
    FreecellModel<Card> model = new SimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model, rd, text);
    controller.playGame(model.getDeck(), 4, 1, false);
  }

  @Test(expected = IllegalStateException.class)
  public void testNoSuchElement3() {
    Appendable text = new StringBuilder();
    Readable rd = new StringReader("C1 12 F1");
    FreecellModel<Card> model = new SimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model, rd, text);
    controller.playGame(model.getDeck(), 4, 1, false);
  }

  @Test
  public void WrongMove() {
    String expected = "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: A♦, 5♦, 9♦, K♦, 4♣, 8♣, Q♣, 3♠, 7♠, J♠, 2♥, 6♥, 10♥\n" +
            "C2: 2♦, 6♦, 10♦, A♣, 5♣, 9♣, K♣, 4♠, 8♠, Q♠, 3♥, 7♥, J♥\n" +
            "C3: 3♦, 7♦, J♦, 2♣, 6♣, 10♣, A♠, 5♠, 9♠, K♠, 4♥, 8♥, Q♥\n" +
            "C4: 4♦, 8♦, Q♦, 3♣, 7♣, J♣, 2♠, 6♠, 10♠, A♥, 5♥, 9♥, K♥\n" +
            "Invalid move !\n" + "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: A♦, 5♦, 9♦, K♦, 4♣, 8♣, Q♣, 3♠, 7♠, J♠, 2♥, 6♥, 10♥\n" +
            "C2: 2♦, 6♦, 10♦, A♣, 5♣, 9♣, K♣, 4♠, 8♠, Q♠, 3♥, 7♥, J♥\n" +
            "C3: 3♦, 7♦, J♦, 2♣, 6♣, 10♣, A♠, 5♠, 9♠, K♠, 4♥, 8♥, Q♥\n" +
            "C4: 4♦, 8♦, Q♦, 3♣, 7♣, J♣, 2♠, 6♠, 10♠, A♥, 5♥, 9♥, K♥\n" +
            "Game quit prematurely.";
    Appendable text = new StringBuilder();
    Readable rd = new StringReader("C1 13 F1 Q");
    FreecellModel<Card> model = new SimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model, rd, text);
    controller.playGame(model.getDeck(), 4, 1, false);
    assertEquals(expected, text.toString());
  }

  @Test
  public void WrongMoveNegativeIndex() {
    String expected = "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: A♦, 5♦, 9♦, K♦, 4♣, 8♣, Q♣, 3♠, 7♠, J♠, 2♥, 6♥, 10♥\n" +
            "C2: 2♦, 6♦, 10♦, A♣, 5♣, 9♣, K♣, 4♠, 8♠, Q♠, 3♥, 7♥, J♥\n" +
            "C3: 3♦, 7♦, J♦, 2♣, 6♣, 10♣, A♠, 5♠, 9♠, K♠, 4♥, 8♥, Q♥\n" +
            "C4: 4♦, 8♦, Q♦, 3♣, 7♣, J♣, 2♠, 6♠, 10♠, A♥, 5♥, 9♥, K♥\n" +
            "Invalid move !\n" + "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: A♦, 5♦, 9♦, K♦, 4♣, 8♣, Q♣, 3♠, 7♠, J♠, 2♥, 6♥, 10♥\n" +
            "C2: 2♦, 6♦, 10♦, A♣, 5♣, 9♣, K♣, 4♠, 8♠, Q♠, 3♥, 7♥, J♥\n" +
            "C3: 3♦, 7♦, J♦, 2♣, 6♣, 10♣, A♠, 5♠, 9♠, K♠, 4♥, 8♥, Q♥\n" +
            "C4: 4♦, 8♦, Q♦, 3♣, 7♣, J♣, 2♠, 6♠, 10♠, A♥, 5♥, 9♥, K♥\n" +
            "Game quit prematurely.";
    Appendable text = new StringBuilder();
    Readable rd = new StringReader("C1 -13 F1 Q");
    FreecellModel<Card> model = new SimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model, rd, text);
    controller.playGame(model.getDeck(), 4, 1, false);
    assertEquals(expected, text.toString());
  }

  @Test
  public void WrongMoveNegativeIndexSource() {
    String expected = "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: A♦, 5♦, 9♦, K♦, 4♣, 8♣, Q♣, 3♠, 7♠, J♠, 2♥, 6♥, 10♥\n" +
            "C2: 2♦, 6♦, 10♦, A♣, 5♣, 9♣, K♣, 4♠, 8♠, Q♠, 3♥, 7♥, J♥\n" +
            "C3: 3♦, 7♦, J♦, 2♣, 6♣, 10♣, A♠, 5♠, 9♠, K♠, 4♥, 8♥, Q♥\n" +
            "C4: 4♦, 8♦, Q♦, 3♣, 7♣, J♣, 2♠, 6♠, 10♠, A♥, 5♥, 9♥, K♥\n" +
            "Invalid move !\n" + "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: A♦, 5♦, 9♦, K♦, 4♣, 8♣, Q♣, 3♠, 7♠, J♠, 2♥, 6♥, 10♥\n" +
            "C2: 2♦, 6♦, 10♦, A♣, 5♣, 9♣, K♣, 4♠, 8♠, Q♠, 3♥, 7♥, J♥\n" +
            "C3: 3♦, 7♦, J♦, 2♣, 6♣, 10♣, A♠, 5♠, 9♠, K♠, 4♥, 8♥, Q♥\n" +
            "C4: 4♦, 8♦, Q♦, 3♣, 7♣, J♣, 2♠, 6♠, 10♠, A♥, 5♥, 9♥, K♥\n" +
            "Game quit prematurely.";
    Appendable text = new StringBuilder();
    Readable rd = new StringReader("C1 13 F-1 Q");
    FreecellModel<Card> model = new SimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model, rd, text);
    controller.playGame(model.getDeck(), 4, 1, false);
    assertEquals(expected, text.toString());
  }

  @Test
  public void InvalidCascadeToCascade() {
    initOrderedDeck();
    String expected = "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: A♦, 5♦, 9♦, K♦, 4♣, 8♣, Q♣, 3♠, 7♠, J♠, 2♥, 6♥, 10♥\n" +
            "C2: 2♦, 6♦, 10♦, A♣, 5♣, 9♣, K♣, 4♠, 8♠, Q♠, 3♥, 7♥, J♥\n" +
            "C3: 3♦, 7♦, J♦, 2♣, 6♣, 10♣, A♠, 5♠, 9♠, K♠, 4♥, 8♥, Q♥\n" +
            "C4: 4♦, 8♦, Q♦, 3♣, 7♣, J♣, 2♠, 6♠, 10♠, A♥, 5♥, 9♥, K♥\n" +
            "Invalid move !\n" + "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: A♦, 5♦, 9♦, K♦, 4♣, 8♣, Q♣, 3♠, 7♠, J♠, 2♥, 6♥, 10♥\n" +
            "C2: 2♦, 6♦, 10♦, A♣, 5♣, 9♣, K♣, 4♠, 8♠, Q♠, 3♥, 7♥, J♥\n" +
            "C3: 3♦, 7♦, J♦, 2♣, 6♣, 10♣, A♠, 5♠, 9♠, K♠, 4♥, 8♥, Q♥\n" +
            "C4: 4♦, 8♦, Q♦, 3♣, 7♣, J♣, 2♠, 6♠, 10♠, A♥, 5♥, 9♥, K♥\n" +
            "Game quit prematurely.";
    Appendable text = new StringBuilder();
    Readable rd = new StringReader("C1 13 C2 Q");
    FreecellModel<Card> model = new SimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model, rd, text);
    controller.playGame(model.getDeck(), 4, 1, false);
    assertEquals(expected, text.toString());
  }

  @Test
  public void MoveIntoNoneEmptyOpen() {
    initOrderedDeck();
    String expected = "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" +
            "C1: A♦, 5♦, 9♦, K♦, 4♣, 8♣, Q♣, 3♠, 7♠, J♠, 2♥, 6♥, 10♥\n" +
            "C2: 2♦, 6♦, 10♦, A♣, 5♣, 9♣, K♣, 4♠, 8♠, Q♠, 3♥, 7♥, J♥\n" +
            "C3: 3♦, 7♦, J♦, 2♣, 6♣, 10♣, A♠, 5♠, 9♠, K♠, 4♥, 8♥, Q♥\n" +
            "C4: 4♦, 8♦, Q♦, 3♣, 7♣, J♣, 2♠, 6♠, 10♠, A♥, 5♥, 9♥, K♥\n" +
            "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1: 10♥\n" +
            "C1: A♦, 5♦, 9♦, K♦, 4♣, 8♣, Q♣, 3♠, 7♠, J♠, 2♥, 6♥\n" +
            "C2: 2♦, 6♦, 10♦, A♣, 5♣, 9♣, K♣, 4♠, 8♠, Q♠, 3♥, 7♥, J♥\n" +
            "C3: 3♦, 7♦, J♦, 2♣, 6♣, 10♣, A♠, 5♠, 9♠, K♠, 4♥, 8♥, Q♥\n" +
            "C4: 4♦, 8♦, Q♦, 3♣, 7♣, J♣, 2♠, 6♠, 10♠, A♥, 5♥, 9♥, K♥\n" +
            "Invalid move !\n" + "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1: 10♥\n" +
            "C1: A♦, 5♦, 9♦, K♦, 4♣, 8♣, Q♣, 3♠, 7♠, J♠, 2♥, 6♥\n" +
            "C2: 2♦, 6♦, 10♦, A♣, 5♣, 9♣, K♣, 4♠, 8♠, Q♠, 3♥, 7♥, J♥\n" +
            "C3: 3♦, 7♦, J♦, 2♣, 6♣, 10♣, A♠, 5♠, 9♠, K♠, 4♥, 8♥, Q♥\n" +
            "C4: 4♦, 8♦, Q♦, 3♣, 7♣, J♣, 2♠, 6♠, 10♠, A♥, 5♥, 9♥, K♥\n" +
            "Game quit prematurely.";
    Appendable text = new StringBuilder();
    Readable rd = new StringReader("C1 13 O1 C1 12 O1 Q");
    FreecellModel<Card> model = new SimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model, rd, text);
    controller.playGame(model.getDeck(), 4, 1, false);
    assertEquals(expected, text.toString());
  }

  @Test
  public void StopAcceptingAfterWin() {
    initOrderedDeck();
    win();
    Appendable text = new StringBuilder();
    Readable rd = new StringReader(winSequenece + " fewiewhiehfi");
    FreecellModel<Card> model = new SimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model, rd, text);
    controller.playGame(orderedDeck, 4, 1, false);
    assertEquals(false, text.toString().contains("Invalid pile type !\n"));
  }

  @Test
  public void voidInvalidBuild() {
    initOrderedDeck();
    String expected = "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" + "O2:\n" + "O3:\n" +
            "O4:\n" + "C1: A♦, 5♦, 9♦, K♦, 4♣, 8♣, Q♣, 3♠, 7♠, J♠, 2♥, 6♥, 10♥\n" +
            "C2: 2♦, 6♦, 10♦, A♣, 5♣, 9♣, K♣, 4♠, 8♠, Q♠, 3♥, 7♥, J♥\n" +
            "C3: 3♦, 7♦, J♦, 2♣, 6♣, 10♣, A♠, 5♠, 9♠, K♠, 4♥, 8♥, Q♥\n" +
            "C4: 4♦, 8♦, Q♦, 3♣, 7♣, J♣, 2♠, 6♠, 10♠, A♥, 5♥, 9♥, K♥\n" +
            "Invalid move !\n" + "F1:\n" + "F2:\n" + "F3:\n" + "F4:\n" + "O1:\n" + "O2:\n" +
            "O3:\n" + "O4:\n" + "C1: A♦, 5♦, 9♦, K♦, 4♣, 8♣, Q♣, 3♠, 7♠, J♠, 2♥, 6♥, 10♥\n" +
            "C2: 2♦, 6♦, 10♦, A♣, 5♣, 9♣, K♣, 4♠, 8♠, Q♠, 3♥, 7♥, J♥\n" +
            "C3: 3♦, 7♦, J♦, 2♣, 6♣, 10♣, A♠, 5♠, 9♠, K♠, 4♥, 8♥, Q♥\n" +
            "C4: 4♦, 8♦, Q♦, 3♣, 7♣, J♣, 2♠, 6♠, 10♠, A♥, 5♥, 9♥, K♥\n" +
            "Game quit prematurely.";
    Appendable text = new StringBuilder();
    Readable rd = new StringReader("C1 8 F1 Q");
    FreecellModel<Card> model = new MultiMoveSimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model, rd, text);
    controller.playGame(model.getDeck(), 4, 4, false);
    assertEquals(expected, text.toString());
  }

  @Test
  public void NotEnoughEmptySpots() {
    initOrderedDeck();
    String expected = "F1: A♦\n" + "F2: A♠, 2♠\n" + "F3: A♥, 2♥, 3♥\n" + "F4:\n" + "O1:\n" +
            "C1: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♠, 2♦, A♣\n" +
            "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠\n" +
            "C3: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣\n" +
            "C4: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦\n" + "Invalid move !\n" +
            "F1: A♦\n" + "F2: A♠, 2♠\n" + "F3: A♥, 2♥, 3♥\n" + "F4:\n" + "O1:\n" +
            "C1: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♠, 2♦, A♣\n" +
            "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠\n" +
            "C3: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣\n" +
            "C4: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦\n" +
            "Game quit prematurely.";
    String set_up = "C4 13 F1 C2 13 F2 C2 12 F2 C1 13 F3 C1 12 F3 C1 11 F3 ";
    String making_build = "C2 11 C1 C4 12 C1 C3 13 C1 ";
    Appendable text = new StringBuilder();
    Readable rd = new StringReader(set_up + making_build + "C1 10 F1 Q");
    FreecellModel<Card> model = new MultiMoveSimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model, rd, text);
    controller.playGame(orderedDeck, 4, 1, false);
    assertEquals(expected, text.toString().substring(2176));
  }

  @Test
  public void MovingBuild() {
    initOrderedDeck();
    String expected = "F1: A♦\n" + "F2: A♠, 2♠\n" + "F3: A♥, 2♥, 3♥\n" + "F4:\n" +
            "O1: 4♠\n" + "O2:\n" + "O3:\n" + "O4:\n" +
            "C1: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥\n" +
            "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♥, 3♠, 2♦, A♣\n" +
            "C3: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣\n" +
            "C4: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦\n" +
            "Game quit prematurely.";
    String set_up = "C4 13 F1 C2 13 F2 C2 12 F2 C1 13 F3 C1 12 F3 C1 11 F3 ";
    String making_build = "C2 11 C1 C4 12 C1 C3 13 C1 C2 10 O1 ";
    Appendable text = new StringBuilder();
    Readable rd = new StringReader(set_up + making_build + "C1 10 C2 Q");
    FreecellModel<Card> model = new MultiMoveSimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model, rd, text);
    controller.playGame(orderedDeck, 4, 4, false);
    assertEquals(expected, text.toString().substring(2789));
  }

  @Test
  public void MovingBuildInvalidDestinationOpen() {
    initOrderedDeck();
    String expected = "Invalid move !\n" + "F1: A♦\n" + "F2: A♠, 2♠\n" + "F3: A♥, 2♥, 3♥\n" +
            "F4:\n" + "O1: 4♠\n" + "O2:\n" + "O3:\n" + "O4:\n" +
            "C1: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♠, 2♦, A♣\n" +
            "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠\n" +
            "C3: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣\n" +
            "C4: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦\n" +
            "Game quit prematurely.";
    String set_up = "C4 13 F1 C2 13 F2 C2 12 F2 C1 13 F3 C1 12 F3 C1 11 F3 ";
    String making_build = "C2 11 C1 C4 12 C1 C3 13 C1 C2 10 O1 ";
    Appendable text = new StringBuilder();
    Readable rd = new StringReader(set_up + making_build + "C1 10 O2 Q");
    FreecellModel<Card> model = new MultiMoveSimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model, rd, text);
    controller.playGame(orderedDeck, 4, 4, false);
    assertEquals(expected, text.toString().substring(2789));
  }

  @Test
  public void MovingBuildInvalidDestinationFoundation() {
    initOrderedDeck();
    String expected = "Invalid move !\n" + "F1: A♦\n" + "F2: A♠, 2♠\n" + "F3: A♥, 2♥, 3♥\n" +
            "F4:\n" + "O1: 4♠\n" + "O2:\n" + "O3:\n" + "O4:\n" +
            "C1: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♠, 2♦, A♣\n" +
            "C2: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠\n" +
            "C3: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣\n" +
            "C4: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦\n" +
            "Game quit prematurely.";
    String set_up = "C4 13 F1 C2 13 F2 C2 12 F2 C1 13 F3 C1 12 F3 C1 11 F3 ";
    String making_build = "C2 11 C1 C4 12 C1 C3 13 C1 C2 10 O1 ";
    Appendable text = new StringBuilder();
    Readable rd = new StringReader(set_up + making_build + "C1 10 F2 Q");
    FreecellModel<Card> model = new MultiMoveSimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model, rd, text);
    controller.playGame(orderedDeck, 4, 4, false);
    assertEquals(expected, text.toString().substring(2789));
  }

  @Test
  public void completeGameWithInvalidInputs() {
    initOrderedDeck();
    halfwin();
    Appendable text = new StringBuilder();
    Readable rd = new StringReader("ufehuwfhuhwu " + winSequenece);
    FreecellModel<Card> model = new SimpleFreecellModel();
    SimpleFreecellController controller = new SimpleFreecellController(model, rd, text);
    assertEquals(false, model.isGameOver());
    controller.playGame(orderedDeck, 4, 1, false);
    assertEquals(true, model.isGameOver());
  }
}
