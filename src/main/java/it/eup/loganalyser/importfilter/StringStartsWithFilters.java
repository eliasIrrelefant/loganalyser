package it.eup.loganalyser.importfilter;

import it.eup.loganalyser.entity.LogDataRow;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;

public class StringStartsWithFilters implements Filterable {

  private final Function<LogDataRow, String> valueAccessor;
  private final String startsWith;

  public StringStartsWithFilters(Function<LogDataRow, String> accessor, String startsWith) {
    this.valueAccessor = accessor;
    this.startsWith = startsWith;
  }

  @Override
  public boolean isValid(LogDataRow dataRow) {
    String value = valueAccessor.apply(dataRow);
    return StringUtils.startsWith(value, startsWith);
  }

}
