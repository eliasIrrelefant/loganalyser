package it.eup.loganalyser.importfilter;

import it.eup.loganalyser.entity.LogDataRow;

public interface Filterable {

	boolean isValid(LogDataRow dataRow);
}
