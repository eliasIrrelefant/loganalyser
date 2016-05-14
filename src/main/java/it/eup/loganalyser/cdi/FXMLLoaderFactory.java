package it.eup.loganalyser.cdi;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import javafx.fxml.FXMLLoader;
import javafx.util.Callback;

@ApplicationScoped
public class FXMLLoaderFactory {

	@Inject
	Instance<Object> instance;

	@Produces
	@Dependent
	public FXMLLoader createLoader() {
		FXMLLoader loader = new FXMLLoader();

		loader.setControllerFactory(new Callback<Class<?>, Object>() {
			@Override
			public Object call(Class<?> param) {
				return instance.select(param).get();
			}
		});
		return loader;
	}
}
