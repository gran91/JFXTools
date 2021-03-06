/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kles.view.util;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.util.Duration;
import com.kles.MainApp;
import com.kles.model.AbstractDataModel;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.util.ResourceBundle;
import resources.ResourceCSS;

/**
 *
 * @author Jeremy.CHAUT
 */
public class ComboboxModelAddDisplay extends ComboboxModelAdd {

    //Image imageDisplay = new Image(getClass().getResourceAsStream("/resources/images/info.png"));
    FontAwesomeIcon imageDisplay = FontAwesomeIcon.EDIT;
    
    @FXML
    private Button bDisplay;// = new Button("", new ImageView(imageDisplay));

    public ComboboxModelAddDisplay() {
        super();
        FontAwesomeIconView fInfo = new FontAwesomeIconView(imageDisplay);
        fInfo.setSize("15");
//        fInfo.setStyle(ResourceCSS.INFORMATION_STYLE);
        fInfo.setFill(ResourceCSS.informationFill());
        bDisplay = new Button("", fInfo);
        this.getChildren().add(bDisplay);
    }
    
    public ComboboxModelAddDisplay(MainApp main, ObservableList l, AbstractDataModel m) {
        super(main, l, m);
        FontAwesomeIconView fInfo = new FontAwesomeIconView(imageAdd);
        fInfo.setStyle(ResourceCSS.INFORMATION_STYLE);
        bDisplay = new Button("", fInfo);
        this.getChildren().add(bDisplay);
    }
    
    public void init(ResourceBundle rb) {
        super.init(rb);
        bDisplay.setOnAction((ActionEvent event) -> {
            displayModel();
        });
    }
    
    public void displayModel() {
        try {
            if (popOver != null && popOver.isShowing()) {
                popOver.hide(Duration.ZERO);
            }
            com.sun.glass.ui.Robot robot = com.sun.glass.ui.Application.GetApplication().createRobot();
            int x = robot.getMouseX();
            int y = robot.getMouseY();
            targetX = x;
            targetY = y;
            if (listModel.getSelectionModel().getSelectedItem() == null) {
                return;
            }
            popOver = createPopOver(listModel.getSelectionModel().getSelectedItem());
            boolean autoPosition = false;
            if (autoPosition) {
                popOver.show(this);
            } else {
                popOver.show(this, targetX, targetY);
            }
        } catch (IOException ex) {
            Logger.getLogger(ComboboxModelAddDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
