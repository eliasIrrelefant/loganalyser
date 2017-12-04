package it.eup.loganalyser.importfilter;

import it.eup.loganalyser.entity.LogDataRow;
import java.util.function.Function;

public class BooleanFilter implements Filterable {

  private final Function<LogDataRow, Boolean> valueAccessor;
  private final Boolean expectedValue;

  public BooleanFilter(Function<LogDataRow, Boolean> accessor, Boolean exprectedValue) {
    this.valueAccessor = accessor;
    this.expectedValue = exprectedValue;
  }

  @Override
  public boolean isValid(LogDataRow dataRow) {
    Boolean value = valueAccessor.apply(dataRow);
    return expectedValue.equals(value);
  }

}
