package it.eup.loganalyser.logparser;

import java.util.function.BiConsumer;

import org.apache.commons.lang3.StringUtils;

import it.eup.loganalyser.entity.LogDataRow;

public class HttpFirstLineConsumer implements BiConsumer<LogDataRow, String> {

    @Override
    public void accept(LogDataRow row, String httpFirstLine) {
        String[] parts = StringUtils.split(httpFirstLine);

        if (parts.length >= 1) {
            row.setMethod(parts[0]);
        }

        if (parts.length >= 2) {
            String resourceString = parts[1];
            new ParserHelper().splittResourceStringAndUpdateValues(row, resourceString);
        }

        if (parts.length >= 3) {
            row.setHttpVersion(StringUtils.substring(parts[2], 0, 15));
        }
    }

}