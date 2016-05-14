package it.eup.loganalyser.gui;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import it.eup.loganalyser.events.ImportInterruptedException;
import it.eup.loganalyser.exceptions.AuthenticationFailureException;
import it.eup.loganalyser.importfilter.Filterable;
import it.eup.loganalyser.logging.Logger;
import it.eup.loganalyser.model.CredentialsModel;
import it.eup.loganalyser.model.StatisticsData;
import it.eup.loganalyser.service.ImportService;
import javafx.application.Platform;

@Dependent
public class ImportAsyncRunner {

    @Inject
    ImportService importService;

    @Inject
    CredentialsModel credentialsModel;

    public void doImport(final Filterable filter, final String... urlStrings) throws Exception {
        Runnable r = new Runnable() {

            @Override
            public void run() {
                Map<String, StatisticsData> imported = new HashMap<>();
                List<String> filesNotFound = new ArrayList<>();
                boolean printStatistics = true;
                
                StatisticsData overallStatistics = new StatisticsData();
                for (final String urlString : urlStrings) {
                    if (imported.containsKey(urlString)) {
                        Logger.log("Warnung: Datei {0} wurde bereits im Zuge dieses Vorgangs verarbeitet und wird übersprungen.",
                                urlString);
                        continue;
                    }

                    final InputStream inputStream;
                    final String origin;

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
                        Logger.log("Import abgebrochen. Weitere Logfiles werden nicht importiert.");
                        overallStatistics.addAll(e.getStatistics());
                        break;
                    } catch (FileNotFoundException e) {
                        Logger.log("Die Datei {0} konnte nicht gefunden werden.", urlString);
                        filesNotFound.add(urlString);
                    } catch (AuthenticationFailureException e) {
                        String message ="Es wurden keine Zugangsdaten eingegeben oder diese sind ungültig."; 
                        Logger.log(message);
                        
                        Platform.runLater(() -> new DialogBuilder().createExceptionDialog("Zugangsdaten", message, e).build().showAndWait());
                        
                        printStatistics = false;
                        break;
                    }
                }
                
                if (printStatistics) {
                Logger.log("\n\nGesamt-Statistik:");
                Logger.log(overallStatistics.getStatisticsString());
                }
                if (filesNotFound.isEmpty() == false) {
                    Logger.log("\n\nFolgende Dateien konnten nicht gefunden werden: {0}\n", StringUtils.join(filesNotFound.iterator(), ", "));
                }
            }
        };

        new Thread(r).start();
    }
}
