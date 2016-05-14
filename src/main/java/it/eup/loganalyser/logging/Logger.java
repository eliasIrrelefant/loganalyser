package it.eup.loganalyser.logging;

import java.text.MessageFormat;

import javax.enterprise.inject.spi.CDI;

import org.apache.commons.lang3.exception.ExceptionUtils;

import it.eup.loganalyser.gui.LoggingEvent;

public class Logger {

    public static void log(String message, Object... params) {
        Throwable throwable = null;
        String logMessage = null;
        int paramCount = params.length;
        
        if (paramCount >= 1 && params[paramCount - 1] instanceof Throwable) {
            throwable = (Throwable) params[params.length - 1];
            paramCount--;
        }

        if (paramCount == 0) {
            logMessage = message;
        } else {
            logMessage = MessageFormat.format(message, params);
        }

        if (throwable != null) {
            String stacktrace = ExceptionUtils.getStackTrace(throwable);
            logMessage += "\n" + stacktrace; 
        }

        logInternal(logMessage, throwable);
    }

    private static void logInternal(String message, Throwable t) {
        LoggingEvent event = new LoggingEvent();
        event.setMessage(message);
        CDI.current().getBeanManager().fireEvent(event);
    }
}
