package it.eup.loganalyser.parser.converter;

import org.apache.commons.lang3.StringUtils;

public class StringConverter implements Converter<String> {

  private Integer maxLength;

  public StringConverter() {
  }

  public StringConverter(Integer maxLength) {
    super();
    this.maxLength = maxLength;
  }

  @Override
  public String convert(String string) {
    if (maxLength != null && maxLength >= 0) {
      return StringUtils.substring(string, 0, maxLength);
    }

    return string;
  }
}
