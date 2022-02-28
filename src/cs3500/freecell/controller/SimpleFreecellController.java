package cs3500.freecell.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Function;

import cs3500.freecell.model.Card;
import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.PileType;
import cs3500.freecell.view.FreecellTextView;
import cs3500.freecell.view.FreecellView;

/**
 * Creates a controller for the freecell game. It's in charge of reading inputs and
 * output data to view.
 */
public class SimpleFreecellController implements FreecellController<Card> {
  private final FreecellModel<Card> model;
  private final Readable rd;
  private final Appendable ap;
  private FreecellView view;
  private boolean activeGame;

  /**
   * To represent a controller for a game of freecell.
   *
   * @param model is the model of the freecell game.
   * @param rd    is the reader for user inputs.
   * @param ap    is the output location for the game.
   */
  public SimpleFreecellController(FreecellModel<Card> model, Readable rd, Appendable ap) {
    if (model == null || rd == null || ap == null) {
      throw new IllegalArgumentException("Arguments cannot be null !");
    }
    this.model = model;
    this.rd = rd;
    this.ap = ap;
    this.activeGame = false;
  }

  @Override
  public void playGame(List<Card> deck, int numCascades, int numOpens, boolean shuffle) {
    this.view = new FreecellTextView(model, ap);
    if (!callStartOnModel(deck, numCascades, numOpens, shuffle)) {
      return;
    }

    activeGame = true;
    Scanner scan = new Scanner(rd);
    while (activeGame) {
      // checks if the game has ended.
      if (model.isGameOver()) {
        try {
          view.renderBoard();
          view.renderMessage("Game over.");
        } catch (IOException e) {
          throw new IllegalStateException("Error while rendering board.");
        }
        scan.close();
        return;
      }

      // render the board.
      try {
        view.renderBoard();
      } catch (IOException e) {
        throw new IllegalStateException("Error while rendering message.");
      }

      Map<InputType, String> validateInputs = getInput(scan);
      if (validateInputs == null) {
        return;
      }
      
      PileType[] types = getTypes(validateInputs);

      // All inputs are already validated, so parsing and conversions are safe.
      PileType source = types[0];
      PileType destination = types[1];
      int sourcePileIdx = Integer.parseInt(validateInputs.get(InputType.SOURCE).substring(1)) ;
      int destPileIdx = Integer.parseInt(validateInputs.get(InputType.DESTINATION).substring(1));
      int cardIndex = Integer.parseInt(validateInputs.get(InputType.CARDIDX));

      // indexes are index one bigger than the model's
      sourcePileIdx -= 1;
      destPileIdx -= 1;
      cardIndex -= 1;

      // Try making a move.
      try {
        model.move(source, sourcePileIdx, cardIndex, destination, destPileIdx);
      } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
        try {
          view.renderMessage("Invalid move !\n");
        } catch (IOException ex) {
          throw new IllegalStateException("Error while rendering message.");
        }
      }
    }
  }

  ///////////////// CHECKS FOR VALID GAME START /////////////////

  /**
   * Start the model and checks for illegal arguments.
   *
   * @param deck        is the deck used in the model.
   * @param numCascades is the number of cascade piles.
   * @param numOpens    is the number of open piles.
   * @param shuffle     is whether the deck is being shuffled.
   * @return true if the start gamne conditions are permitted.
   * @throws IllegalArgumentException if deck is null or any parameter is unsupported by the model.
   */
  private boolean callStartOnModel(List<Card> deck, int numCascades,
                                   int numOpens, boolean shuffle) {
    if (deck == null) {
      throw new IllegalArgumentException("Deck can't be null !");
    }
    try {
      model.startGame(deck, numCascades, numOpens, shuffle);
      return true;
    } catch (Exception e) {
      try {
        view.renderMessage("Could not start game.");
      } catch (IOException ex) {
        throw new IllegalStateException("Error while rendering message.");
      }
      return false;
    }
  }

  ///////////// METHODS THAT CHECKS FOR QUIT BY USER ///////////////

  /**
   * Checks if the user is trying to quit the game.
   *
   * @param input is the input by the user.
   * @return true if user is trying to quit the game (input is 'q' or 'Q').
   */
  private boolean checkForQuit(String input) {
    return (input.toLowerCase().equals("q"));
  }

  //////////////////// FUNCTION TO GET USER INPUTS FOR ALL TYPES //////////////////////

  /**
   * Determines if the inputs by the user are valid. If not valid it asks for more inputs until
   * the input is valid or the user wants to quit the game.
   *
   * @param scan is the scanner to get user inputs.
   */
  private Map<InputType, String> getInput(Scanner scan) {

    // Locations to store inputs based on InputType (Source, CardIdx, Destination).
    Map<InputType, Function<Scanner, String>> inputFunctions = new HashMap<>();
    Map<InputType, String> inputs = new HashMap<>();

    // Each function in the map gets the input based on the input type.
    inputFunctions.put(InputType.SOURCE, this::getPileInput);
    inputFunctions.put(InputType.CARDIDX, this::getCardIdxInput);
    inputFunctions.put(InputType.DESTINATION, this::getPileInput);

    // Put types in a list so we can loop.
    List<InputType> types = new ArrayList<>(Arrays.asList(InputType.SOURCE,
          InputType.CARDIDX, InputType.DESTINATION));

    // Loop and get each type of input (Source, CardIdx, Destination).
    for (InputType type : types) {
      String userResponse = inputFunctions.get(type).apply(scan);
      if (checkForQuit(userResponse)) {
        activeGame = false;
        try {
          view.renderMessage("Game quit prematurely.");
        } catch (IOException ex) {
          throw new IllegalStateException("Error while rendering message.");
        }
        return null;
      }
      inputs.put(type, userResponse); // Put valid input in map (validation happens in helpers).
    }
    return inputs;
  }

  /////////////// CHECKS THAT THE SOURCE/DESTINATION INPUT IS CORRECT ///////////////

  /**
   * Checks that the source is a valid pile to move cards from.
   *
   * @param scan is a scanner that takes in inputs by user.
   * @return valid string for move.
   */
  private String getPileInput(Scanner scan) {
    String input;
    try {
      input = scan.next();  // gets user input
    } catch (NoSuchElementException e) {
      throw new IllegalStateException("No input was given.");
    }
    // check for quit.
    if (checkForQuit(input)) {
      return input;
    }
    return checkForPileInput(scan, input); // finalize the input.
  }

  ///////////////////// CHECK THAT THE FIRST CHAR IS A VALID SOURCE ////////////////////////

  /**
   * Checks that the source pile is a valid pile.
   *
   * @param scan  scans for a user input.
   * @param input user's original input.
   * @return valid input or 'Q' or 'q' (q or Q stands for user wanting to quit).
   */
  private String checkForPileInput(Scanner scan, String input) {
    String inputFirstLetter = input.substring(0, 1);

    // checks for quitting
    if (input.toLowerCase().equals("q")) {
      return input;
    }
    // checks that the input has correct format (valid pile and pile index).
    if (inputFirstLetter.equals("C") || inputFirstLetter.equals("O")
            || inputFirstLetter.equals("F")) {
      return checkForIntegerInput(scan, input);
    }
    // repeat the method if input is inappropriate.
    else {
      try {
        view.renderMessage("Invalid pile type !\n");
      } catch (IOException e) {
        throw new IllegalStateException("Error while rendering message.");
      }
      try {
        input = scan.next();
      } catch (NoSuchElementException e) {
        throw new IllegalStateException("No input was given.");
      }

      return checkForPileInput(scan, input);
    }
  }

  ///////////////////// CHECKS THAT SOURCE INDEX IS CORRECT ////////////////////////

  /**
   * Checks that the second character in the user input is valid.
   *
   * @param scan     takes in user inputs using a scanner.
   * @param input    is the input by the user.
   * @return input by the user (only valid inputs or 'Q' or 'q').
   */
  private String checkForIntegerInput(Scanner scan, String input) {
    // divides the user input into substrings
    String firstChar = input.substring(0, 1);
    String secondChar = input.substring(1);

    // validate the index or check for quitting.
    String validatedAnswer = validateIndex(scan, secondChar);
    return (checkForQuit(validatedAnswer)) ? validatedAnswer : firstChar + validatedAnswer;
  }

  /////////////////// CHECKS THAT CARD IDX IS CORRECT ///////////////////////

  /**
   * Gets the card index of the card that the user wants to move.
   *
   * @param scan  scans for the user input.
   * @return string containing the card index being moved by user.
   */
  private String getCardIdxInput(Scanner scan) {
    String cardIndexInput;

    try {
      cardIndexInput = scan.next();
    } catch (NoSuchElementException e) {
      throw new IllegalStateException("No input was given 3.");
    }
    if (checkForQuit(cardIndexInput)) { // checks that the input is quit.
      return cardIndexInput;
    }
    return validateIndex(scan, cardIndexInput);
  }

  //////////////////////// HELPER METHODS /////////////////////////////

  /**
   * Gets the type for the source and destination piles from user input.
   *
   * @param inputs is the validated inputs by the user.
   * @return list with the pile types for the source and destination inputs.
   */
  private PileType[] getTypes(Map<InputType, String> inputs) {
    PileType[] sourceAndDestinationTypes = new PileType[2];
    String source = inputs.get(InputType.SOURCE).substring(0, 1);
    String destination = inputs.get(InputType.DESTINATION).substring(0, 1);
    sourceAndDestinationTypes[0] = (checkForPileTypes(source));
    sourceAndDestinationTypes[1] = (checkForPileTypes(destination));
    return sourceAndDestinationTypes;
  }

  /**
   * Gets the pileType according to character letter given.
   *
   * @param initial initial corresponding one of the pile types.
   * @return pileType according to the initial.
   */
  private PileType checkForPileTypes(String initial) {
    switch (initial) {
      case "O":
        return PileType.OPEN;
      case "C":
        return PileType.CASCADE;
      default:
        return PileType.FOUNDATION;
    }
  }

  /**
   * Validates tha the (index - 1) is between 0 and the given upper limits.
   *
   * @param scan       scans for the user's inputs.
   * @param index      is the index of a pile or a card.
   * @return index of the card/pile or 'Q' or 'q'.
   */
  private String validateIndex(Scanner scan, String index) {
    // parsing the integer and checking its valid.
    try {
      Integer.parseInt(index);
      return index;
    } catch (NumberFormatException e) {
      try {
        view.renderMessage("Index must be an integer !\n");
      } catch (IOException ex) {
        throw new IllegalStateException("Error while rendering message.");
      }
      try {
        index = scan.next();
      } catch (NoSuchElementException exc) {
        throw new IllegalStateException("No input was given.");
      }
      if (checkForQuit(index)) {
        return index;
      }
    }
    return validateIndex(scan, index);
  }
}


