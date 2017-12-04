package it.eup.loganalyser.logging;

import it.eup.loganalyser.gui.LoggingEvent;
import java.text.MessageFormat;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class Logger {

  @Autowired
  private ApplicationEventPublisher applicationEventPublisher;

  public void log(String message, Object... params) {
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
      throwable.printStackTrace();
      String stacktrace = ExceptionUtils.getStackTrace(throwable);
      logMessage += "\n" + stacktrace;
    }

    logInternal(logMessage, throwable);
  }

  private void logInternal(String message, Throwable t) {
    LoggingEvent event = new LoggingEvent();
    event.setMessage(message);
    applicationEventPublisher.publishEvent(event);
  }
}
