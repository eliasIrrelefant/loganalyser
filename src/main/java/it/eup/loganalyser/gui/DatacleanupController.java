package it.eup.loganalyser.gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import it.eup.loganalyser.model.OriginOverviewModel;
import it.eup.loganalyser.service.DataCleanupService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

@Dependent
public class DatacleanupController implements Initializable {

	@FXML
	TableView<OriginOverviewModel> tableView;

	@Inject
	DataCleanupService dataCleanupService;

	@FXML
	protected void doCleanup() {
		List<Long> ids = extractOriginIds();

		dataCleanupService.deleteOrigins(ids);

		updateSelectionListValues();
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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		updateSelectionListValues();
	}

	private void updateSelectionListValues() {
		List<OriginOverviewModel> origins = dataCleanupService.findAllOrigins();
		tableView.setItems(FXCollections.observableList(origins));
	}
}
