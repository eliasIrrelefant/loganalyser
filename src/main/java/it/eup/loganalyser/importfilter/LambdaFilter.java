package it.eup.loganalyser.importfilter;

import it.eup.loganalyser.entity.LogDataRow;
import java.util.function.Predicate;

public class LambdaFilter implements Filterable {

  final Predicate<LogDataRow> predicate;

  public LambdaFilter(Predicate<LogDataRow> predicate) {
    super();
    this.predicate = predicate;
  }

  @Override
  public boolean isValid(LogDataRow dataRow) {
    return predicate.test(dataRow);
  }

}
