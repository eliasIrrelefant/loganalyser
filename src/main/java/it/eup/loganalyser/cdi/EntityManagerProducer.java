package it.eup.loganalyser.cdi;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

@ApplicationScoped
public class EntityManagerProducer {

	@Inject
	private EntityManagerFactory entityManagerFactory;

	@Produces
	@Dependent
	public EntityManager createEntityManager() {
		return entityManagerFactory.createEntityManager();
	}

	public void closeEntityManager(@Disposes EntityManager entityManager) {
		if (entityManager.isOpen()) {
			entityManager.close();
		}
	}

}
