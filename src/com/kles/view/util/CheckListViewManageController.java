package com.kles.view.util;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;
import org.controlsfx.control.CheckListView;
import com.kles.MainApp;
import javafx.scene.layout.VBox;

public class CheckListViewManageController<T extends Object> {

    @FXML
    public VBox root;
    @FXML
    public Label title, label;
    @FXML
    public ProgressIndicator indicator;
    @FXML
    public Button bSelect, bUnselect;
    @FXML
    public CheckListView<T> list;

    private BooleanProperty isDisable, isLeastOne, isOnControl;
    private final ObservableList<T> listData = FXCollections.observableArrayList();

    private MainApp mainApp;
    private Stage stage;

    public CheckListViewManageController() {

    }

    @FXML
    private void initialize() {
        list.setItems(listData);
        isDisable = new SimpleBooleanProperty(true);
        isLeastOne = new SimpleBooleanProperty(false);
        isOnControl = new SimpleBooleanProperty(false);

        list.disableProperty().bind(isDisable);
        bSelect.disableProperty().bind(isDisable);
        bUnselect.disableProperty().bind(isDisable);
        indicator.visibleProperty().bind(isOnControl);
        list.getCheckModel().getCheckedItems().addListener((ListChangeListener.Change<? extends T> c) -> {
            isLeastOne.set(!list.getCheckModel().getCheckedIndices().isEmpty());
        });
    }

    @FXML
    private void listChecker(ActionEvent e) {
        if (e.getSource().equals(bSelect)) {
            selectAll();
        } else if (e.getSource().equals(bUnselect)) {
            unselectAll();
        }
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void selectAll() {
        if (list != null) {
            list.getCheckModel().checkAll();
        }
    }

    public void unselectAll() {
        if (list != null) {
            list.getCheckModel().clearChecks();
        }
    }

    public String getTitle() {
        return title.getText();
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public String getLabel() {
        return label.getText();
    }

    public void setLabel(String label) {
        this.label.setText(label);
    }

    public ProgressIndicator getIndicator() {
        return indicator;
    }

    public void setIndicator(ProgressIndicator indicator) {
        this.indicator = indicator;
    }

    public CheckListView<T> getList() {
        return list;
    }

    public void setList(CheckListView<T> list) {
        this.list = list;
    }

    public BooleanProperty getIsDisable() {
        return isDisable;
    }

    public void setIsDisable(BooleanProperty isDisable) {
        this.isDisable = isDisable;
    }

    public BooleanProperty getIsLeastOne() {
        return isLeastOne;
    }

    public void setIsLeastOne(BooleanProperty isLeastOne) {
        this.isLeastOne = isLeastOne;
    }

    public ObservableList<T> getListData() {
        return listData;
    }

    public VBox getRoot() {
        return root;
    }
}
