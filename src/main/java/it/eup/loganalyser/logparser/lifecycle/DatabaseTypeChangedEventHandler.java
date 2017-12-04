package it.eup.loganalyser.logparser.lifecycle;

import it.eup.loganalyser.config.Constants;
import it.eup.loganalyser.events.DatabaseTypeChangeEvent;
import it.eup.loganalyser.model.DbType;
import it.eup.loganalyser.service.H2DatabaseServerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DatabaseTypeChangedEventHandler {

  @Autowired
  private ConfigurableApplicationContext applicationContext;

  @Autowired
  private DataSourceProperties dataSourceProperties;

  @Autowired
  private H2DatabaseServerManager serverManager;

  private DbType currentState = DbType.EMBEDDED;

  @EventListener
  public synchronized void handleDbTypeChange(DatabaseTypeChangeEvent event) {
    DbType newType = event.getNewType();

    if (newType == currentState) {
      return;
    }

    if (newType == DbType.TCP_SERVER) {
      switchToTcpServerMode();
    } else {
      switchToEmbeddedMode();
    }
  }

  private void switchToTcpServerMode() {
    serverManager.start();
    this.dataSourceProperties.setUrl(Constants.H2_TCPSERVER_CONNECTION_URL);
    refreshIfRequired();
  }

  private void switchToEmbeddedMode() {
    this.dataSourceProperties.setUrl(Constants.H2_EMBEDDED_CONNECTION_URL);
    refreshIfRequired();
    this.serverManager.stop();
  }

  private void refreshIfRequired() {
    if (this.applicationContext.isRunning()) {
      // this.applicationContext.refresh();
    }
  }
}
