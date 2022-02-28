import java.io.IOException;

/**
 * To represent and appendable object that fails for testing purposes.
 */
public class FailingAppendable implements Appendable {
  int round;
  Appendable ignore;
  int currRound;

  FailingAppendable(int round) {
    currRound = 0;
    this.round = round;
    this.ignore = new StringBuilder();
  }

  @Override
  public Appendable append(CharSequence csq) throws IOException {
    if (currRound == round) {
      throw new IOException("Input/Output operation failed");
    }
    currRound += 1;
    return ignore;
  }

  @Override
  public Appendable append(CharSequence csq, int start, int end) throws IOException {
    if (currRound == round) {
      throw new IOException("Input/Output operation failed");
    }
    currRound += 1;
    return ignore;
  }

  @Override
  public Appendable append(char c) throws IOException {
    if (currRound == round) {
      throw new IOException("Input/Output operation failed");
    }
    currRound += 1;
    return ignore;
  }
}
