package com.kles.view;

import com.kles.fx.custom.FxUtil;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import com.kles.MainApp;
import com.kles.model.AbstractDataModel;
import java.util.ResourceBundle;

/**
 * Dialog to edit details of a environment.
 *
 * @author Jérémy Chaut
 */
public class AbstractDataModelEditController {

    @FXML
    protected GridPane grid;
    protected MainApp mainApp;
    protected Stage dialogStage;
    protected AbstractDataModel datamodel;
    protected AbstractDataModel parentmodel;
    protected ResourceBundle resourceBundle;
    protected BooleanProperty hasError = new SimpleBooleanProperty(false);
    protected BooleanProperty okClicked = new SimpleBooleanProperty(false);
    protected String errorMessage = "";
    protected Map<BooleanBinding, String> messages = new LinkedHashMap<>();

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {

    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setMainApp(MainApp main) {
        mainApp = main;
        resourceBundle = mainApp.getResourceBundle();
        setBooleanMessage();
    }

    /**
     * Sets the datamodel to be edited in the dialog.
     *
     * @param datamodel
     */
    public void setDataModel(AbstractDataModel datamodel) {
        this.datamodel = datamodel;
    }

    public void setBooleanMessage() {
    }

    public AbstractDataModel getParentmodel() {
        return parentmodel;
    }

    public void setParentmodel(AbstractDataModel parentmodel) {
        this.parentmodel = parentmodel;
    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     *
     * @return
     */
    public boolean isOkClicked() {
        return okClicked.get();
    }

    public BooleanProperty isClickedProperty() {
        return okClicked;
    }

    public boolean hasError() {
        return hasError.get();
    }

    public BooleanProperty hasErrorProperty() {
        return hasError;
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            saveData();
            closeDialog();
        }
    }

    public void saveData() {

    }

    public void closeDialog() {
        okClicked.set(true);
        dialogStage.close();
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Validates the user input in the text fields.
     *
     * @return true if the input is valid
     */
    public boolean isInputValid() {
        errorMessage = "";
        if (messages.size() > 0) {
            Iterator<Entry<BooleanBinding, String>> it = messages.entrySet().iterator();
            while (it.hasNext()) {
                Entry<BooleanBinding, String> temp = it.next();
                if (temp.getKey().get()) {
                    errorMessage += temp.getValue() + "\n";
                }

            }
        }
        if (errorMessage.length() == 0) {
            hasError.set(false);
            return true;
        } else {
            FxUtil.showAlert(Alert.AlertType.WARNING, MainApp.resourceMessage.getString("main.controlData"), MainApp.resourceMessage.getString("main.invalidData"), errorMessage);
            hasError.set(true);
            return false;
        }
    }
}
