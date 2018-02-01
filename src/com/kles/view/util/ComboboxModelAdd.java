/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kles.view.util;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;
import com.kles.MainApp;
import com.kles.model.AbstractDataModel;
import com.kles.view.AbstractDataModelEditController;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.util.ResourceBundle;
import javafx.geometry.Pos;
import javafx.scene.layout.Priority;
import resources.ResourceCSS;

/**
 *
 * @author Jeremy.CHAUT
 */
public class ComboboxModelAdd extends HBox {

    protected FontAwesomeIcon imageAdd = FontAwesomeIcon.PLUS;
    @FXML
    protected ComboBox<AbstractDataModel> listModel = new ComboBox();

    @FXML
    protected Button bAdd;

    protected AbstractDataModel model;
    protected MainApp mainApp;
    protected Stage dialogStage;
    protected ObservableList list;
    protected PopOver popOver;
    protected double targetX;
    protected double targetY;
    protected double prefHeight = 35;
    protected double spacing = 5.0d;
    protected Pos position = Pos.CENTER_LEFT;
    protected double prefWidth = 265;
    protected double minWidth = 265;
    protected double maxWidth = Double.MAX_VALUE;
    protected String viewpath = "";

    public ComboboxModelAdd() {
        FontAwesomeIconView fAdd = new FontAwesomeIconView(imageAdd);
//        fAdd.setStyle(ResourceCSS.SUCCESS_STYLE);
        fAdd.setFill(ResourceCSS.sucessFill());
        fAdd.setSize("15");
        bAdd = new Button("", fAdd);
        this.setSpacing(5.0d);
        HBox.setHgrow(listModel, Priority.ALWAYS);
        this.getChildren().add(listModel);
        this.getChildren().add(bAdd);
    }

    public ComboboxModelAdd(MainApp main, ObservableList l, AbstractDataModel m) {
        this();
        mainApp = main;
        list = l;
        model = m;
        init(mainApp.getResourceBundle());
    }

    public void init() {
        init(mainApp.getResourceBundle());
    }

    public void init(ResourceBundle rb) {
        this.setPrefHeight(prefHeight);
        this.setSpacing(spacing);
        this.setAlignment(position);
        listModel.setMinWidth(minWidth);
        listModel.setPrefWidth(prefWidth);
        if (rb.containsKey(model.datamodelName().toLowerCase() + ".select")) {
            listModel.setPromptText(rb.getString(model.datamodelName().toLowerCase() + ".select"));
        }
        listModel.setItems(list);
        listModel.setCellFactory((ListView<AbstractDataModel> param) -> new ListCell<AbstractDataModel>() {
            @Override
            public void updateItem(AbstractDataModel item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item.toString());
                } else {
                    setText(null);
                }
            }
        });
        bAdd.setOnAction((ActionEvent event) -> {
            addModel();
        });
    }

    public void addModel() {
        try {
            if (popOver != null && popOver.isShowing()) {
                popOver.hide(Duration.ZERO);
            }
            com.sun.glass.ui.Robot robot = com.sun.glass.ui.Application.GetApplication().createRobot();
            int x = robot.getMouseX();
            int y = robot.getMouseY();
            targetX = x;
            targetY = y;
            popOver = createPopOver();
            boolean autoPosition = false;
            if (autoPosition) {
                popOver.show(this);
            } else {
                popOver.show(this, targetX, targetY);
            }
        } catch (IOException ex) {
            Logger.getLogger(ComboboxModelAdd.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected PopOver createPopOver() throws IOException {
        return createPopOver(model.newInstance());
    }

    protected PopOver createPopOver(AbstractDataModel model) throws IOException {
        popOver = new PopOver();
        popOver.setDetachable(true);
        popOver.setDetached(false);
        popOver.setArrowSize(10);
        if (!viewpath.isEmpty()) {
            viewpath = "view/" + model.datamodelName() + "EditDialog.fxml";
        }

        AbstractDataModelEditController controller = mainApp.showDataModelEditDialogStage(model, viewpath, this.getScene().getWindow(), mainApp.getResourceBundle());

        popOver.setContentNode(controller.getDialogStage().getScene().getRoot());
        controller.isOkClickedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue && !controller.hasError()) {
                if (!list.contains(model)) {
                    list.add(model);
                }
                popOver.hide();
            } else {
                popOver.show(this);
            }
        });

        controller.isCancelClickedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            popOver.hide();
        });
        return popOver;
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Sets the customer list to the combobox
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public AbstractDataModel getModel() {
        return model;
    }

    public void setModel(AbstractDataModel model) {
        this.model = model;
    }

    public ObservableList getList() {
        return list;
    }

    public void setList(ObservableList list) {
        this.list = list;
        this.listModel.setItems(this.list);
    }

    public ComboBox getListModel() {
        return listModel;
    }

    public void setValue(AbstractDataModel m) {
        listModel.setValue(m);
    }

    public void setViewPath(String viewpath) {
        this.viewpath = viewpath;
    }
}
