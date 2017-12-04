package it.eup.loganalyser.gui;

import it.eup.loganalyser.model.DbType;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class StartupDialogDbModeHelper {

  private static final String TCP_SERVER = "TCP-Server";

  private static final String EMBEDDED = "Embedded";

  public static DbType showDialog(Stage stage) {
    String message = "Embedded ist am schnellsten für den Import großer Datenmengen\n"
        + "TCP-Server bietet zusätzliche Möglichkeiten wie die Benutzung\n"
        + "von DB-Visualizer oder einer Web-Console.";

    String title = "Wählen Sie die Art der Datenbankverbindung aus";

    Alert alert = new Alert(AlertType.CONFIRMATION);

    alert.setTitle("Bitte auswählen ...");
    alert.setHeaderText(title);
    alert.setContentText(message);
    alert.setHeight(alert.getHeight() * 2);

    ButtonType embeddedButton = new ButtonType(EMBEDDED);
    ButtonType tcpServerButton = new ButtonType(TCP_SERVER);
    alert.getButtonTypes().clear();
    alert.getButtonTypes().add(embeddedButton);
    alert.getButtonTypes().add(tcpServerButton);

    Optional<ButtonType> result = alert.showAndWait();

    if (result.isPresent() == false) {
      System.exit(0);
    }

    ButtonType selectedOption = result.get();
    if (selectedOption == tcpServerButton) {
      return DbType.TCP_SERVER;
    } else {
      return DbType.EMBEDDED;
    }
  }
}
