package it.eup.loganalyser.importfilter;

import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

import it.eup.loganalyser.entity.LogDataRow;

public class StringInputFilter implements InputFilterFactory<String> {

	private final Function<LogDataRow, String> valueAccessor;

	public StringInputFilter(Function<LogDataRow, String> valueAccessor) {
		this.valueAccessor = valueAccessor;
	}
	
	@Override
	public Filterable createFilter(String choice, String input) {
		if (StringUtils.equals(FilterOptions.CONTAINS, choice)) {
			return new StringContainsFilter(valueAccessor, input);
		} else if (StringUtils.equals(FilterOptions.CONTAINS_IGNORE_CASE, choice)) {
			return new StringContainsIgnoreCaseFilter(valueAccessor, input);
		} else if (StringUtils.equals(FilterOptions.EXACT, choice)) {
			return new StringEqualsFilter(valueAccessor, input);
		} else if (StringUtils.equals(FilterOptions.MATCHES, choice)) {
			return new StringMatchesFilter(valueAccessor, input);
		} else if (StringUtils.equals(FilterOptions.STARTS_WITH, choice)) {
			return new StringStartsWithFilters(valueAccessor, input); 
		}
		
		throw new IllegalArgumentException("Auswahl " + choice + " wird nicht unterstützt.");
	}

}
