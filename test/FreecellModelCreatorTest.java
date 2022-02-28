import org.junit.Test;

import cs3500.freecell.model.Card;
import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.FreecellModelCreator;
import cs3500.freecell.model.SimpleFreecellModel;
import cs3500.freecell.model.multimove.MultiMoveSimpleFreecellModel;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the creator class. Checks that the correct types are being returns by the class.
 */
public class FreecellModelCreatorTest {

  private FreecellModelCreator.GameType single = FreecellModelCreator.GameType.SINGLEMOVE;
  private FreecellModelCreator.GameType multi = FreecellModelCreator.GameType.MULTIMOVE;

  @Test
  public void SingleGameType() {
    FreecellModel<Card> model = FreecellModelCreator.create(single);
    assertEquals(true, model instanceof SimpleFreecellModel);
    assertEquals(false, model instanceof MultiMoveSimpleFreecellModel);
  }

  @Test
  public void MultiGameType() {
    FreecellModel<Card> model = FreecellModelCreator.create(multi);
    assertEquals(true, model instanceof SimpleFreecellModel);
    assertEquals(true, model instanceof MultiMoveSimpleFreecellModel);
  }

  @Test(expected = IllegalArgumentException.class)
  public void NullType() {
    FreecellModel<Card> model = FreecellModelCreator.create(null);
  }
}