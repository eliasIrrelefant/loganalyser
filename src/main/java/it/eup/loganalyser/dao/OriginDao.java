package it.eup.loganalyser.dao;

import java.util.Collection;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import it.eup.loganalyser.entity.LogDataRow;
import it.eup.loganalyser.entity.LogDataRow_;
import it.eup.loganalyser.entity.LogOrigin;
import it.eup.loganalyser.entity.LogOrigin_;

@ApplicationScoped
public class OriginDao {

	@Inject
	EntityManager entityManager;

	public List<LogOrigin> getAllLogOrigins() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<LogOrigin> criteriaQuery = builder.createQuery(LogOrigin.class);
		Root<LogOrigin> root = criteriaQuery.from(LogOrigin.class);

		criteriaQuery.orderBy(builder.asc(root.get(LogOrigin_.path)));
		
		List<LogOrigin> resultList = entityManager.createQuery(criteriaQuery).getResultList();

		return resultList;
	}

	public void deleteOrigins(List<Long> ids) {
		entityManager.getTransaction().begin();

		deleteFromRowData(ids);
		deleteFromOrigin(ids);

		entityManager.getTransaction().commit();
	}

	private void deleteFromOrigin(List<Long> ids) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaDelete<LogOrigin> originDelete = builder.createCriteriaDelete(LogOrigin.class);

		Root<LogOrigin> originRoot = originDelete.from(LogOrigin.class);
		originDelete.where(originRoot.get(LogOrigin_.id).in(ids));

		entityManager.createQuery(originDelete).executeUpdate();
	}

	private void deleteFromRowData(Collection<Long> selectedItems) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaDelete<LogDataRow> criteriaDelete = builder.createCriteriaDelete(LogDataRow.class);

		Root<LogDataRow> root = criteriaDelete.from(LogDataRow.class);
		criteriaDelete.where(root.get(LogDataRow_.origin).get(LogOrigin_.id).in(selectedItems));
		entityManager.createQuery(criteriaDelete).executeUpdate();
	}
}
