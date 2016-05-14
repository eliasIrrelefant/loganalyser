package it.eup.loganalyser.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;

import org.apache.commons.io.IOUtils;

import it.eup.loganalyser.entity.LogDataRow;
import it.eup.loganalyser.entity.LogOrigin;
import it.eup.loganalyser.events.ImportDoneEvent;
import it.eup.loganalyser.events.ImportErrorEvent;
import it.eup.loganalyser.events.ImportInterruptedException;
import it.eup.loganalyser.importfilter.Filterable;
import it.eup.loganalyser.logging.Logger;
import it.eup.loganalyser.logparser.HttpdLogParser;
import it.eup.loganalyser.model.ConfigModel;
import it.eup.loganalyser.model.StatisticsData;
import it.eup.loganalyser.service.ConfigService;

@Dependent
public class LogFileImporter {

    @Inject
    private ConfigService configService;
    
	@Inject
	private EntityManager entityManager;

	@Inject
	Event<ImportDoneEvent> importDoneEvent;

	@Inject
	Event<ImportErrorEvent> importErrorEvent;

	boolean interrupted = false;

	public StatisticsData process(final InputStream inputStream, final String origin, Filterable filter) throws IOException {
		try {
			StatisticsData statistics = processInternal(inputStream, origin, filter);
			importDoneEvent.fire(ImportDoneEvent.createNew(origin));
			return statistics;
		} catch (ImportInterruptedException e) {
			throw e;
		} catch (Exception e) {
			importErrorEvent.fire(ImportErrorEvent.createNew(origin, e));
			return null;
		}
	}

	private StatisticsData processInternal(InputStream inputStream, String origin, Filterable filter) throws IOException {
	    ConfigModel defaultEntries = configService.getDefaultEntries();
        String logFormat = defaultEntries.getLogformat();
	    
        HttpdLogParser httpdLogParser = new HttpdLogParser(logFormat);
        
		interrupted = false;

		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

		if (entityManager.getTransaction().isActive()) {
			entityManager.getTransaction().rollback();
		}

		entityManager.clear();

		entityManager.getTransaction().begin();
		entityManager.setFlushMode(FlushModeType.AUTO);

		String line = null;
		StatisticsData statistics = new StatisticsData();

		long start = System.currentTimeMillis();
		long time = System.currentTimeMillis();

		LogOrigin logFileOrigin = new LogOrigin();
		logFileOrigin.setPath(origin);
		entityManager.persist(logFileOrigin);

		try {
			while (interrupted == false && (line = reader.readLine()) != null) {
			    statistics.incrementCount();

				if (statistics.getCount() % 5000 == 0) {
					long duration = System.currentTimeMillis() - time;
					Logger.log("Fortschritt: Zeile {0} ({1}ms)", statistics.getCount(), duration);
					time = System.currentTimeMillis();
				}

				
                LogDataRow data = httpdLogParser.parseLine(line);

				if (data == null) {
					statistics.incrementError();
					continue;
				}

				if (filter.isValid(data) == false) {
					statistics.incrementFiltered();
					continue;
				}

				statistics.incrementSuccess();

				data.setOrigin(logFileOrigin);

				entityManager.persist(data);

				if (statistics.getSuccess() % 50 == 0) {
					entityManager.flush();
					entityManager.clear();
				}

				if (statistics.getSuccess() % 1000 == 0) {
					entityManager.getTransaction().commit();
					entityManager.getTransaction().begin();
				}
			}
		} catch (Exception e) {
			Logger.log("{0}: {1}", statistics.getCount(), line, e);
			entityManager.getTransaction().commit();
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(reader);
		}
		
		logFileOrigin = entityManager.find(LogOrigin.class, logFileOrigin.getId());
		logFileOrigin.setTotalCount(statistics.getCount());
		logFileOrigin.setSuccessCount(statistics.getSuccess());
		logFileOrigin.setErrorsCount(statistics.getError());
		logFileOrigin.setFilteredCount(statistics.getFiltered());
		
		entityManager.getTransaction().commit();

		long duration = System.currentTimeMillis() - start;
		statistics.setDuration(duration);

		if (interrupted) {
			Logger.log("Import unterbrochen. Bisher importiert: {0}.", statistics.getCount());
		} else {
			Logger.log("Import fertig.");
		}

		Logger.log(statistics.getStatisticsString());

		if (interrupted == true) {
			throw new ImportInterruptedException(statistics);
		}
		
		return statistics;
	}

	public void clearTables() {
		Logger.log("Clearing Table... ");
		entityManager.getTransaction().begin();
		entityManager.createQuery("delete from " + LogDataRow.class.getSimpleName()).executeUpdate();
		entityManager.getTransaction().commit();
		Logger.log("[DONE]");
	}

	public void interrupt() {
		this.interrupted = true;
	}

}
