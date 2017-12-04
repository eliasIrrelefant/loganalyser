package it.eup.loganalyser.parser.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class BooleanConverterTest {

  BooleanConverter sut = new BooleanConverter();

  @Test
  public void minusIsConvertedToNull() throws Exception {
    assertNull(sut.convert("-"));
  }

  @Test
  public void yesIsConvertedToTrue() throws Exception {
    assertEquals(Boolean.TRUE, sut.convert("yes"));
  }
}
