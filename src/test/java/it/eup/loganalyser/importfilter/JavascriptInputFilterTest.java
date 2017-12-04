package it.eup.loganalyser.importfilter;

import it.eup.loganalyser.entity.LogDataRow;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class JavascriptInputFilterTest {

  LogDataRow emptyLogDataRow;

  @Before
  public void setup() {
    emptyLogDataRow = new LogDataRow();
  }

  @Test
  public void testScriptIsParsed() throws Exception {
    Filterable filter = JavascriptInputFilterFactory.createNew("return true");
    Assert.assertTrue(filter.isValid(emptyLogDataRow));
  }

  @Test
  public void nonEmptyRefererIsDetected() throws Exception {
    emptyLogDataRow.setReferer("http://ebay/test");

    Filterable filter = JavascriptInputFilterFactory.createNew("return row.referer.indexOf('ebay') != -1");
    Assert.assertTrue(filter.isValid(emptyLogDataRow));
  }
}
