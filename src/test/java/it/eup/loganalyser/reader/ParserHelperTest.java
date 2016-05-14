package it.eup.loganalyser.reader;

import static junit.framework.Assert.assertSame;
import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import it.eup.loganalyser.logparser.ParserHelper;

public class ParserHelperTest {

	ParserHelper helper = new ParserHelper();
	
	@Test
	public void truncateReturnsInputWhenLengthNotExeeded() {
		String input = "test";
	
		assertSame(input, helper.truncate(input, 2048));
	}
	
	@Test
	public void truncateReturnsNewStringWithGivenLengthWhenLengthExeeded() {
		String input = "test123";
		assertEquals("tes", helper.truncate(input, 3));
	}
	
	@Test
	public void testParseDate() {
		String dateString = "07/May/2015:23:47:35 +0200";

		Date date = helper.parseDate(dateString);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		assertEquals(7, calendar.get(Calendar.DAY_OF_MONTH));
		assertEquals(4, calendar.get(Calendar.MONTH));
		assertEquals(2015, calendar.get(Calendar.YEAR));
		assertEquals(23, calendar.get(Calendar.HOUR_OF_DAY));
		assertEquals(47, calendar.get(Calendar.MINUTE));
		assertEquals(35, calendar.get(Calendar.SECOND));
	}
}
