package it.eup.loganalyser.logparser;

import it.eup.loganalyser.entity.LogDataRow;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class ParserHelper {

  private static final int SESSIONID_MAX_LENGTH = 128;
  private static final String SESSIONID_REGEX = "[0-9a-zA-Z-_:]*";

  public String extractBasePath(String completePath) {
    return StringUtils.substringBefore(completePath, "?");
  }

  public String extractQueryString(String completePath) {
    return StringUtils.substringAfter(completePath, "?");
  }

  public Integer parseInt(String input) {
    try {
      return Integer.parseInt(input);
    } catch (Exception e) {
      return null;
    }
  }

  public String extractSessionId(String path) {
    return StringUtils.substringAfter(path, ";jsessionid=");
  }

  public Date parseDate(String dateString) {
    if (StringUtils.isBlank(dateString)) {
      return null;
    }

    dateString = dateString.replaceAll("[\\[\\]]", "");

    // 07/May/2015:23:47:35 +0200
    DateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z", Locale.US);

    try {
      return dateFormat.parse(dateString);
    } catch (ParseException e) {
      return null;
    }
  }

  public boolean isCacheAgePresent(String cacheAge) {
    return StringUtils.isNotBlank(cacheAge);
  }

  public String extractPathWithoutSessionId(String path) {
    return StringUtils.substringBefore(path, ";jsessionid=");
  }

  public void splittResourceStringAndUpdateValues(LogDataRow row, String resourceString) {
    String basePath = extractBasePath(resourceString);

    String resourceWithoutSessionId = extractPathWithoutSessionId(basePath);
    String queryString = extractQueryString(resourceString);
    String sessionId = extractSessionId(basePath);

    if (isSessionIdValid(sessionId)) {
      row.setPath(resourceWithoutSessionId);
      row.setSessionid(sessionId);
      row.setQueryString(queryString);
    } else {
      row.setPath(StringUtils.substringBefore(basePath, "?"));
      row.setQueryString(null);
      row.setSessionid(null);
    }
  }

  public boolean isSessionIdValid(String sessionid) {
    if (StringUtils.isBlank(sessionid)) {
      return true;
    }

    if (StringUtils.length(sessionid) > SESSIONID_MAX_LENGTH) {
      return false;
    }

    boolean patternMatches = Pattern.matches(SESSIONID_REGEX, sessionid);

    if (patternMatches == false) {
      return false;
    }

    return true;
  }

  public String truncate(String referer, int maxLength) {
    if (referer == null) {
      return null;
    }

    if (referer.length() > maxLength) {
      return referer.substring(0, maxLength);
    }

    return referer;
  }

  public String extractIp(String combinedIp) {
    if (StringUtils.contains(combinedIp, ",") == false) {
      return combinedIp;
    }

    return StringUtils.substringAfter(combinedIp, ", ");
  }

  public String extractProxyIp(String combinedIp) {
    if (StringUtils.contains(combinedIp, ", ") == false) {
      return null;
    }

    return StringUtils.substringBefore(combinedIp, ", ");
  }

}
