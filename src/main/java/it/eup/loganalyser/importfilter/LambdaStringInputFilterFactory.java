package it.eup.loganalyser.importfilter;

import it.eup.loganalyser.entity.LogDataRow;
import java.util.function.Predicate;
import pl.joegreen.lambdaFromString.LambdaFactory;
import pl.joegreen.lambdaFromString.LambdaFactoryConfiguration;
import pl.joegreen.lambdaFromString.TypeReference;

public class LambdaStringInputFilterFactory {

  public static Filterable createNew(String script) {
    LambdaFactory lambdaFactory =
        LambdaFactory.get(LambdaFactoryConfiguration.get().withImports("org.apache.commons.lang3.*", "it.eup.loganalyser.entity.*"));

    Predicate<LogDataRow> predicate = lambdaFactory.createLambdaUnchecked("row -> " + script, new TypeReference<Predicate<LogDataRow>>() {
    });

    return new LambdaFilter(predicate);
  }

}
