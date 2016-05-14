package it.eup.loganalyser.parser.converter;

import java.util.Date;

import it.eup.loganalyser.logparser.ParserHelper;

public class DateConverter implements Converter<Date> {

	@Override
	public Date convert(String dateString) {
		return new ParserHelper().parseDate(dateString);
				
	}
}
