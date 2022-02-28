import org.junit.Test;

import java.io.IOException;

import cs3500.freecell.model.Card;
import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.PileType;
import cs3500.freecell.model.SimpleFreecellModel;
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

  private void init() {
    this.model = new SimpleFreecellModel();
    this.view = new FreecellTextView(model);
  }

  private void initView() {
    this.ap = new StringBuilder();
    this.model = new SimpleFreecellModel();
    this.view = new FreecellTextView(model, ap);
  }

  private void initModel() {
    this.model.startGame(model.getDeck(),
            4, 4, false);
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
}
