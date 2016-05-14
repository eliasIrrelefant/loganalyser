package it.eup.loganalyser.parser.converter;

import it.eup.loganalyser.logparser.ParserHelper;

public class IntegerConverter implements Converter<Integer> {

	@Override
	public Integer convert(String string) {
		return new ParserHelper().parseInt(string);
	}
}
