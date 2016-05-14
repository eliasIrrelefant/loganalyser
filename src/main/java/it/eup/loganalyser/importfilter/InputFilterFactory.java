package it.eup.loganalyser.importfilter;

public interface InputFilterFactory<T> {

	Filterable createFilter(String choice, String input);
}
