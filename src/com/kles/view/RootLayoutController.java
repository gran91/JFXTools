package com.kles.view;

import com.kles.fx.custom.FxUtil;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.kles.MainApp;
import com.kles.model.AbstractDataModel;
import resources.Resource;

/**
 * The controller for the root layout. The root layout provides the basic
 * application layout containing a menu bar and space where other JavaFX
 * elements can be placed.
 *
 * @author Jérémy Chaut
 */
public class RootLayoutController {

    @FXML
    private RadioMenuItem menuFR;
    @FXML
    private RadioMenuItem menuEN;
    @FXML
    private Menu langmenu, skinmenu;
    private MainApp mainApp;
    private Stage environmentStage;

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        this.mainApp.getListSkin().entrySet().stream().forEach((nameSkin) -> {
            final CheckMenuItem menuItemSkin = new CheckMenuItem(nameSkin.getKey());
            menuItemSkin.setSelected(this.mainApp.prefs.get(MainApp.SKIN, null).equals(nameSkin.getKey()));
            menuItemSkin.setOnAction(e -> {
                Application.setUserAgentStylesheet(nameSkin.getValue());
                this.mainApp.prefs.put(MainApp.SKIN, nameSkin.getKey());
                skinmenu.getItems().stream().forEach(m -> {
                    ((CheckMenuItem) m).setSelected(m.getText().equals(nameSkin.getKey()));
                });
            });
            skinmenu.getItems().add(menuItemSkin);
        });
        langmenu.getItems().stream().forEach(m -> {
            ((RadioMenuItem) m).setSelected(this.mainApp.prefs.get(MainApp.LANGUAGE, null).equals(m.getId()));
            m.setOnAction(e -> {
                Locale.setDefault(new Locale(m.getId()));
                this.mainApp.prefs.put(MainApp.LANGUAGE, m.getId());
                langmenu.getItems().stream().forEach(m1 -> {
                    ((RadioMenuItem) m).setSelected(m1.getId().equals(this.mainApp.prefs.get(MainApp.LANGUAGE, null)));
                });
                try {
                    mainApp.getPrimaryStage().close();
                } catch (Exception ex) {
                    Logger.getLogger(RootLayoutController.class.getName()).log(Level.SEVERE, null, ex);
                }
                new MainApp().start(new Stage());
            });
        });
    }

    /**
     * Creates an empty address book.
     */
    @FXML
    private void handleNew() {
        mainApp.clearData();
        mainApp.setRegistryFilePath(null);
    }

    /**
     * Opens a FileChooser to let the user select an address book to load.
     */
    @FXML
    private void handleOpen() {
        DirectoryChooser fileChooser = new DirectoryChooser();
        File file = fileChooser.showDialog(mainApp.getPrimaryStage());
        if (file != null) {
            mainApp.loadDataDirectory(file);
        }
    }

    /**
     * Saves the file to the person file that is currently open. If there is no
     * open file, the "save as" dialog is shown.
     */
    @FXML
    private void handleSave() {
        File directory = mainApp.getDataDirectoryPath();
        if (directory != null) {
            mainApp.saveDataToFile(directory);
        } else {
            handleSaveAs();
        }
    }

    /**
     * Opens a FileChooser to let the user select a file to save to.
     */
    @FXML
    private void handleSaveAs() {
        DirectoryChooser fileChooser = new DirectoryChooser();
        File file = fileChooser.showDialog(mainApp.getPrimaryStage());
        if (file != null) {
            mainApp.saveDataToFile(file);
        }
    }

    /**
     * Opens an about dialog.
     */
    @FXML
    private void handleAbout() {
        FxUtil.showAlert(Alert.AlertType.INFORMATION, mainApp.getResourceMessage().getString("about.title"), String.format(mainApp.getResourceMessage().getString("about.header"), Resource.VERSION), String.format(mainApp.getResourceMessage().getString("about.text"), Resource.VERSION));
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        handleSave() ;
        System.exit(0);
    }

    /**
     * Opens a dialog to edit details for the specified datamodel. If the user
     * clicks OK, the changes are saved into the provided datamodel object and
     * true is returned.
     *
     * @param model the datamodel object to be edited
     * @return true if the user clicked OK, false otherwise.
     */
    public boolean showDataModelEditDialog(AbstractDataModel model) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(ResourceBundle.getBundle("resources.language", mainApp.getLocale()));
            loader.setLocation(MainApp.class.getResource("view/" + model.datamodelName() + "EditDialog.fxml"));
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(mainApp.resourceMessage.getString(model.datamodelName().toLowerCase() + ".title"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(environmentStage);
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
            controller.setMainApp(mainApp);
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
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(ResourceBundle.getBundle("resources.language", mainApp.getLocale()));
            loader.setLocation(MainApp.class.getResource("view/" + model.datamodelName() + "EditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(MainApp.resourceMessage.getString(model.datamodelName().toLowerCase() + ".title"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(environmentStage);
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
            controller.setMainApp(mainApp);
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
            loader.setResources(ResourceBundle.getBundle("resources.language", mainApp.getLocale()));
            loader.setLocation(MainApp.class.getResource("view/" + datamodel + "Overview.fxml"));
            AnchorPane modelManagerOverview = (AnchorPane) loader.load();

            Stage stage = new Stage();
            stage.setTitle(mainApp.getResourceMessage().getString(datamodel.toLowerCase() + ".title"));
            stage.initModality(Modality.NONE);
            stage.initOwner(mainApp.getPrimaryStage());
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
            controller.setMainApp(mainApp);

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showEnvironmentOverview() {
        showModelManagerTableView("Environment");
    }
}
