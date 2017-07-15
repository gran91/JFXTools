/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kles.view.util;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 *
 * @author Jeremy.CHAUT
 */
public class FilePathChooser extends HBox {

    @FXML
    private TextField pathField = new TextField();
    @FXML
    private Button buttonPath = new Button("...");
    private Stage dialogStage;
    private int type = 0;
    private String title = "";

    public FilePathChooser(Priority p) {
        this.setSpacing(5d);
        pathField.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(pathField, p);
        this.getChildren().add(pathField);
        this.getChildren().add(buttonPath);
        buttonPath.setOnAction(e -> chooseFile(e));
    }

    public FilePathChooser() {
        this(Priority.ALWAYS);
    }

    public void fixedSize() {
        HBox.setHgrow(pathField, Priority.SOMETIMES);
    }

    @FXML
    private void chooseFile(ActionEvent e) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle(title);
        if (new File(pathField.getText()).exists()) {
            chooser.setInitialDirectory(new File(pathField.getText()));
        }
        File selectedDirectory = chooser.showDialog(dialogStage);
        if (selectedDirectory != null) {
            pathField.setText(selectedDirectory.getPath());
        }

    }

    public TextField getPathField() {
        return pathField;
    }

    public void setPathField(TextField pathField) {
        this.pathField = pathField;
    }

    public Button getButtonPath() {
        return buttonPath;
    }

    public void setButtonPath(Button buttonPath) {
        this.buttonPath = buttonPath;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String path) {
        pathField.setText(path);
    }

    public String getText() {
        return pathField.getText();
    }

    public File getFile() {
        return new File(pathField.getText());
    }
}
