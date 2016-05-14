package it.eup.loganalyser.logparser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import it.eup.loganalyser.entity.LogDataRow;
import it.eup.loganalyser.parser.LambdaTokenParser;
import it.eup.loganalyser.parser.converter.StringConverter;

public class HttpdLogParser {

    private final String logFormat;
    private final List<LambdaTokenParser<?>> parserList = new ArrayList<>();
	private final Map<String, TokenBuilder<?>> tokenRegistry = new HashMap<>();

	public HttpdLogParser(String logFormat) {
	    this.logFormat = logFormat;
	    
		registerParser(TokenBuilder.newStringBuilder("%{X-Forwarded-For}i").withConverter(new StringConverter(128)).withEscapeChars("\\,")
				.withValueConsumer((x, y) -> x.setForwardedForIp(y)));
		registerParser(TokenBuilder.newRequestDateBuilder("%t").withValueConsumer((x,y) -> x.setRequestDate(y)));
		registerParser(TokenBuilder.newStringBuilder("%r").withValueConsumer(new HttpFirstLineConsumer()));
		registerParser(TokenBuilder.newStringBuilder("%{User-Agent}i").withConverter(new StringConverter(1024)).withValueConsumer((x, y) -> x.setUserAgent(y)));
		registerParser(TokenBuilder.newStringBuilder("%{Referer}i").withConverter(new StringConverter(2048)).withValueConsumer((x, y) -> x.setReferer(y)));
		registerParser(TokenBuilder.newStringBuilder("%{Host}i").withConverter(new StringConverter(1024)).withValueConsumer((x, y) -> x.setHostname(y)));
		registerParser(TokenBuilder.newStringBuilder("%v").withConverter(new StringConverter(1024)).withValueConsumer((x, y) -> x.setHostname(y)));
		registerParser(TokenBuilder.newIntegerBuilder("%D").withValueConsumer((x, y) -> x.setResponseTime(y)));
		registerParser(TokenBuilder.newIntegerBuilder("%{Age}o").withValueConsumer((x, y) -> x.setCacheAge(y)));
		registerParser(TokenBuilder.newIntegerBuilder("%s").withValueConsumer((x, y) -> x.setStatusCode(y)));
		registerParser(TokenBuilder.newIntegerBuilder("%>s").withValueConsumer((x, y) -> x.setStatusCode(y)));
		registerParser(TokenBuilder.newIntegerBuilder("%b").withValueConsumer((x, y) -> x.setTransferedBytes(y)));
		registerParser(TokenBuilder.newStringBuilder("%h").withValueConsumer((x,y) -> x.setIp(y)));

		registerParser(
				TokenBuilder.newStringBuilder("%{X-CipherName}i").withValueConsumer((x, y) -> x.setCipherName(y)));
		registerParser(TokenBuilder.newStringBuilder("%{X-CipherVersion}i")
				.withValueConsumer((x, y) -> x.setCipherVersion(y)));
		registerParser(
				TokenBuilder.newStringBuilder("%{X-CipherBits}i").withValueConsumer((x, y) -> x.setCipherBits(y)));
		registerParser(
				TokenBuilder.newBooleanBuilder("%{SSL-FRONTEND}i").withValueConsumer((x, y) -> x.setSslFrontend(y)));

		init();
	}

	private void registerParser(TokenBuilder<?> tokenBuilder) {
		this.tokenRegistry.put(tokenBuilder.getName(), tokenBuilder);
	}

	public void init() {
		parserList.clear();

		String[] parts = logFormat.split(" ");

		for (int i = 0; i < parts.length; ++i) {
			String part = parts[i];

			LambdaTokenParser<?> parser = initMatchingParser(part);
			parserList.add(parser);
		}
	}

	private LambdaTokenParser<?> initMatchingParser(String part) {
		for (TokenBuilder<?> builder : tokenRegistry.values()) {
			if (isTokenBuilderApplicable(builder, part)) {
				String name = builder.getName();
				
				String patternPrefix = StringUtils.substringBefore(part, name);
				String patternSuffix = StringUtils.substringAfterLast(part, name);
				
				builder.enquotedWith(patternPrefix, patternSuffix);
				
				return builder.build();
			}
		}
		
		System.out.printf("Log token %s wird nicht unterstützt und beim Import ignoriert.\n", part);
		return TokenBuilder.newIgnoreBuilder(part).build();
	}

	private boolean isTokenBuilderApplicable(TokenBuilder<?> builder, String logFragment) {
		return StringUtils.contains(logFragment, builder.getName());
	}

	public LogDataRow parseLine(String line) {
		LogDataRow result = new LogDataRow();

		LogFileTokenizer tokenizer = new LogFileTokenizer(line);

		for (LambdaTokenParser<?> logFileTokenParser : parserList) {
			try {
				logFileTokenParser.parse(result, tokenizer);
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}

		if (result.getIp() == null) {
			result.setIp(StringUtils.substringBefore(result.getForwardedForIp(), ","));
		}

		if (result.getCacheAge() != null) {
			result.setCacheAgePresent(true);
		}

		return result;
	}
}
