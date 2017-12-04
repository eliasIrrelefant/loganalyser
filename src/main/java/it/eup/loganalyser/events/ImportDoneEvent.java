package it.eup.loganalyser.events;

public class ImportDoneEvent {

  private String filename;

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public static ImportDoneEvent createNew(String filename) {
    ImportDoneEvent event = new ImportDoneEvent();
    event.setFilename(filename);
    return event;
  }
}
