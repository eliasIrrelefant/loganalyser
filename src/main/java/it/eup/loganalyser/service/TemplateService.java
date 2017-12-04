package it.eup.loganalyser.service;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import it.eup.loganalyser.logging.Logger;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Map;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TemplateService {

  @Autowired
  private Logger logger;

  public String merge(String templateString) {
    Configuration configuration = getConfiguration();
    Map<String, Object> model = Collections.emptyMap();
    Writer writer = new StringWriter();

    try {
      Template template = new Template("template", new StringReader(templateString), configuration);
      template.process(model, writer);
      return writer.toString();
    } catch (Exception e) {
      logger.log("Fehler", e);
      throw new RuntimeException(e);
    }
  }

  protected Configuration getConfiguration() {
    Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
    cfg.setDefaultEncoding("UTF-8");
    cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_23));
    return cfg;
  }
}
