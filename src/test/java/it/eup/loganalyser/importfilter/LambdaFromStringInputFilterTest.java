package it.eup.loganalyser.importfilter;

import it.eup.loganalyser.entity.LogDataRow;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class LambdaFromStringInputFilterTest {

  LogDataRow emptyLogDataRow;

  @Before
  public void setup() {
    emptyLogDataRow = new LogDataRow();
  }

  @Test
  public void testScriptIsParsed() throws Exception {
    Filterable filter = LambdaStringInputFilterFactory.createNew("true");
    Assert.assertTrue(filter.isValid(emptyLogDataRow));
  }

  @Test
  public void refererContainingEbayIsDetected() throws Exception {
    emptyLogDataRow.setReferer("http://ebay/test");

    Filterable filter = LambdaStringInputFilterFactory.createNew("StringUtils.contains(row.getReferer(), \"ebay\")");
    Assert.assertTrue(filter.isValid(emptyLogDataRow));
  }

  @Test
  public void refererNotContainingEbayIsNotDetected() throws Exception {
    emptyLogDataRow.setReferer("http://otherdomain.tld/test");

    Filterable filter = LambdaStringInputFilterFactory.createNew("StringUtils.contains(row.getReferer(), \"ebay\")");
    Assert.assertFalse(filter.isValid(emptyLogDataRow));
  }
}
