package it.eup.loganalyser.parser;

import com.google.common.base.Objects;
import it.eup.loganalyser.entity.LogDataRow;
import it.eup.loganalyser.logparser.LogFileTokenizer;
import it.eup.loganalyser.parser.converter.Converter;
import java.util.function.BiConsumer;

public class LambdaTokenParser<T> {

  private final BiConsumer<LogDataRow, T> valueConsumer;
  private final Converter<T> converter;
  private final String prefix;
  private final String suffix;
  private final String escapeChars;
  private final String name;

  public LambdaTokenParser(String name, BiConsumer<LogDataRow, T> valueConsumer, Converter<T> converter,
      String prefix, String suffix, String escapeChars) {
    this.valueConsumer = valueConsumer;
    this.converter = converter;
    this.prefix = Objects.firstNonNull(prefix, "");
    this.suffix = Objects.firstNonNull(suffix, "");
    this.escapeChars = escapeChars;
    this.name = name;
  }

  public void parse(LogDataRow dataRow, LogFileTokenizer tokenizer) {
    String token = tokenizer.next(getPrefix(), getSuffix(), getEscapeChars());

    valueConsumer.accept(dataRow, converter.convert(token));
  }

  public String getPrefix() {
    return prefix;
  }

  public String getSuffix() {
    return suffix;
  }

  public String getEscapeChars() {
    return escapeChars;
  }

  public String getName() {
    return name;
  }
}
