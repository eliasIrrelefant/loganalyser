package it.eup.loganalyser.gui;

import it.eup.loganalyser.importfilter.Filterable;
import javafx.scene.layout.GridPane;

public interface GuiFilterEntry {

	void addToGrid(GridPane gridPane, int row);

	Filterable getFilter();

	 boolean isFilterEnabled();
}
