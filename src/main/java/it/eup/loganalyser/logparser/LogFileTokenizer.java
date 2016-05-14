package it.eup.loganalyser.logparser;

import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;

public class LogFileTokenizer {

	private static final DefaultEscapeCharDetector DEFAULT_ESCAPED_CHAR_DETECTOR = new DefaultEscapeCharDetector();

	private final TokenHelper tokenHelper;
	
	private final String line;
	private int start = 0;

	public LogFileTokenizer(String line) {
		this.line = line;
		tokenHelper = new TokenHelper(line);
	}
	
	public LogFileTokenizer(String line, int startPos) {
		this(line);
		this.start = startPos;
	}
	
	public String next(String endDelimiter) {
		return next(null, endDelimiter, null);
	}
	
	public String next(String startDelimiter, String endDelimiter) {
		return next(startDelimiter, endDelimiter, null);
	}
	
	public String next(String startDelimiter, String endDelimiter, String escapeChars) {
		if (start >= line.length()) {
			return null;
		}
		
		Predicate<Character> delimitDetector = DEFAULT_ESCAPED_CHAR_DETECTOR;
		
		if (escapeChars != null) {
			delimitDetector = (character) -> StringUtils.contains(escapeChars, character); 
		}
		
		if (StringUtils.isNotBlank(startDelimiter)) {
			start = StringUtils.indexOf(line, startDelimiter, start);
		}

		if (start == -1) {
			return null;
		}

		int end = tokenHelper.findNextDelimiter(start, new DefaultDelimiterDetector(endDelimiter), delimitDetector);

		if (end == -1) {
			start = line.length();
			return null;
		}
		
		int readFrom = start + startDelimiter.length();  
		String result = StringUtils.substring(line, readFrom, end);

		start = Math.min(line.length(), end + StringUtils.length(endDelimiter) + 1);

		return result;
	}

	public int getCurrentPos() {
		return start;
	}
}
