package it.eup.loganalyser.service;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import it.eup.loganalyser.logging.Logger;

@ApplicationScoped
public class TemplateService {

	public String merge(String templateString) {
		Configuration configuration = getConfiguration();
		Map<String, Object> model = Collections.emptyMap();
		Writer writer = new StringWriter();

		try {
			Template template = new Template("template", new StringReader(templateString), configuration);
			template.process(model, writer);
			return writer.toString();
		} catch (Exception e) {
			Logger.log("Fehler", e);
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
