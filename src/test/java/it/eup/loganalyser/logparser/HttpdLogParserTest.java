package it.eup.loganalyser.logparser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import it.eup.loganalyser.entity.LogDataRow;
import java.text.DateFormat;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;

public class HttpdLogParserTest {

  public static String LOG_FORMAT =
      "%{X-Forwarded-For}i %l %u %t \"%r\" %>s %b \"%{Referer}i\" \"%{User-Agent}i\" %D %% \"%{Age}o\" %% %{If-Modified-Since}i "
          + "\"%{X-CipherName}i\" \"%{X-CipherVersion}i\" \"%{X-CipherBits}i\" \"%{SSL-FRONTEND}i\"";

  public HttpdLogParser parser;

  public ParserHelper helper = new ParserHelper();

  @Before
  public void setup() {
    parser = new HttpdLogParser(LOG_FORMAT);
  }

  @Test
  public void defaultLineIsRead() {
    String input =
        "172.16.13.37 - - [08/Mar/2015:23:45:55 +0100] \"GET /path/to/resource/index.html?param=value HTTP/1.1\" 200 1418 \"http://www"
            + ".referer.tld/test.html\" \"Mozilla/5.0; TOB 6.13 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko\" 12595 % \"42\""
            + " %";

    LogDataRow data = parser.parseLine(input);

    assertEquals("172.16.13.37", data.getIp());
    assertEquals("GET", data.getMethod());
    assertEquals("/path/to/resource/index.html", data.getPath());
    assertEquals("param=value", data.getQueryString());
    assertEquals("HTTP/1.1", data.getHttpVersion());
    assertEquals(Integer.valueOf(200), data.getStatusCode());
    assertEquals("Mozilla/5.0; TOB 6.13 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko", data.getUserAgent());
    assertEquals("http://www.referer.tld/test.html", data.getReferer());
    assertEquals(Integer.valueOf(42), data.getCacheAge());
    assertTrue("cacheAgePresent must be true", data.getCacheAgePresent());
  }

  @Test
  public void lineWithNoResponseTimeIsRead() {
    String input =
        "172.16.13.37 - - [08/Mar/2015:23:46:04 +0100] \"GET /path/to/resource/index.html HTTP/1.1\" 304 - \"-\" \"Googlebot-Image/1.0\" "
            + "24219 % \"-\" %";

    LogDataRow data = parser.parseLine(input);

    assertEquals("172.16.13.37", data.getIp());
    assertDateEquals("08.03.2015 23:46:04", data.getRequestDate());
    assertEquals("/path/to/resource/index.html", data.getPath());
  }

  @Test
  public void lineWithoutCacheInfoIsParsedWithoutError() {
    String input =
        "172.16.13.37 - - [30/May/2015:23:46:02 +0200] \"GET /path/to/resource/index.html HTTP/1.1\" 200 19609 \"-\" "
            + "\"AndroidDownloadManager/4.1.1 (Linux; U; Android 4.1.1; ST80208-2 Build/JRO03H)\" 12098";

    LogDataRow dataRow = parser.parseLine(input);

    assertNotNull(dataRow);
    assertNull(dataRow.getCacheAge());
    assertFalse("cacheAgePresent must be false", dataRow.getCacheAgePresent());
  }

  @Test
  public void lineBlankCacheAgeIsParsedAsNull() throws Exception {
    String input =
        "172.16.13.37 - - [08/Mar/2015:23:46:04 +0100] \"GET /path/to/resource/index.html HTTP/1.1\" 304 - \"-\" \"Googlebot-Image/1.0\" "
            + "24219 % \"-\" %";

    LogDataRow data = parser.parseLine(input);

    assertEquals(null, data.getCacheAge());
  }

  @Test
  public void parsingWithTokenizerForDefaultLine() throws Exception {
    String input =
        "172.16.13.37 ident username [08/Mar/2015:23:46:04 +0100] \"GET /path/to/resource/index.html?param=value&v=2 HTTP/1.1\" 200 1337 "
            + "\"http://referer.com/\" \"Googlebot-Image/1.0\" 24219 % \"42\" %";

    LogDataRow data = parser.parseLine(input);

    assertEquals("172.16.13.37", data.getIp());
    assertEquals("GET", data.getMethod());
    assertEquals("/path/to/resource/index.html", data.getPath());
    assertEquals("param=value&v=2", data.getQueryString());
    assertEquals("HTTP/1.1", data.getHttpVersion());
    assertEquals(Integer.valueOf(200), data.getStatusCode());
    assertEquals("Googlebot-Image/1.0", data.getUserAgent());
    assertEquals("http://referer.com/", data.getReferer());
    assertEquals(Integer.valueOf(42), data.getCacheAge());
    assertTrue("cacheAgePresent is expected to be true", data.getCacheAgePresent());
  }

  @Test
  public void parsingWithTokenizerForDefaultLineWithoutCacheAge() throws Exception {
    String input =
        "172.16.13.37 ident username [08/Mar/2015:23:46:04 +0100] \"GET /path/to/resource/index.html?param=value&v=2 HTTP/1.1\" 200 1337 "
            + "\"http://referer.com/\" \"Googlebot-Image/1.0\" 24219";

    LogDataRow data = parser.parseLine(input);

    assertEquals("172.16.13.37", data.getIp());
    assertEquals("GET", data.getMethod());
    assertEquals("/path/to/resource/index.html", data.getPath());
    assertEquals("param=value&v=2", data.getQueryString());
    assertEquals("HTTP/1.1", data.getHttpVersion());
    assertEquals(Integer.valueOf(200), data.getStatusCode());
    assertEquals("Googlebot-Image/1.0", data.getUserAgent());
    assertEquals("http://referer.com/", data.getReferer());
    assertEquals(null, data.getCacheAge());
    assertFalse("cacheAgePresent is expected to be false", data.getCacheAgePresent());
  }

  @Test
  public void cipherInfoIsDetected() throws Exception {
    String input =
        "172.16.13.37 - - [03/May/2016:01:20:34 +0200] \"GET / HTTP/1.1\" 302 237 \"-\" \"Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US;"
            + " rv:1.7.12) Gecko/20050915 Firefox/1.0.7\" 1698 % \"-\" % - \"DHE-RSA-AES256-GCM-SHA384\" \"TLSv1.2\" \"256\" \"yes\"";

    LogDataRow data = parser.parseLine(input);

    assertEquals("DHE-RSA-AES256-GCM-SHA384", data.getCipherName());
    assertEquals("TLSv1.2", data.getCipherVersion());
    assertEquals("256", data.getCipherBits());
    assertEquals(Boolean.TRUE, data.getSslFrontend());
  }

  @Test
  public void lineWithInvalidResourceStringIsDetected() throws Exception {
    String line =
        "172.16.13.37 - - [17/Jun/2015:17:50:42 +0200] \"GET /path/to/resource/index.html;jsessionid=\\\"&uri=http%253A%252F%252Fwww"
            + ".domain.tld%252FTest%252FEN%252FHome%252F HTTP/1.0\" 404 56074 \"-\" \"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 7.0) "
            + "XSpider 7\" 435130 % \"-\" %";

    LogDataRow dataRow = parser.parseLine(line);

    assertNotNull(dataRow);
    assertEquals("/path/to/resource/index.html;jsessionid=\\\"&uri=http%253A%252F%252Fwww.domain.tld%252FTest%252FEN%252FHome%252F",
        dataRow.getPath());
    assertNull(dataRow.getQueryString());
    assertNull(dataRow.getSessionid());

  }

  @Test
  public void logEntryWithProxyForwardForIsParsedCorrect() {
    String line =
        "172.16.13.37, 10.0.0.1 - - [03/Jul/2015:07:53:04 +0200] \"GET /path/to/resource/index.html?param=value&v=2 HTTP/1.1\" 200 "
            + "1114112 \"http://www.referer.tld/test.html\" \"Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) "
            + "Chrome/43.0.2357.130 Safari/537.36\" 1188548 % \"-\" %";

    LogDataRow dataRow = parser.parseLine(line);

    assertNotNull(dataRow);
    assertEquals("172.16.13.37, 10.0.0.1", dataRow.getForwardedForIp());
    assertEquals("172.16.13.37", dataRow.getIp());

    assertDateEquals("03.07.2015 07:53:04", dataRow.getRequestDate());
  }

  protected void assertDateEquals(String expected, Date actual) {
    assertEquals(expected, DateFormat.getDateTimeInstance().format(actual));
  }
}
