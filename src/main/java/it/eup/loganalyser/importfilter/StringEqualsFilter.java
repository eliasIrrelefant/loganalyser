package it.eup.loganalyser.importfilter;

import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

import it.eup.loganalyser.entity.LogDataRow;

public class StringEqualsFilter implements Filterable {

	private final Function<LogDataRow, String> valueAccessor;
	private final String expected;

	public StringEqualsFilter(Function<LogDataRow, String> accessor, String expected) {
		this.valueAccessor = accessor;
		this.expected = expected;
	}

	@Override
	public boolean isValid(LogDataRow dataRow) {
		String value = valueAccessor.apply(dataRow);
		return StringUtils.equals(value, expected);
	}

}
