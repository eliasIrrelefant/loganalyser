package it.eup.loganalyser.launcher;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import it.eup.loganalyser.annotations.StartupScene;
import it.eup.loganalyser.cdi.EntityManagerFactoryProducer;
import it.eup.loganalyser.cdi.EntityManagerFactoryProducer.Type;
import it.eup.loganalyser.gui.StartupDialogDbModeHelper;
import it.eup.loganalyser.model.DbType;
import it.eup.loganalyser.service.H2DatabaseServerManager;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

@ApplicationScoped
public class AppStarter {

	@Inject
	FXMLLoader fxmlLoader;

	@Inject
	H2DatabaseServerManager h2DatabaseServerManager;

	@Inject
	EntityManagerFactoryProducer entityManagerFactoryProducer;

	private Stage mainStage;

	public void launch(@Observes @StartupScene Stage s) {
		mainStage = s;

		try {
			DbType type = StartupDialogDbModeHelper.showDialog(s);

			if (type == DbType.TCP_SERVER) {
				h2DatabaseServerManager.start();
				entityManagerFactoryProducer.setType(Type.tcp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Produces
	@Dependent
	public Stage getRootStage() {
		return mainStage;
	}
}
