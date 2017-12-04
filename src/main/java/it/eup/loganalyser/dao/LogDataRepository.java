package it.eup.loganalyser.dao;

import it.eup.loganalyser.entity.LogDataRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogDataRepository extends JpaRepository<LogDataRow, Long> {
}
