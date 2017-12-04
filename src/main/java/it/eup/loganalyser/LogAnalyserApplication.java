package it.eup.loganalyser;

import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import it.eup.loganalyser.gui.MainView;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LogAnalyserApplication extends AbstractJavaFxApplicationSupport {

  private Stage rootStage;

  @Autowired private DataSourceProperties dataSourceProperties;

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

  @Override public void start(Stage stage) throws Exception {
    this.rootStage = stage;
    super.start(stage);
  }

  @Bean
  public Stage getRootStage() {
    return rootStage;
  }

  public void setRootStage(Stage rootStage) {
    this.rootStage = rootStage;
  }

  public static void main(String[] args) {
    launchApp(LogAnalyserApplication.class, MainView.class, args);
  }
}
