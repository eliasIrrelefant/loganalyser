package it.eup.loganalyser.cdi;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

import javax.enterprise.inject.spi.CDI;
import javax.enterprise.util.AnnotationLiteral;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

import it.eup.loganalyser.annotations.Initialized;
import it.eup.loganalyser.annotations.StartupScene;
import it.eup.loganalyser.service.H2DatabaseServerManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class WeldJavaFXLauncher extends Application {
	/**
	 * Nothing special, we just use the JavaFX Application methods to boostrap
	 * JavaFX
	 */
	public static void main(String[] args) {
		Application.launch(WeldJavaFXLauncher.class, args);
	}

	private Weld weld;
	private WeldContainer weldContainer;

	@Override
	public void init() throws Exception {
		weld = new Weld();
		weldContainer = weld.initialize();
	}

	@SuppressWarnings("serial")
	@Override
	public void start(final Stage primaryStage) throws Exception {
	    CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
	    
		// Make the application parameters injectable with a standard CDI
		// annotation
		ApplicationParametersProvider applicationParametersProvider = weldContainer.instance().select(ApplicationParametersProvider.class)
				.get();
		applicationParametersProvider.setParameters(getParameters());
		// Now that JavaFX thread is ready
		// let's inform whoever cares using standard CDI notification mechanism:
		// CDI events
		CDI.current().getBeanManager().fireEvent(primaryStage, new AnnotationLiteral<StartupScene>() {
		});
		CDI.current().getBeanManager().fireEvent(primaryStage, new AnnotationLiteral<Initialized>() {
		});
	}

	@Override
	public void stop() throws Exception {
		weldContainer.instance().select(H2DatabaseServerManager.class).get().stop();
		weld.shutdown();
	}
}