package it.eup.loganalyser.dao;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

import it.eup.loganalyser.entity.LogDataRow;

@ApplicationScoped
public class LogDataRepository {

	@PersistenceContext
	private EntityManager entityManager;

	public LogDataRepository() {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("loggingPu");
		entityManager = factory.createEntityManager();
	}

	public void persist(LogDataRow dataRow) {
		entityManager.getTransaction().begin();
		try {
			entityManager.persist(dataRow);
		} catch (RuntimeException e) {
			throw e;
		}
		entityManager.getTransaction().commit();
	}
}
