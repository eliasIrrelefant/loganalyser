package it.eup.loganalyser.gui;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.eup.loganalyser.importfilter.FilterOptions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;

public class UiUtils {

    public static <T> ChoiceBox<T> createChoiceBoxWithMaxWidthAndFirstEntrySelected(List<T> choiceValues) {
        ObservableList<T> list = FXCollections.observableArrayList(choiceValues);
        
        ChoiceBox<T> choiceBox = new ChoiceBox<T>(list);
        choiceBox.setMaxWidth(Double.MAX_VALUE);
        choiceBox.getSelectionModel().select(0);

        return choiceBox;
    }
    
    public void setDefaultInputPaddings(Control node) {
        node.setPadding(new Insets(0, 10, 0, 0));
    }
    
    public static Label createLabelWithMaxWidth(String labelString) {
        Label label = new Label(labelString);
        label.setPrefWidth(100);
        
        return label;
    }
    
    public static boolean isNotAnySelected(ChoiceBox<String> choiceBox) {
        return !isAnySelected(choiceBox);
    }
    
    public static boolean isAnySelected(ChoiceBox<String> choiceBox) {
        String value = choiceBox.getValue();
        
        return StringUtils.equals(FilterOptions.ANY, value);
    }
}
