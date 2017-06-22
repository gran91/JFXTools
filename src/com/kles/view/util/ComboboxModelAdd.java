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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import resources.ResourceCSS;

/**
 *
 * @author Jeremy.CHAUT
 */
public class ComboboxModelAdd extends HBox {

    //protected Image imageAdd = new Image(getClass().getResourceAsStream("/resources/images/add.png"));
    protected FontAwesomeIcon imageAdd = FontAwesomeIcon.PLUS;
    //Button button3 = new Button("Accept", new ImageView(imageAdd));
    @FXML
    protected ComboBox<AbstractDataModel> listModel = new ComboBox();

    @FXML
    //protected Button bAdd = new Button("", new ImageView(imageAdd));
    protected Button bAdd;

    protected AbstractDataModel model;
    protected MainApp mainApp;
    protected Stage dialogStage;
    protected ObservableList list;
    protected PopOver popOver;
    protected double targetX;
    protected double targetY;

    public ComboboxModelAdd() {
        FontAwesomeIconView fAdd=new FontAwesomeIconView(imageAdd);
        fAdd.setStyle(ResourceCSS.SUCCESS_STYLE);
        fAdd.setSize("15");
        bAdd = new Button("", fAdd);
        this.setSpacing(5.0d);
        this.getChildren().add(listModel);
        this.getChildren().add(bAdd);
    }

    public ComboboxModelAdd(MainApp main, ObservableList l, AbstractDataModel m) {
        this();
        mainApp = main;
        list = l;
        model = m;
//        this.getChildren().add(listModel);
//        this.getChildren().add(bAdd);
        init(mainApp.getResourceBundle());
    }

    public void init() {
        init(mainApp.getResourceBundle());
    }

    public void init(ResourceBundle rb) {
        this.setPrefHeight(35);
        this.setSpacing(5.0d);
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);
        listModel.setMinWidth(265);
        listModel.setPrefWidth(265);
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
        popOver.setDetachable(false);
        popOver.setDetached(false);
        popOver.setArrowSize(10);
        AbstractDataModelEditController controller = mainApp.showDataModelEditDialogStage(model, this.getScene().getWindow(), mainApp.getResourceBundle());
        popOver.setContentNode(controller.getDialogStage().getScene().getRoot());
        controller.isClickedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue && !controller.hasError()) {
                list.add(model);
                popOver.hide();
            } else {
                popOver.show(this);
            }
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
}
