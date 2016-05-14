package it.eup.loganalyser.importfilter;

import java.util.function.Function;
import java.util.regex.Pattern;

import it.eup.loganalyser.entity.LogDataRow;

public class StringMatchesFilter implements Filterable {

	private final Function<LogDataRow, String> valueAccessor;
	private final String pattern;

	public StringMatchesFilter(Function<LogDataRow, String> accessor, String pattern) {
		this.valueAccessor = accessor;
		this.pattern = pattern;
	}

	@Override
	public boolean isValid(LogDataRow dataRow) {
		String value = valueAccessor.apply(dataRow);
		return Pattern.matches(pattern, value);
	}

}
