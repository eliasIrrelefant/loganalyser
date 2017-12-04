package it.eup.loganalyser.logparser;

import java.util.function.BiPredicate;
import org.apache.commons.lang3.StringUtils;

public class DefaultDelimiterDetector implements BiPredicate<String, Integer> {

  private final String delimiter;

  public DefaultDelimiterDetector(String delimiter) {
    this.delimiter = delimiter;
  }

  @Override
  public boolean test(String line, Integer start) {
    if (start + delimiter.length() > line.length()) {
      return StringUtils.isEmpty(delimiter);
    }

    for (int i = 0; i < delimiter.length(); ++i) {
      if (delimiter.charAt(i) != line.charAt(start + i)) {
        return false;
      }
    }

    if (line.length() == start + delimiter.length()) {
      return true;
    } else {
      return Character.isWhitespace(line.charAt(start + delimiter.length()));
    }
  }

}
