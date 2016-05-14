package it.eup.loganalyser.events;

public class ImportErrorEvent {

	private String filename;
	private Throwable throwable;

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Throwable getThrowable() {
		return throwable;
	}

	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}

	public static ImportErrorEvent createNew(String filename, Throwable t) {
		ImportErrorEvent event = new ImportErrorEvent();
		event.setFilename(filename);
		event.setThrowable(t);
		return event;
	}
}
