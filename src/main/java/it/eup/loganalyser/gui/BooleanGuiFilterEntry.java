package it.eup.loganalyser.gui;

import it.eup.loganalyser.entity.LogDataRow;
import it.eup.loganalyser.importfilter.BooleanFilter;
import it.eup.loganalyser.importfilter.FilterOptions;
import it.eup.loganalyser.importfilter.Filterable;
import java.util.function.Function;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.apache.commons.lang3.StringUtils;

public class BooleanGuiFilterEntry implements GuiFilterEntry {

  private Label label;
  private ChoiceBox<String> choiceBox;
  private Function<LogDataRow, Boolean> valueAccessor;

  public BooleanGuiFilterEntry(String labelString, Function<LogDataRow, Boolean> valueAccessor) {
    label = UiUtils.createLabelWithMaxWidth(labelString);
    choiceBox = UiUtils.createChoiceBoxWithMaxWidthAndFirstEntrySelected(FilterOptions.ALL_BOOLEAN_OPTIONS);
    this.valueAccessor = valueAccessor;
  }

  @Override
  public void addToGrid(GridPane gridPane, int row) {
    gridPane.add(label, 0, row, 2, 1);
    gridPane.add(choiceBox, 2, row, 2, 1);
  }

  @Override
  public Filterable getFilter() {
    String choiceValue = choiceBox.getValue();

    if (StringUtils.equals(FilterOptions.YES, choiceValue)) {
      return new BooleanFilter(valueAccessor, Boolean.TRUE);
    } else if (StringUtils.equals(FilterOptions.NO, choiceValue)) {
      return new BooleanFilter(valueAccessor, Boolean.FALSE);
    }

    throw new IllegalArgumentException();
  }

  @Override
  public boolean isFilterEnabled() {
    return UiUtils.isNotAnySelected(choiceBox);
  }

}
