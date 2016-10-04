package com.kles.view;

import javafx.collections.FXCollections;
import com.kles.fx.custom.FxUtil;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import com.kles.MainApp;
import com.kles.model.AbstractDataModel;

public abstract class ModelManagerTableViewController extends AbstractModelManagerController implements IModelManagerView {

    @FXML
    protected TableView<AbstractDataModel> table;
    @FXML
    protected ContextMenu menu;

    public ModelManagerTableViewController(String dataname) {
        super(dataname);
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        table.setPlaceholder(new Text(MainApp.resourceMessage.getString("main.nodata")));
//        TableViewUtils.addCustomTableMenu(table);
        //TableViewUtils.addHeaderFilter(table);
        loadColumnTable();
    }

    public void loadColumnTable() {

    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    @Override
    public void setMainApp(MainApp mainApp) {
        super.setMainApp(mainApp);
        setData(mainApp.getDataMap().get(datamodelname)==null ? FXCollections.observableArrayList() : mainApp.getDataMap().get(datamodelname));
    }

    @Override
    public <AbstractDataModel> void setData(ObservableList<AbstractDataModel> listData) {
        this.listData = listData;
        table.setItems(this.listData);
    }

     @FXML
    @Override
    public void handleNew() {
        newInstance();
         datamodel.setParent(parentmodel);
        boolean okClicked = mainApp.showDataModelEditDialog(datamodel);
        if (okClicked) {
            listData.add(datamodel);
        }
    }
    
    /**
     * Called when the user clicks on the delete button.
     */
    @FXML
    @Override
    public void handleDelete() {
        int selectedIndex = table.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            table.getItems().remove(selectedIndex);
        } else {
            FxUtil.showAlert(Alert.AlertType.WARNING, MainApp.resourceMessage.getString("main.delete"), MainApp.resourceMessage.getString("main.noselection"), String.format(MainApp.resourceMessage.getString("message.noselection"), MainApp.resourceMessage.getString(datamodelname.toLowerCase() + ".label").toLowerCase()));
        }
    }

    /**
     * Called when the user clicks the new button. Opens a dialog to edit
     * details for a new server.
     */
    @FXML
    @Override
    public void handleCopy() {
        datamodel = table.getSelectionModel().getSelectedItem();
        super.handleCopy();
    }

    /**
     * Called when the user clicks the edit button. Opens a dialog to edit
     * details for the selected datamodel.
     */
    @FXML
    @Override
    public void handleEdit() {
        datamodel = table.getSelectionModel().getSelectedItem();
        super.handleEdit();
    }

    public ContextMenu getMenu() {
        return menu;
    }
    
    class PasswordLabelCell extends TableCell<AbstractDataModel, String> {

        private final Label label;

        public PasswordLabelCell() {
            label = new Label();
            this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            this.setGraphic(null);
        }

        private String genDotString(int len) {
            String dots = "";

            for (int i = 0; i < len; i++) {
                dots += "\u2022";
            }

            return dots;
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                label.setText(genDotString(item.length()));
                setGraphic(label);
            } else {
                setGraphic(null);
            }
        }
    }
}
