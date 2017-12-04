package it.eup.loganalyser.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class CredentialsModel {
  private StringProperty username = new SimpleStringProperty();
  private StringProperty password = new SimpleStringProperty();

}
