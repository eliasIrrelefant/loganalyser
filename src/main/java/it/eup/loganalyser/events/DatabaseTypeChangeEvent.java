package it.eup.loganalyser.events;

import it.eup.loganalyser.model.DbType;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class DatabaseTypeChangeEvent {

  private final DbType newType;

}
