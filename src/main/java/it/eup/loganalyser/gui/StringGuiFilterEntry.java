package it.eup.loganalyser.gui;

import it.eup.loganalyser.importfilter.Filterable;
import it.eup.loganalyser.importfilter.InputFilterFactory;
import java.util.List;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.apache.commons.lang3.StringUtils;

public class StringGuiFilterEntry implements GuiFilterEntry {

  private InputFilterFactory<String> inputFilterFactory;
  private Label label;
  private ChoiceBox<String> choiceBox;
  private TextField input;

  public StringGuiFilterEntry(String labelString, List<String> choiceValues, InputFilterFactory<String> inputFilter) {
    this.inputFilterFactory = inputFilter;

    label = UiUtils.createLabelWithMaxWidth(labelString);

    choiceBox = UiUtils.createChoiceBoxWithMaxWidthAndFirstEntrySelected(choiceValues);

    input = new TextField();
  }

  protected String getSelectedChoice() {
    return choiceBox.getSelectionModel().getSelectedItem();
  }

  protected String getSelectedInputString() {
    return input.getText();
  }

  @Override
  public boolean isFilterEnabled() {
    return StringUtils.equals("beliebig", choiceBox.getValue()) == false;
  }

  @Override
  public Filterable getFilter() {
    return inputFilterFactory.createFilter(choiceBox.getValue(), input.getText());
  }

  @Override
  public void addToGrid(GridPane gridPane, int row) {
    gridPane.add(label, 0, row, 2, 1);
    gridPane.add(choiceBox, 2, row, 2, 1);
    gridPane.add(input, 4, row, 8, 1);
  }
}