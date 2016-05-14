package it.eup.loganalyser.importfilter;

import it.eup.loganalyser.entity.LogDataRow;

public interface ValueAccessor<T> {

	T getValue(LogDataRow dataRow);
}
