package it.eup.loganalyser.gui;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLInputElement;

import com.sun.webkit.dom.HTMLTextAreaElementImpl;

public class WebViewUtils {

	public static void updateInputValueByName(Document document, String htmlName, String newValue) {
		HTMLInputElement inputElement = (HTMLInputElement) findElementByTypeAndName(document, "input", htmlName);

		updateValue(inputElement, newValue);
	}

	private static void updateValue(HTMLInputElement inputElement, String newValue) {
		if (inputElement != null) {
			inputElement.setValue(newValue);
		}
	}

	public static void updateTextAreaValueById(Document document, String id, String newValue) {
		HTMLTextAreaElementImpl element = (HTMLTextAreaElementImpl) document.getElementById(id);

		if (element != null) {
			element.setValue(newValue);
		}
	}

	public static HTMLElement findElementByTypeAndName(Document document, String tagName, String htmlName) {
		NodeList tags = document.getElementsByTagName(tagName);

		for (int i = 0; i < tags.getLength(); ++i) {
			HTMLElement element = (HTMLElement) tags.item(i);

			String name = element.getAttribute("name");

			if (StringUtils.equals(name, htmlName)) {
				return element;
			}
		}

		return null;
	}
}
