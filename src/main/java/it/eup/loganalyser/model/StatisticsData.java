package it.eup.loganalyser.model;

import java.text.MessageFormat;

public class StatisticsData {

    private long count = 0;
    private long error = 0;
    private long success = 0;
    private long filtered = 0;
    private long duration = 0;

	public long getCount() {
		return count;
	}

	public long getError() {
        return error;
    }
	
	public long getFiltered() {
        return filtered;
    }
	
	public long getSuccess() {
        return success;
    }
	
	public long getDuration() {
        return duration;
    }
	
	public void setDuration(long duration) {
        this.duration = duration;
    }
	
	
	public void incrementCount() {
	    count++;
	}
	
	public void incrementError() {
	    error++;
	}
	
	public void incrementSuccess() {
	    success++;
	}
	
	public void incrementFiltered() {
	    filtered++;
	}
	
	public void addAll(StatisticsData data) {
	    count += data.getCount();
	    error += data.getError();
	    filtered += data.getFiltered();
	    success += data.getSuccess();
	    duration += data.getDuration();
	}
	
	public String getStatisticsString() {
	    return MessageFormat.format("Datensätze: {0}\nDavon Importiert: {4}\nDavon gefiltert: {5}\nDavon Fehler: {1}\n\nDauer: {2}ms\nDurchsatz: {3} pro Sekunde",
        count, error, duration, ((float) count / duration * 1000), success, filtered);
	}
}
