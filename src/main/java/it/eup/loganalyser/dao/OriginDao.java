package it.eup.loganalyser.dao;

import it.eup.loganalyser.entity.LogOrigin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OriginDao extends JpaRepository<LogOrigin, Long> {

}
