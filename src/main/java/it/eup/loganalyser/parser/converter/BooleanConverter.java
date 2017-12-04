package it.eup.loganalyser.parser.converter;

import org.apache.commons.lang3.BooleanUtils;

public class BooleanConverter implements Converter<Boolean> {

  @Override
  public Boolean convert(String string) {
    return BooleanUtils.toBooleanObject(string);
  }
}
