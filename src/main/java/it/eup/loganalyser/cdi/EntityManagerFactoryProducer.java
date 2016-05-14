package it.eup.loganalyser.cdi;

import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import it.eup.loganalyser.config.Constants;

@ApplicationScoped
public class EntityManagerFactoryProducer {

	public static enum Type {
		embedded, tcp
	}

	private Type type = Type.embedded;

	@Produces
	@ApplicationScoped
	public EntityManagerFactory createEntityManagerFactory() {
		Properties properties = buildProperties();
		return Persistence.createEntityManagerFactory("loggingPu", properties);
	}

	public void closeEntityManagerFactory(@Disposes EntityManagerFactory entityManagerFactory) {
		entityManagerFactory.close();
	}

	private Properties buildProperties() {
		Properties properties = new Properties();

		if (type == Type.tcp) {
			properties.put("javax.persistence.jdbc.url", Constants.H2_TCPSERVER_CONNECTION_URL);
		} else {
			properties.put("javax.persistence.jdbc.url", Constants.H2_EMBEDDED_CONNECTION_URL);
		}

		return properties;
	}

	public void setType(Type type) {
		this.type = type;
	}

}
