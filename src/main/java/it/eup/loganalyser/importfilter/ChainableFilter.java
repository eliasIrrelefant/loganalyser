package it.eup.loganalyser.importfilter;

import java.util.ArrayList;
import java.util.List;

import it.eup.loganalyser.entity.LogDataRow;

public class ChainableFilter implements Filterable {

	private List<Filterable> filters = new ArrayList<Filterable>();

	@Override
	public boolean isValid(LogDataRow dataRow) {
		for (Filterable filter : filters) {

			if (filter.isValid(dataRow) == false) {
				return false;
			}

		}
		return true;
	}

	public void add(Filterable filterable) {
		filters.add(filterable);
	}
}
