package cs3500.freecell.model;

import cs3500.freecell.model.multimove.MultiMoveSimpleFreecellModel;

/**
 * To represent a factory for making distinct type of freecell games.
 */
public class FreecellModelCreator {

  /**
   * To represent the types of games that exists for freecell.
   */
  public enum GameType {
    SINGLEMOVE, MULTIMOVE;

  }

  /**
   * Makes a game of freecell depending of the typoe the user wants to play.
   *
   * @param type type of freecell game that the user wants to play (multi or single).
   * @return freecell game that works according to the input type.
   */
  public static FreecellModel<Card> create(GameType type) {
    if (type == null) {
      throw new IllegalArgumentException("Type can't be null.");
    }
    else if (type == GameType.SINGLEMOVE) {
      return new SimpleFreecellModel();
    }
    else {
      return new MultiMoveSimpleFreecellModel();
    }
  }
}
