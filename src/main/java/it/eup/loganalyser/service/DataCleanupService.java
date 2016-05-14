package it.eup.loganalyser.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import it.eup.loganalyser.dao.OriginDao;
import it.eup.loganalyser.entity.LogOrigin;
import it.eup.loganalyser.model.OriginOverviewModel;

@ApplicationScoped
public class DataCleanupService {

	@Inject
	OriginDao originDao;

	@Inject
	EntityManager entityManager;

	public void deleteOrigins(List<Long> ids) {
		if (ids == null) {
			return;
		}

		originDao.deleteOrigins(ids);
	}

	public List<OriginOverviewModel> findAllOrigins() {
		List<LogOrigin> entries = originDao.getAllLogOrigins();
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
