/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kles.view.util;

import javafx.concurrent.Service;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;

/**
 *
 * @author GRAN_LAPTOP
 */
public class PanelIndicator {

    public VBox panel;
    public Label label;
    public ProgressIndicator progress;

    public static final PanelIndicator create() {
        return new PanelIndicator();
    }

    public final PanelIndicator build() {
        panel = new VBox();
        panel.setAlignment(Pos.CENTER);
        panel.setSpacing(10);
//        panel.setPrefSize(347, 595);
        progress = new ProgressIndicator(ProgressIndicator.INDETERMINATE_PROGRESS);
        label = new Label();
//        label.setPrefSize(345, 40);
        label.setAlignment(Pos.CENTER);
        panel.getChildren().add(progress);
        panel.getChildren().add(label);
        return this;
    }

    public final PanelIndicator setService(Service service) {
        label.textProperty().bind(service.messageProperty());
        return this;
    }

    public VBox getPanel() {
        return panel;
    }

    public Label getLabel() {
        return label;
    }

    public ProgressIndicator getProgress() {
        return progress;
    }
}
