package it.eup.loganalyser.logparser;

import it.eup.loganalyser.entity.LogDataRow;
import it.eup.loganalyser.parser.LambdaTokenParser;
import it.eup.loganalyser.parser.converter.BooleanConverter;
import it.eup.loganalyser.parser.converter.Converter;
import it.eup.loganalyser.parser.converter.DateConverter;
import it.eup.loganalyser.parser.converter.IntegerConverter;
import it.eup.loganalyser.parser.converter.StringConverter;
import java.util.Date;
import java.util.function.BiConsumer;

public class TokenBuilder<T> {

  public static TokenBuilder<String> newStringBuilder(String name) {
    return new TokenBuilder<String>(name).withConverter(new StringConverter());
  }

  public static TokenBuilder<Boolean> newBooleanBuilder(String name) {
    return new TokenBuilder<Boolean>(name).withConverter(new BooleanConverter());
  }

  public static <T> TokenBuilder<T> newBuilder(String name, Converter<T> converter) {
    return new TokenBuilder<T>(name).withConverter(converter);
  }

  public static TokenBuilder<Date> newRequestDateBuilder(String name) {
    return new TokenBuilder<Date>(name).enquotedWith("[", "]").withConverter(new DateConverter());
  }

  public static TokenBuilder<Date> newDateBuilder(String name) {
    return new TokenBuilder<Date>(name).withConverter(new DateConverter());
  }

  public static TokenBuilder<Integer> newIntegerBuilder(String name) {
    return new TokenBuilder<Integer>(name).withConverter(new IntegerConverter());
  }

  public static TokenBuilder<String> newIgnoreBuilder(String name) {
    return newStringBuilder(name).withValueConsumer((x, y) -> {
    });
  }

  private final String name;
  private String enquotePrefix = "";

  private String enquoteSuffix = "";

  private String escapeChars = "\\";
  private Converter<T> converter;

  private BiConsumer<LogDataRow, T> valueConsumer = (x, y) -> {
  };

  private TokenBuilder(String name) {
    this.name = name;
  }

  public LambdaTokenParser<T> build() {
    return new LambdaTokenParser<T>(name, valueConsumer, converter, enquotePrefix, enquoteSuffix, escapeChars);
  }

  public TokenBuilder<T> withDoubleQuotes() {
    enquotedWith("\"");
    return this;
  }

  public TokenBuilder<T> enquotedWith(String chars) {
    enquotedWith(chars, chars);
    return this;
  }

  public TokenBuilder<T> enquotedWith(String prefix, String suffix) {
    this.enquotePrefix = prefix + this.enquotePrefix;
    this.enquoteSuffix = this.enquoteSuffix + suffix;
    return this;
  }

  public String getName() {
    return name;
  }

  public TokenBuilder<T> withConverter(Converter<T> converter) {
    this.converter = converter;
    return this;
  }

  public TokenBuilder<T> withEscapeChars(String escapeChars) {
    this.escapeChars = escapeChars;
    return this;
  }

  public TokenBuilder<T> withPrefix(String prefix) {
    this.enquotePrefix = prefix;
    return this;
  }

  public TokenBuilder<T> withSuffix(String suffix) {
    this.enquoteSuffix = suffix;
    return this;
  }

  public TokenBuilder<T> withValueConsumer(BiConsumer<LogDataRow, T> valueConsumer) {
    this.valueConsumer = valueConsumer;
    return this;
  }
}
