package cs3500.freecell.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import cs3500.freecell.model.FreecellModelState;
import cs3500.freecell.model.PileType;

/**
 * To represent a text view of the game of FreeCell.
 */
public class FreecellTextView implements FreecellView {
  private final FreecellModelState<?> model;
  private Appendable destination;

  /**
   * To represent a text view with no controller.
   *
   * @param model is the freecell model that is being viewed.
   */
  public FreecellTextView(FreecellModelState<?> model) {
    this.model = model;
  }

  /**
   * To represent a text view with controller.
   *
   * @param model is the model being viewed.
   * @param dest is the destination to which the view is sent.
   */
  public FreecellTextView(FreecellModelState<?> model, Appendable dest) {
    this.model = model;
    this.destination = dest;
  }

  @Override
  public String toString() {

    if (model.getNumCascadePiles() == -1) {
      return "";
    }

    else {
      List<String> output = new ArrayList<>();
      int numOpenPiles = model.getNumOpenPiles();
      int numCascadePiles = model.getNumCascadePiles();

      List<String> foundationPileStrings = buildEntries(4, PileType.FOUNDATION,
              this.model::getNumCardsInFoundationPile,
              this.model::getFoundationCardAt);

      List<String> cascadePileStrings = buildEntries(numCascadePiles, PileType.CASCADE,
              this.model::getNumCardsInCascadePile,
              this.model::getCascadeCardAt);

      List<String> openPileStrings = buildEntries(numOpenPiles, PileType.OPEN,
              this.model::getNumCardsInOpenPile, (Integer pileIndex, Integer cardIndex) ->
                      this.model.getOpenCardAt(pileIndex));

      String foundationPileFormatting = String.join(System.lineSeparator(), foundationPileStrings);
      String cascadePileFormatting = String.join(System.lineSeparator(), cascadePileStrings);
      String openPileFormatting = String.join(System.lineSeparator(), openPileStrings);

      return foundationPileFormatting + "\n" + openPileFormatting + "\n" + cascadePileFormatting;
    }
  }

  @Override
  public void renderBoard() throws IOException {
    destination.append(this.toString() + "\n");
  }

  @Override
  public void renderMessage(String message) throws IOException {
    destination.append(message);
  }

  /**
   * Makes a list of strings where the strings are the cards of the corresponding pile.
   *
   * @param pileSize is the size of the pile being formatted.
   * @param type is the type of pile that is being formatted.
   * @param getCard is a function that gets the card at the current index.
   * @return list of strings containing the cards within each pile in the correct format.
   */
  private List<String> buildEntries(int pileSize, PileType type, Function<Integer, Integer> size,
                                    BiFunction<Integer, Integer, ?> getCard) {
    String fullLine = "";
    List<String> output = new ArrayList<>();
    for (int i = 0; i < pileSize; i++) {
      List<String> entry = new ArrayList<>();
      int numCardsInPile = size.apply(i);
      String root = detectType(type) + (i + 1) + ":";

      for (int j = 0; j < numCardsInPile; j++) {
        entry.add(getCard.apply(i, j).toString());
      }
      if (entry.size() == 0) {
        fullLine = root;
      }
      else {
        fullLine = root + " " + String.join(", ", entry);
      }
      output.add(fullLine);
    }

    return output;
  }

  /**
   * Gives the correct initial based on the pile type given.
   *
   * @param type is the pile type.
   * @return is the initial of the pile type as a string.
   */
  private String detectType(PileType type) {
    switch (type) {
      case CASCADE:
        return "C";
      case OPEN:
        return "O";
      default:
        return "F";
    }
  }
}