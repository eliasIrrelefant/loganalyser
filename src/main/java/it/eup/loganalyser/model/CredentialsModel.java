package it.eup.loganalyser.model;

import javax.enterprise.context.ApplicationScoped;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@ApplicationScoped
public class CredentialsModel {
	private StringProperty username = new SimpleStringProperty();
	private StringProperty password = new SimpleStringProperty();

	public StringProperty getPassword() {
		return password;
	}

	public StringProperty getUsername() {
		return username;
	}

	public void setPassword(StringProperty password) {
		this.password = password;
	}

	public void setUsername(StringProperty username) {
		this.username = username;
	}
}
