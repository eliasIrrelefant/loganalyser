package it.eup.loganalyser.service;

import org.h2.tools.Server;
import org.springframework.stereotype.Component;

@Component
public class H2DatabaseServerManager {

  final Server webServer;
  final Server tcpServer;

  public H2DatabaseServerManager() {
    try {
      webServer = Server.createWebServer("-webPort", "10500");
      tcpServer = Server.createTcpServer("-trace");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public boolean isWebServerStarted() {
    if (webServer == null) {
      return false;
    }

    return webServer.isRunning(false);
  }

  public void start() {
    try {
      tcpServer.start();
      webServer.start();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void stop() {
    try {
      tcpServer.stop();
      webServer.stop();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
