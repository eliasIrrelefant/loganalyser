package it.eup.loganalyser;

import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import it.eup.loganalyser.gui.MainView;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LogAnalyserApplication extends AbstractJavaFxApplicationSupport {

  private ConfigurableApplicationContext springContext;
  private Parent root;

  private Stage mainStage;

  //  @Override public void init() throws Exception {
  //    springContext = SpringApplication.run(LogAnalyserApplication.class);
  //        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui.fxml"));
  //        fxmlLoader.setControllerFactory(springContext::getBean);
  //        root = fxmlLoader.load();
  //
  //    super.init();
  //  }

  //  @Override
  //  public void start(Stage s) throws Exception {
  //    mainStage = s;
  //
  //    try {
  //      DbType type = StartupDialogDbModeHelper.showDialog(s);
  //
  //      if (type == DbType.TCP_SERVER) {
  //        /*h2DatabaseServerManager.start();
  //        entityManagerFactoryProducer.setType(Type.tcp);*/
  //      }
  //
  //    } catch (Exception e) {
  //      e.printStackTrace();
  //      throw e;
  //    }
  //
  //    super.start(s);
  // }

  public static void main(String[] args) {
    launchApp(LogAnalyserApplication.class, MainView.class, args);
  }

  @Bean
  public Stage getRootStage() {
    return mainStage;
  }

}
