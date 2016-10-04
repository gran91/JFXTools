package com.kles;

import com.kles.fx.custom.DigitalClock;
import com.kles.fx.custom.FxUtil;
import com.kles.model.AbstractDataModel;
import com.kles.model.Language;
import com.kles.tray.SystemTray;
import com.kles.view.AbstractDataModelEditController;
import com.kles.view.ModelManagerTableViewController;
import java.awt.TrayIcon;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import resources.Resource;

public class MainApp extends Application {

    public TrayIcon trayIcon;
    public Stage primaryStage;
    public Locale locale;
    public Properties configProp;
    public final StringProperty title = new SimpleStringProperty(Resource.TITLE);
    public final StringProperty dirData = new SimpleStringProperty();
    public static final String configFileName = "config";
    public static ResourceBundle resourceMessage;
    public static String SKIN = "skin";
    public static String LANGUAGE = "lang";
    public static final ObservableList<Language> listLanguage = FXCollections.observableArrayList();
    public Preferences prefs = Preferences.userRoot().node("3KLES_" + Resource.TITLE);
    private HashMap<String, ObservableList> dataMap;
    private final DigitalClock clock = new DigitalClock(DigitalClock.CLOCK);
    private final LinkedHashMap<String, String> listSkin = new LinkedHashMap<>();
    public static final Image LOGO_IMAGE = new Image(MainApp.class.getResourceAsStream("/resources/images/logo.png"));

    /**
     * Constructor
     */
    public MainApp() {
        clock.start();
        if (prefs.get(LANGUAGE, null) == null) {
            prefs.put(LANGUAGE, Locale.getDefault().toString());
        } else {
            Locale.setDefault(new Locale(prefs.get(LANGUAGE, null).split("_")[0], prefs.get(LANGUAGE, null).split("_")[1]));
        }
        locale = Locale.getDefault();
        resourceMessage = ResourceBundle.getBundle("resources/language", Locale.getDefault());
        dataMap = new HashMap();
        loadSkins();
        if (prefs.get(SKIN, null) == null) {
            prefs.put(SKIN, STYLESHEET_MODENA);
        }
        Application.setUserAgentStylesheet(prefs.get(SKIN, null));
    }

    public static ObservableList<Language> loadLanguages() {
        if (listLanguage.isEmpty()) {
            for (String l : Locale.getISOCountries()) {
                listLanguage.add(new Language(l));
            }
        }
        return listLanguage;
    }

    @Override
    public void start(Stage primaryStage) {
        title.bind(Bindings.concat(Resource.TITLE).concat("\t").concat(clock.getTimeText()));
        this.primaryStage = primaryStage;
        this.primaryStage.titleProperty().bind(title);
        this.primaryStage.getIcons().addAll(Resource.LOGO_ICON_128, Resource.LOGO_ICON_64, Resource.LOGO_ICON_32, Resource.LOGO_ICON_16);
        loadView();
    }

    public void loadView() {
    }

    public void addSystemTray() {
        SystemTray.addSystemTray(this);
    }

    private void loadSkins() {
        listSkin.put("CASPIAN", STYLESHEET_CASPIAN);
        listSkin.put("MODENA", STYLESHEET_MODENA);
        listSkin.put("DarkTheme", "com/kles/css/DarkTheme.css");
        listSkin.put("Windows 7", "com/kles/css/Windows7.css");
        listSkin.put("JMetroDarkTheme", "com/kles/css/JMetroDarkTheme.css");
        listSkin.put("JMetroLightTheme", "com/kles/css/JMetroLightTheme.css");
    }

    public void clearData() {
        dataMap.clear();
    }

    public void addToDataMap(String name, ObservableList data) {
        dataMap.put(name, data);
    }

    public File getDataDirectoryPath() {
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            Path p = Paths.get("data");
            System.out.println(p.toFile().getAbsolutePath());
            if (p.toFile().exists() && p.toFile().isDirectory()) {

            } else {
                p.toFile().mkdirs();
            }
            return p.toFile();
        }
    }

    public void setRegistryFilePath(File file) {
        if (file != null) {
            prefs.put("filePath", file.getAbsolutePath());
            dirData.set(" - " + file.getAbsolutePath());
        } else {
            prefs.remove("filePath");
            dirData.set("");
        }
    }

    public void loadDataDirectory(File directory) {
        JAXBContext context;
        Unmarshaller um;
        try {
            if (directory.exists() && directory.isDirectory()) {
                File[] listFile = directory.listFiles();
                for (File f : listFile) {
                }
            }
            setRegistryFilePath(directory);
        } catch (Exception e) {
            FxUtil.showAlert(Alert.AlertType.ERROR, "Error", "Could not load data from file:\n" + directory.getPath(), e.getLocalizedMessage());
        }
    }

    public void saveDataToFile(File file) {
        try {
            setRegistryFilePath(file);
        } catch (Exception e) {
            FxUtil.showAlert(Alert.AlertType.ERROR, "Error", "Could not save data to file:\n" + file.getPath(), e.getLocalizedMessage());
        }
    }

    public boolean showDataModelEditDialog(AbstractDataModel model, ResourceBundle rb) {
        return showDataModelEditDialog(model, primaryStage, rb);
    }

    public boolean showDataModelEditDialog(AbstractDataModel model) {
        return showDataModelEditDialog(model, primaryStage, null);
    }

    public boolean showDataModelEditDialog(AbstractDataModel model, Window parent) {
        return showDataModelEditDialog(model, parent, null);
    }

    public boolean showDataModelEditDialog(AbstractDataModel model, Window parent, ResourceBundle rb) {
        try {
            FXMLLoader loader = new FXMLLoader();
            if (rb == null) {
                loader.setResources(ResourceBundle.getBundle("resources.language", this.getLocale()));
            } else {
                loader.setResources(rb);
            }
            loader.setLocation(MainApp.class.getResource("view/" + model.datamodelName() + "EditDialog.fxml"));
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            String title = "";
            try {
                title = loader.getResources().getString(model.datamodelName().toLowerCase() + ".title");
            } catch (Exception e) {
            }
            dialogStage.setTitle(title);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(parent);
            dialogStage.getIcons().add(Resource.LOGO_ICON_32);
            Scene scene = new Scene(page);
            scene.getStylesheets().add(MainApp.class.getResource("application.css").toExternalForm());
            dialogStage.setScene(scene);
            /*UndecoratorScene scene = new UndecoratorScene(dialogStage, page);
             scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
             dialogStage.setScene(scene);
             scene.setFadeInTransition();
             dialogStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
             @Override
             public void handle(WindowEvent we) {
             we.consume();   // Do not hide yet
             scene.setFadeOutTransition();
             }
             });
             Undecorator undecorator = scene.getUndecorator();
             dialogStage.setMinWidth(undecorator.getMinWidth());
             dialogStage.setMinHeight(undecorator.getMinHeight());
             */
            AbstractDataModelEditController controller = loader.getController();
            controller.setMainApp(this);
            controller.setDialogStage(dialogStage);
            controller.setDataModel(model);
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public AbstractDataModelEditController showDataModelEditDialogStage(AbstractDataModel model) {
        return showDataModelEditDialogStage(model, primaryStage);
    }

    public AbstractDataModelEditController showDataModelEditDialogStage(AbstractDataModel model, Window parent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(ResourceBundle.getBundle("resources.language", this.getLocale()));
            loader.setLocation(MainApp.class.getResource("view/" + model.datamodelName() + "EditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(MainApp.resourceMessage.getString(model.datamodelName().toLowerCase() + ".title"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(parent);
            dialogStage.getIcons().add(Resource.LOGO_ICON_32);
            Scene scene = new Scene(page);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            dialogStage.setScene(scene);
            /*UndecoratorScene scene = new UndecoratorScene(dialogStage, page);
             scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
             dialogStage.setScene(scene);
             scene.setFadeInTransition();
             dialogStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
             @Override
             public void handle(WindowEvent we) {
             we.consume();   // Do not hide yet
             scene.setFadeOutTransition();
             }
             });
             Undecorator undecorator = scene.getUndecorator();
             dialogStage.setMinWidth(undecorator.getMinWidth());
             dialogStage.setMinHeight(undecorator.getMinHeight());
             */
            AbstractDataModelEditController controller = loader.getController();
            controller.setMainApp(this);
            controller.setDialogStage(dialogStage);
            controller.setDataModel(model);
            return controller;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void showModelManagerTableView(String datamodel) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(ResourceBundle.getBundle("resources.language", this.getLocale()));
            loader.setLocation(MainApp.class.getResource("view/" + datamodel + "Overview.fxml"));
            AnchorPane modelManagerOverview = (AnchorPane) loader.load();

            Stage stage = new Stage();
            stage.setTitle(this.getResourceMessage().getString(datamodel.toLowerCase() + ".title"));
            stage.initModality(Modality.NONE);
            stage.initOwner(this.getPrimaryStage());
            stage.getIcons().add(Resource.LOGO_ICON_32);
            Scene scene = new Scene(modelManagerOverview);
            stage.setScene(scene);
            /*UndecoratorScene scene = new UndecoratorScene(stage, modelManagerOverview);
             stage.setScene(scene);
             scene.setFadeInTransition();
             stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
             @Override
             public void handle(WindowEvent we) {
             we.consume();   // Do not hide yet
             scene.setFadeOutTransition();
             }
             });*/

            ModelManagerTableViewController controller = loader.getController();
            controller.setMainApp(this);

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * **
     */
    /**
     * Returns the main stage.
     *
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public ResourceBundle getResourceMessage() {
        return resourceMessage;
    }

    public void setResourceMessage(ResourceBundle resourceMessage) {
        MainApp.resourceMessage = resourceMessage;
    }

    public HashMap<String, ObservableList> getDataMap() {
        return dataMap;
    }

    public void setDataMap(HashMap<String, ObservableList> dataMap) {
        this.dataMap = dataMap;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public LinkedHashMap<String, String> getListSkin() {
        return listSkin;
    }

    public TrayIcon getTrayIcon() {
        return trayIcon;
    }

    public void setTrayIcon(TrayIcon trayIcon) {
        this.trayIcon = trayIcon;
    }

    public static void main(String[] args) {
        launch(args);
    }

    static {
        loadLanguages();
    }
}
