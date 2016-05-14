package it.eup.loganalyser.gui;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLSelectElement;

import it.eup.loganalyser.config.Constants;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class H2WebConsoleLoginHandler implements ChangeListener<Document> {

	@Override
	public void changed(ObservableValue<? extends Document> observableValue, Document oldDocument, Document newDocument) {
		if (newDocument == null) {
			return;
		}

		String newUrl = newDocument.getDocumentURI();

		if (StringUtils.startsWith(newUrl, "http://localhost:10500/login.jsp") == false) {
			return;
		}

		HTMLSelectElement select = (HTMLSelectElement) WebViewUtils.findElementByTypeAndName(newDocument, "select", "setting");
		select.setValue("Generic H2 (Server)");

		WebViewUtils.updateInputValueByName(newDocument, "url", Constants.H2_TCPSERVER_CONNECTION_URL);
		WebViewUtils.updateInputValueByName(newDocument, "user", "");
		WebViewUtils.updateInputValueByName(newDocument, "password", "");

		HTMLFormElement form = (HTMLFormElement) WebViewUtils.findElementByTypeAndName(newDocument, "form", "login");
		form.submit();
	}

}
