package it.eup.loganalyser.gui;

import it.eup.loganalyser.events.ImportInterruptedException;
import it.eup.loganalyser.exceptions.AuthenticationFailureException;
import it.eup.loganalyser.importfilter.Filterable;
import it.eup.loganalyser.logging.Logger;
import it.eup.loganalyser.model.CredentialsModel;
import it.eup.loganalyser.model.StatisticsData;
import it.eup.loganalyser.service.ImportService;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ImportAsyncRunner {

  @Autowired
  ImportService importService;

  @Autowired
  CredentialsModel credentialsModel;

  @Autowired
  private Logger logger;

  public void doImport(final Filterable filter, final String... urlStrings) throws Exception {
    Runnable r = () -> {
      Map<String, StatisticsData> imported = new HashMap<>();
      List<String> filesNotFound = new ArrayList<>();
      boolean printStatistics = true;

      StatisticsData overallStatistics = new StatisticsData();
      for (final String urlString : urlStrings) {
        if (imported.containsKey(urlString)) {
          logger.log("Warnung: Datei {0} wurde bereits im Zuge dieses Vorgangs verarbeitet und wird übersprungen.",
              urlString);
          continue;
        }

        InputStream inputStream = null;
        String origin;

        if (StringUtils.isBlank(urlString)) {
          continue;
        }

        try {
          if (urlString.startsWith("http")) {
            String username = credentialsModel.getUsername().get();
            String password = credentialsModel.getPassword().get();

            inputStream = importService.openUrlInputStream(urlString, username, password);
            origin = urlString;
          } else {
            inputStream = importService.openFileInputStream(urlString);
            origin = urlString;
          }

          StatisticsData singleStatistics = importService.importStream(inputStream, origin, filter);

          if (singleStatistics != null) {
            overallStatistics.addAll(singleStatistics);
            imported.put(urlString, singleStatistics);
          }
        } catch (ImportInterruptedException e) {
          logger.log("Import abgebrochen. Weitere Logfiles werden nicht importiert.");
          overallStatistics.addAll(e.getStatistics());
          break;
        } catch (FileNotFoundException e) {
          logger.log("Die Datei {0} konnte nicht gefunden werden.", urlString);
          filesNotFound.add(urlString);
        } catch (AuthenticationFailureException e) {
          String message = "Es wurden keine Zugangsdaten eingegeben oder diese sind ungültig.";
          logger.log(message);

          Platform.runLater(() -> new DialogBuilder().createExceptionDialog("Zugangsdaten", message, e).build().showAndWait());

          printStatistics = false;
          break;
        } finally {
          IOUtils.closeQuietly(inputStream);
        }

      }

      if (printStatistics) {
        logger.log("\n\nGesamt-Statistik:");
        logger.log(overallStatistics.getStatisticsString());
      }
      if (filesNotFound.isEmpty() == false) {
        logger.log("\n\nFolgende Dateien konnten nicht gefunden werden: {0}\n", StringUtils.join(filesNotFound.iterator(), ", "));
      }
    };

    new Thread(r).start();
  }
}
