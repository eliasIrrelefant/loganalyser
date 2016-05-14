package it.eup.loganalyser.cdi;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import javafx.application.Application.Parameters;

@ApplicationScoped
public class ApplicationParametersProvider {

	private Parameters parameters;

	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}

	public @Produces
	Parameters getParameters() {
		return parameters;
	}
}
