package it.eup.loganalyser.logparser;

import java.util.function.Predicate;

public class DefaultEscapeCharDetector implements Predicate<Character> {

  @Override
  public boolean test(Character t) {
    return (t == '\\');
  }

}
