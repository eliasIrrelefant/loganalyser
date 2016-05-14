package it.eup.loganalyser.gui;

import org.apache.commons.lang3.StringUtils;

import it.eup.loganalyser.importfilter.Filterable;
import it.eup.loganalyser.importfilter.LambdaStringInputFilterFactory;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

public class LambdaStringGuiFilterEntry implements GuiFilterEntry {

	private Label label;
	private TextArea input;

	protected LambdaStringGuiFilterEntry(String labelString) {
	    label = UiUtils.createLabelWithMaxWidth(labelString);
		input = new TextArea();
	}

	@Override
	public boolean isFilterEnabled() { 
	    return StringUtils.isNotBlank(input.getText());
	}

	@Override
	public Filterable getFilter() {
	    return LambdaStringInputFilterFactory.createNew(input.getText());
	}

	@Override
	public void addToGrid(GridPane gridPane, int row) {
		gridPane.add(label, 0, row, 2, 1);
		gridPane.add(input, 2, row, 10, 1);
	}
}