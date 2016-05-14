package it.eup.loganalyser.gui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Function;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;

import it.eup.loganalyser.annotations.Initialized;
import it.eup.loganalyser.cdi.FXMLLoaderFactory;
import it.eup.loganalyser.dao.QueryDao;
import it.eup.loganalyser.entity.LogDataRow;
import it.eup.loganalyser.events.ImportDoneEvent;
import it.eup.loganalyser.events.ImportErrorEvent;
import it.eup.loganalyser.events.InterruptImportEvent;
import it.eup.loganalyser.importfilter.ChainableFilter;
import it.eup.loganalyser.importfilter.FilterOptions;
import it.eup.loganalyser.importfilter.Filterable;
import it.eup.loganalyser.importfilter.InputFilterFactory;
import it.eup.loganalyser.importfilter.StringInputFilter;
import it.eup.loganalyser.logging.Logger;
import it.eup.loganalyser.model.ConfigModel;
import it.eup.loganalyser.model.CredentialsModel;
import it.eup.loganalyser.model.QueryModel;
import it.eup.loganalyser.service.ConfigService;
import it.eup.loganalyser.service.H2DatabaseServerManager;
import it.eup.loganalyser.service.ImportService;
import it.eup.loganalyser.service.TemplateService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;

@ApplicationScoped
public class MainController {

    @FXML
    TextArea urls;

    @FXML
    TextField username;

    @FXML
    TextField password;

    @FXML
    TextArea textArea;

    @FXML
    TableView<ObservableMap<String, Object>> queryResultTable;

    @FXML
    private TextArea log;

    @FXML
    private TextArea query;

    @FXML
    private BorderPane rootPane;

    @FXML
    WebView webView;

    @FXML
    VBox logBox;

    @FXML
    VBox webConsoleBox;

    @FXML
    Menu queriesMenu;

    @FXML
    GridPane filters;

    @Inject
    private Window rootStage;

    @Inject
    private QueryDao queryDao;

    @Inject
    ImportAsyncRunner importAsyncRunner;

    @Inject
    ImportService importService;

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    @Inject
    CredentialsModel credentialsModel;

    @Inject
    Event<LoggingEvent> logEvent;

    @Inject
    H2DatabaseServerManager h2DatabaseServerManager;

    @Inject
    ConfigService configService;

    @Inject
    Event<InterruptImportEvent> interruptEvent;

    @Inject
    TemplateService templateService;

    List<GuiFilterEntry> filterEntries = new ArrayList<GuiFilterEntry>();

    @FXML
    protected void executeQuery() {
        try {
            String queryText = query.getText();
            List<Map<String, Object>> data = null;

            if (StringUtils.isNotBlank(queryText)) {
                data = queryDao.query(queryText);
            }

            updateTable(data);
        } catch (Exception e) {
            new DialogBuilder().createExceptionDialog("Fehler", "Es ist ein Fehler aufgetreten", e).build().showAndWait();
        }
    }

    private void updateTable(List<Map<String, Object>> data) {
        if (data == null || data.isEmpty()) {
            queryResultTable.getColumns().clear();
            queryResultTable.setItems(null);
            return;
        }

        ObservableList<ObservableMap<String, Object>> list = FXCollections.observableArrayList();

        for (Map<String, Object> map : data) {
            ObservableMap<String, Object> row = FXCollections.observableMap(map);
            list.add(row);
        }

        double width = queryResultTable.getWidth();
        double prefAverageWidth = (width - 18) / data.get(0).size();

        ObservableList<TableColumn<ObservableMap<String, Object>, ?>> tableColumns = queryResultTable.getColumns();
        tableColumns.clear();
        for (String key : data.get(0).keySet()) {
            TableColumn<ObservableMap<String, Object>, ?> column = new TableColumn<ObservableMap<String, Object>, Object>(key);
            MapValueFactory mapValueFactory = new MapValueFactory<CellDataFeatures<ObservableMap<String, Object>, ?>>(key);
            column.setCellValueFactory(mapValueFactory);
            column.setMinWidth(20);
            column.setPrefWidth(prefAverageWidth);
            tableColumns.add(column);
        }

        queryResultTable.setItems(list);
    }

    @FXML
    protected void startImport(ActionEvent event) {
        String urlString = urls.getText();

        String urlStringExpanded = templateService.merge(urlString);

        if (StringUtils.equals(urlString, urlStringExpanded) == false) {
            urlString = urlStringExpanded;
            Logger.log("URL Template wurde ausgewertet zu:\n{0}", urlStringExpanded);
        }

        if (StringUtils.isBlank(urlString)) {
            new DialogBuilder().createDefaultDialog("Fehler", "Es ist kein Pfad für den Import ausgewählt").build().showAndWait();
            return;
        }

        String lines[] = urlString.split("\\r?\\n");

        doImport(lines);
    }

    @FXML
    protected void resetUrls() {
        urls.clear();
    }

    @FXML
    protected void truncateDatabase() {
        Alert dialog = new DialogBuilder()
                .createDefaultYesNoDialog("Sicher", "Wollen Sie wirklich sämtliche Daten in der Datenbank löschen?").build();

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.get() == ButtonType.YES) {
            queryDao.wipeDatabase();
        }
    }

    protected void doImport(String... urls) {
        try {
            Filterable filter = buildFilter();
            importAsyncRunner.doImport(filter, urls);
        } catch (Exception e) {
            Logger.log(e.getLocalizedMessage(), e);
            new DialogBuilder().createDefaultDialog("Fehler", e.getLocalizedMessage()).build().showAndWait();
        }
    }

    private Filterable buildFilter() {
        ChainableFilter filterChain = new ChainableFilter();

        for (GuiFilterEntry filterEntry : filterEntries) {
            if (filterEntry.isFilterEnabled()) {
                Filterable filter = filterEntry.getFilter();
                filterChain.add(filter);
            }
        }

        return filterChain;
    }

    @FXML
    protected void chooseFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        List<File> files = fileChooser.showOpenMultipleDialog(rootPane.getScene().getWindow());

        Set<String> strings = new LinkedHashSet<String>();
        strings.addAll(Arrays.asList(StringUtils.split(urls.getText(), "\r\n")));

        if (files != null) {
            List<String> joinFilenames = joinFilenames(files);
            strings.addAll(joinFilenames);
        }

        urls.setText(StringUtils.join(strings, "\n"));
    }

    private List<String> joinFilenames(List<File> files) {
        if (files == null) {
            return Collections.emptyList();
        }

        List<String> result = new ArrayList<String>();
        for (File f : files) {
            result.add(f.getAbsolutePath());
        }

        return result;
    }

    @FXML
    protected void clearLog() {
        log.clear();
    }

    @FXML
    protected void querySelected(ActionEvent event) {
        if (event.getSource() instanceof MenuItem) {
            MenuItem item = (MenuItem) event.getSource();

            String queryString = getQueryString(item);

            query.setText(queryString);

            if (h2DatabaseServerManager.isWebServerStarted()) {
                WebEngine webEngine = webView.getEngine();
                Document queryDocument = (Document) webEngine.executeScript("window.frames['h2query'].document");
                WebViewUtils.updateTextAreaValueById(queryDocument, "sql", queryString);
            }
        }
    }

    private String getQueryString(MenuItem item) {
        if (item.getUserData() != null) {
            return item.getUserData().toString();
        } else {
            return item.getText();
        }
    }

    @FXML
    protected void cleanupData() {
        Pane dialog = loadPane("/datacleanup.fxml");

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Datenbereinigung: Auswählen");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(rootStage);

        Scene scene = new Scene(dialog);
        dialogStage.setScene(scene);

        dialogStage.show();
    }

    @FXML
    protected void showSettingsDialog() {
        Pane dialog = loadPane("/settings.fxml");

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Datenbereinigung: Auswählen");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(rootStage);

        Scene scene = new Scene(dialog);
        dialogStage.setScene(scene);

        dialogStage.show();
    }
    
    private Pane loadPane(String path) {
        try {
            InputStream inputStream = getClass().getResourceAsStream(path);
            FXMLLoader fxmlLoader = fxmlLoaderFactory.createLoader();
            return (Pane) fxmlLoader.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void exit() {
        Platform.exit();
    }

    @FXML
    protected void createIndex() {
        queryDao.createIndex();
    }

    @FXML
    protected void dropIndex() {
        queryDao.dropIndex();
    }

    public void handleLogEvent(@Observes LoggingEvent event) {
        Platform.runLater(() -> {
            log.appendText(event.getMessage() + "\n");
        });
    }

    public void handleImportDoneEvent(@Observes ImportDoneEvent importDoneEvent) {
        Logger.log("Import der Datei {0} abgeschlossen.", importDoneEvent.getFilename());

        if (StringUtils.equals(importDoneEvent.getFilename(), urls.getText())) {
            Platform.runLater(() -> {
                urls.clear();
            });
        }
    }

    public void handleImportErrorEvent(@Observes ImportErrorEvent errorEvent) {
        Logger.log("Fehler beim Import der Datei {0}", errorEvent.getFilename(), errorEvent.getThrowable());
    }

    public void initialized(@Observes @Initialized Stage stage) throws Exception {
        loadFxmlAndShowScene(stage);

        Logger.log("Startup done\n");

        initQueryMenu();

        username.textProperty().bindBidirectional(credentialsModel.getUsername());
        password.textProperty().bindBidirectional(credentialsModel.getPassword());

        initWebView();
        
        setupFilterGrid();

        initFilters();
    }

    private void setupFilterGrid() {
        filters.setHgap(5);
        filters.setVgap(5);
        
        for (int i = 0; i < 12; ++i) {
            ColumnConstraints constraints = new ColumnConstraints();
            constraints.setPercentWidth(100.0/12);
            filters.getColumnConstraints().add(constraints);
        }
    }

    private void initFilters() {
        addStringFilter("IP-Adresse", FilterOptions.ALL_STRING_FILTER_OPTIONS, new StringInputFilter(x -> x.getIp()));
        addStringFilter("Pfad", FilterOptions.ALL_STRING_FILTER_OPTIONS, new StringInputFilter(x -> x.getPath()));
        addStringFilter("Query-String", FilterOptions.ALL_STRING_FILTER_OPTIONS, new StringInputFilter(x -> x.getQueryString()));
        addStringFilter("HTTP-Methode", FilterOptions.ALL_STRING_FILTER_OPTIONS, new StringInputFilter(x -> x.getMethod()));
        addStringFilter("User-Agent", FilterOptions.ALL_STRING_FILTER_OPTIONS, new StringInputFilter(x -> x.getUserAgent()));
        addStringFilter("Cipher Name", FilterOptions.ALL_STRING_FILTER_OPTIONS, new StringInputFilter(x -> x.getCipherName()));
        addBooleanFilter("SSL Frontend", x -> x.getSslFrontend());
        addLambdaStringFilter("Advanced Filter");
        
        addFiltersToGrid();
    }

    private void addLambdaStringFilter(String label) {
        GuiFilterEntry entry = new LambdaStringGuiFilterEntry(label);
        filterEntries.add(entry);
    }

    private void addStringFilter(String label, List<String> choices, InputFilterFactory<String> filterFactory) {
        GuiFilterEntry filterEntry = new StringGuiFilterEntry(label, choices, filterFactory);
        filterEntries.add(filterEntry);
    }

    private void addBooleanFilter(String label, Function<LogDataRow, Boolean> valueAccessor) {
        GuiFilterEntry filterEntry = new BooleanGuiFilterEntry(label, valueAccessor);
        filterEntries.add(filterEntry);
    }
    
    private void addFiltersToGrid() {
        for (int i = 0; i < filterEntries.size(); ++i) {
            GuiFilterEntry filter = filterEntries.get(i);

            filter.addToGrid(filters, i);
        }
    }


    private void initQueryMenu() {
        ConfigModel defaultEntries = configService.getDefaultEntries();
        
        if (defaultEntries != null) {
            List<QueryModel> queries = defaultEntries.getQueries();
            updateQueryMenu(queries);
        }
    }

    private void updateQueryMenu(List<QueryModel> queryModels) {
        ObservableList<MenuItem> items = queriesMenu.getItems();
        items.clear();

        for (QueryModel model : queryModels) {
            MenuItem item = new MenuItem();

            Label label = new Label(model.getName());
            label.setTextFill(Color.BLACK);

            String tooltipText = StringUtils.defaultIfBlank(model.getDescription(), "Keine Beschreibung hinterlegt.");
            label.setTooltip(new Tooltip(tooltipText));

            item.setUserData(model.getQuery());
            item.setGraphic(label);

            item.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent arg0) {
                    querySelected(arg0);
                }
            });

            items.add(item);
        }
    }

    private void initWebView() {
        if (h2DatabaseServerManager.isWebServerStarted()) {
            webView.setContextMenuEnabled(false);

            WebEngine webEngine = webView.getEngine();
            webEngine.load("http://localhost:10500/");
            webEngine.documentProperty().addListener(new H2WebConsoleLoginHandler());
        } else {
            webConsoleBox.getChildren().remove(webView);

            Label label = new Label("Die H2-WebConsole steht im Embedded-Mode nicht zur Verfügung.");
            webConsoleBox.setAlignment(Pos.CENTER);
            webConsoleBox.getChildren().add(label);
        }
    }

    private void loadFxmlAndShowScene(Stage stage) throws IOException {
        FXMLLoader fxmll = new FXMLLoader(getClass().getResource("/gui.fxml"), null);

        fxmll.setController(this);

        fxmll.setControllerFactory(new Callback<Class<?>, Object>() {
            @Override
            public Object call(Class<?> param) {
                return CDI.current().select(param).get();
            }
        });

        Parent root = (Parent) fxmll.load();
        Scene scene = new Scene(root);

        stage.setTitle(buildAppTitle());
        stage.setScene(scene);
        stage.setWidth(960);
        stage.show();
    }

    private String buildAppTitle() {
        StringBuilder builder = new StringBuilder("LogAnalyser");

        builder.append(" ").append(getFromVersionBundleWithFallback("app.version", "Unbekannte-Version"));

        builder.append(" (Build: ");
        builder.append(getFromVersionBundleWithFallback("build.date", "unbekannter Build"));
        builder.append(")");

        return builder.toString();
    }

    private String getFromVersionBundleWithFallback(String key, String defaultValue) {
        ResourceBundle bundle = ResourceBundle.getBundle("version");

        try {
            return bundle.getString(key);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    @FXML
    protected void tabChanged(javafx.event.Event e) {
        if (log == null) {
            return;
        }

        Tab source = (Tab) e.getSource();

        if (source == null) {
            return;
        }

        String text = source.getText();

        if (StringUtils.equals("H2-Webconsole", text)) {
            rootPane.setBottom(null);
        } else {
            rootPane.setBottom(logBox);
        }
    }

    @FXML
    protected void interruptImport() {
        interruptEvent.fire(new InterruptImportEvent());
    }

}
