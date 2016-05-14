package it.eup.loganalyser.importfilter;

import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

import it.eup.loganalyser.entity.LogDataRow;

public class StringContainsFilter implements Filterable {

	private final Function<LogDataRow, String> valueAccessor;
	private final String contains;

	public StringContainsFilter(Function<LogDataRow, String> accessor, String contains) {
		this.valueAccessor = accessor;
		this.contains = contains;
	}

	@Override
	public boolean isValid(LogDataRow dataRow) {
		String value = valueAccessor.apply(dataRow);
		return StringUtils.contains(value, contains);
	}

}
