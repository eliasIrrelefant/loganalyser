package it.eup.loganalyser.service;

import static it.eup.loganalyser.entity.QLogDataRow.logDataRow;
import static it.eup.loganalyser.entity.QLogOrigin.logOrigin;

import com.querydsl.jpa.impl.JPADeleteClause;
import it.eup.loganalyser.dao.OriginDao;
import it.eup.loganalyser.entity.LogOrigin;
import it.eup.loganalyser.model.OriginOverviewModel;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class DataCleanupService {

  @Autowired
  OriginDao originDao;

  @Autowired
  EntityManager entityManager;

  public void deleteOrigins(List<Long> ids) {
    if (ids == null) {
      return;
    }

    new JPADeleteClause(entityManager, logDataRow)
        .where(logDataRow.origin.id.in(ids))
        .execute();

    new JPADeleteClause(entityManager, logOrigin)
        .where(logOrigin.id.in(ids))
        .execute();
  }

  public List<OriginOverviewModel> findAllOrigins() {
    List<LogOrigin> entries = originDao.findAll();
    List<OriginOverviewModel> result = new ArrayList<OriginOverviewModel>();

    for (LogOrigin entity : entries) {
      OriginOverviewModel model = new OriginOverviewModel();

      model.setId(entity.getId());
      model.setPath(entity.getPath());
      model.setImportDate(entity.getImportDate());

      model.setTotalCount(entity.getTotalCount());
      model.setSuccessCount(entity.getSuccessCount());
      model.setErrorsCount(entity.getErrorsCount());
      model.setFilteredCount(entity.getFilteredCount());

      result.add(model);
    }

    return result;
  }

}
