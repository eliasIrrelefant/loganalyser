package it.eup.loganalyser.logparser;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class TokenHelper {

  private final String line;

  public TokenHelper(String line) {
    super();
    this.line = line;
  }

  public int findNextDelimiter(int start, BiPredicate<String, Integer> delimiterPredicate, Predicate<Character> escapeCharPredicate) {
    for (int i = start; i < line.length(); ++i) {
      char currentChar = line.charAt(i);

      if (escapeCharPredicate.test(currentChar)) {
        i = i + 2;
        continue;
      }

      if (delimiterPredicate.test(line, i)) {
        return i;
      }
    }

    if (delimiterPredicate.test(line, line.length())) {
      return line.length();
    } else {
      return -1;
    }
  }
}
