package it.eup.loganalyser.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "origin")
public class LogOrigin {

  @Id
  @GeneratedValue
  private Long id;

  @Column(length = 1024, nullable = false)
  private String path;

  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date importDate = new Date();

  @Column(name = "total_count")
  private Long totalCount;

  @Column(name = "error_count")
  private Long errorsCount;

  @Column(name = "imported_count")
  private Long successCount;

  @Column(name = "filtered_count")
  private Long filteredCount;

}
