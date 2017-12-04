package it.eup.loganalyser.service;

import it.eup.loganalyser.logging.Logger;
import it.eup.loganalyser.model.ConfigModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.LineBreak;
import org.yaml.snakeyaml.DumperOptions.ScalarStyle;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

@Service
public class ConfigService {

  @Autowired
  private Logger logger;

  public ConfigModel read(File file) {
    try {
      FileInputStream inputStream = new FileInputStream(file);
      return readYml(inputStream);
    } catch (FileNotFoundException e) {
      logger.log("Fehler beim einlesen der Datei {0}.", file.getAbsoluteFile(), e);
      return null;
    }
  }

  public ConfigModel readYml(InputStream inputStream) {
    Yaml yaml = getYaml();

    ConfigModel configModel = yaml.loadAs(inputStream, ConfigModel.class);

    return configModel;
  }

  protected Yaml getYaml() {
    Representer representer = new SkipNullRepresenter();

    DumperOptions options = new DumperOptions();
    options.setDefaultScalarStyle(ScalarStyle.PLAIN);
    options.setLineBreak(LineBreak.UNIX);

    return new Yaml(representer, options);
  }

  public ConfigModel getDefaultEntries() {
    String pathname = "config/config.yml";

    String home = System.getProperty("app.home");
    if (StringUtils.isNotBlank(home)) {
      pathname = home + "/" + pathname;
    }

    File file = new File(pathname);

    if (file.exists()) {
      logger.log("Lade Queries aus {0}", file.getAbsolutePath());
      return read(file);
    }

    logger.log("Datei {0} ist nicht vorhanden. Verwendet Queries aus Installationspaket", file.getAbsolutePath());

    InputStream inputStream = getClass().getResourceAsStream("/config/config.yml");
    return readYml(inputStream);
  }

  protected class SkipNullRepresenter extends Representer {
    @Override
    protected NodeTuple representJavaBeanProperty(Object javaBean, Property property, Object propertyValue, Tag customTag) {
      if (propertyValue == null) {
        return null;
      } else {
        return super.representJavaBeanProperty(javaBean, property, propertyValue, customTag);
      }
    }
  }

}
