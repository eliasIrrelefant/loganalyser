package it.eup.loganalyser.gui;

import de.felixroske.jfxsupport.FXMLController;
import it.eup.loganalyser.model.OriginOverviewModel;
import it.eup.loganalyser.service.DataCleanupService;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;

@FXMLController
public class DatacleanupController {

  @FXML
  TableView<OriginOverviewModel> tableView;

  @Autowired
  DataCleanupService dataCleanupService;

  @FXML
  protected void doCleanup() {
    List<Long> ids = extractOriginIds();

    dataCleanupService.deleteOrigins(ids);

    List<OriginOverviewModel> origins = dataCleanupService.findAllOrigins();
    tableView.setItems(FXCollections.observableList(origins));
  }

  private List<Long> extractOriginIds() {
    ObservableList<OriginOverviewModel> selectedItems = tableView.getSelectionModel().getSelectedItems();

    if (selectedItems == null) {
      return null;
    }

    List<Long> result = new ArrayList<Long>();

    for (OriginOverviewModel item : selectedItems) {
      result.add(item.getId());
    }

    return result;

  }

  @FXML
  protected void closeDialog() {
    Stage stage = (Stage) tableView.getScene().getWindow();
    stage.close();
  }

  @FXML
  public void initialize() {
    tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    refreshSelectionList();
  }

  public void refreshSelectionList() {
    List<OriginOverviewModel> origins = dataCleanupService.findAllOrigins();
    tableView.setItems(FXCollections.observableList(origins));
  }

}
