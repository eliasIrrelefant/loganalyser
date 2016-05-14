package it.eup.loganalyser.parser.converter;

public interface Converter<T> {

	T convert(String string);
}
