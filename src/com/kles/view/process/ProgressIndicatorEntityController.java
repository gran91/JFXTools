package com.kles.view.process;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Service;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;


public class ProgressIndicatorEntityController {

    @FXML
    protected ProgressIndicator progress;

    @FXML
    protected Label title;

    @FXML
    protected Circle status;

    @FXML
    protected VBox mainPane;

    @FXML
    protected HBox titlePane;

    @FXML
    protected AnchorPane progressPane;

    protected Service service;
    protected boolean toggle;
    protected long lastTimerCall;
    protected AnimationTimer timer;
    protected PopOver popOver;
    protected double targetX;
    protected double targetY;
    protected EventHandler<MouseEvent> mouse;

    protected final BooleanProperty disablePane = new SimpleBooleanProperty(true);
    public static int ACTIVE = 0;
    public static int INACTIVE = 1;
    public static int ERROR = 2;

    @FXML
    private void initialize() {
        progressPane.getStylesheets().add("/org/kles/css/progressIndicatorEntity.css");
        changeStatus(INACTIVE);
        progressPane.disableProperty().bind(disablePane);
        mouse = (MouseEvent click) -> {
            if (popOver != null && !popOver.isDetached()) {
                popOver.hide();
            }
            if (click.getClickCount() == 2 && service != null) {
                try {
                    if (popOver != null && popOver.isShowing()) {
                        popOver.hide(Duration.ZERO);
                    }
                    targetX = click.getScreenX();
                    targetY = click.getScreenY();

                    popOver = createPopOver();
                    boolean autoPosition = false;
                    if (autoPosition) {
                        popOver.show(progressPane);
                    } else {
                        popOver.show(progressPane, targetX, targetY);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ProgressIndicatorEntityController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        progressPane.setOnMouseClicked(mouse);
        progress.setOnMouseClicked(mouse);
        status.setOnMouseClicked(mouse);
    }

    private PopOver createPopOver() throws IOException {
        popOver = new PopOver();
        popOver.setDetachable(false);
        popOver.setDetached(false);
        popOver.setArrowSize(10);
        TextArea text = new TextArea();
        text.textProperty().bind(service.messageProperty());
        StringBinding bindText = Bindings.createStringBinding(new Callable<String>() {

            @Override
            public String call() throws Exception {
                return service.messageProperty().getValue();
            }
        }, service.messageProperty());
        text.textProperty().bind(bindText);
        popOver.setContentNode(text);
        return popOver;
    }

    public void changeStatus(int stat) {
        if (stat == ACTIVE) {
            status.getStyleClass().clear();
            status.getStyleClass().add("circle_green");
            disablePane.set(false);
        } else if (stat == INACTIVE) {
            status.getStyleClass().clear();
            status.getStyleClass().add("circle_black");
            disablePane.set(true);
        } else if (stat == ERROR) {
            status.getStyleClass().clear();
            status.getStyleClass().add("circle_red");
            disablePane.set(false);
        }
    }

    public ProgressIndicator getProgress() {
        return progress;
    }

    public void setProgress(ProgressIndicator progress) {
        this.progress = progress;
    }

    public Label getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public Circle getStatus() {
        return status;
    }

    public void setStatus(Circle status) {
        this.status = status;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
        this.service.setOnRunning((Event event) -> {
            changeStatus(ACTIVE);
        });
        this.service.setOnFailed((Event event) -> {
            changeStatus(ERROR);
        });
        progress.progressProperty().bind(service.progressProperty());
    }

}
