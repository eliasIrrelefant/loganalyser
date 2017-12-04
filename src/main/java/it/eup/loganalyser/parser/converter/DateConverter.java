package it.eup.loganalyser.parser.converter;

import it.eup.loganalyser.logparser.ParserHelper;
import java.util.Date;

public class DateConverter implements Converter<Date> {

  @Override
  public Date convert(String dateString) {
    return new ParserHelper().parseDate(dateString);

  }
}
