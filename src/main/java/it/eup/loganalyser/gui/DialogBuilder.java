package it.eup.loganalyser.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class DialogBuilder {

	private AlertType type = AlertType.CONFIRMATION;
	private String title;
	private String headerText;
	private String contentText;
	private Throwable throwable;
	private List<ButtonType> stageButtons = new ArrayList<>();

	public DialogBuilder() {
	}

	public DialogBuilder createDefaultDialog(String title, String headerText) {
		withTitle(title);
		withHeaderText(headerText);

		return this;
	}

	public DialogBuilder createDefaultYesNoDialog(String title, String headerText) {
		withTitle(title);
		withHeaderText(headerText);

		withButtons(ButtonType.YES, ButtonType.NO);

		return this;
	}

	public DialogBuilder createExceptionDialog(String title, String headerText, Exception e) {
		withTitle(title);
		withHeaderText(headerText);
		this.throwable = e;

		return this;
	}

	public DialogBuilder withTitle(String title) {
		this.title = title;
		return this;
	}

	public DialogBuilder withHeaderText(String headerText) {
		this.headerText = headerText;
		return this;
	}

	public DialogBuilder withContentMessage(String contentText) {
		this.contentText = contentText;
		return this;
	}

	public DialogBuilder addStageButton(String buttonText) {
		this.stageButtons.add(new ButtonType(buttonText));
		return this;
	}

	public DialogBuilder withNoButtons() {
		this.stageButtons = new ArrayList<>();
		return this;
	}

	public DialogBuilder withOkButton() {
		this.stageButtons = Arrays.asList(new ButtonType("OK"));
		return this;
	}

	public DialogBuilder withButtons(ButtonType... buttons) {
		stageButtons = Arrays.asList(buttons);
		return this;
	}

	public DialogBuilder withButtons(String... buttons) {
		stageButtons = new ArrayList<>();

		for (String s : buttons) {
			stageButtons.add(new ButtonType(s));
		}

		return this;
	}

	public Alert build() {
		Alert alert = new Alert(type);

		alert.setTitle(title);

		if (headerText != null) {
			alert.setHeaderText(headerText);
		}

		if (contentText != null) {
			alert.setHeaderText(contentText);
		}

		if (throwable != null) {
			if (contentText== null) {
				alert.setContentText(throwable.getLocalizedMessage());
			}
			
			String stacktrace = ExceptionUtils.getStackTrace(throwable);
			addScrollableReadonlyTextArea(alert, stacktrace);
		}

		if (stageButtons.isEmpty() == false) {
			alert.getButtonTypes().clear();

			for (ButtonType button : stageButtons) {
				alert.getButtonTypes().add(button);
			}
		}

		return alert;
	}

	private void addScrollableReadonlyTextArea(Alert alert, String stacktrace) {
		Label label = new Label("Stacktrace");

		TextArea textArea = new TextArea(stacktrace);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane content = new GridPane();
		content.setMaxWidth(Double.MAX_VALUE);
		content.add(label, 0, 0);
		content.add(textArea, 0, 1);
		
		alert.getDialogPane().setExpandableContent(content);
	}

	public DialogBuilder withThrowable(Throwable t) {
		this.throwable = t;
		return this;
	}

}
