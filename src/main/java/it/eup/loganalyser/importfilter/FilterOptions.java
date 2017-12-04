package it.eup.loganalyser.importfilter;

import java.util.Arrays;
import java.util.List;

public class FilterOptions {

  public static final String ANY = "beliebig";
  public static final String STARTS_WITH = "beginnt mit";
  public static final String EXACT = "entspricht";
  public static final String IS_NOT = "entspricht nicht";
  public static final String CONTAINS = "enthält";
  public static final String CONTAINS_IGNORE_CASE = "enthält (ignore case)";
  public static final String MATCHES = "matches";

  public static final String YES = "wahr";
  public static final String NO = "falsch";

  public static final List<String> ALL_BOOLEAN_OPTIONS = Arrays.asList(ANY, YES, NO);
  public static final List<String> ALL_INTEGER_OPTIONS = Arrays.asList(ANY, YES, NO);
  public static final List<String> ALL_STRING_FILTER_OPTIONS =
      Arrays.asList(ANY, EXACT, STARTS_WITH, CONTAINS, CONTAINS_IGNORE_CASE, MATCHES);
}
