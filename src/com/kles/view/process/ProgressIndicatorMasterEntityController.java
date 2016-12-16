package com.kles.view.process;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.scene.control.TextArea;
import org.controlsfx.control.PopOver;

public class ProgressIndicatorMasterEntityController extends ProgressIndicatorEntityController {

    protected LinkedHashMap<String, Service> listService = new LinkedHashMap();
    //DoubleProperty avg = new SimpleDoubleProperty();
    double avg = 0;

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

    public void setServices(LinkedHashMap<String, Service> servList) {
        listService = servList;
        bindingService();
    }

    public void addService(String name, Service serv) {
        listService.put(name, serv);
        bindingService();
    }

    private void listenerRunningService() {
        int cpt = 0;
        listService.entrySet().stream().filter((s) -> (s.getValue().isRunning())).forEach((Map.Entry<String, Service> s) -> {
            s.getValue().progressProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
                calculateRunningService();
            });
        });
    }

    private void calculateRunningService() {
        int cpt = 0;
        double avg = 0;
        for (Map.Entry<String, Service> s : listService.entrySet()) {
                avg += s.getValue().progressProperty().getValue();
                changeStatus(ACTIVE);
                cpt++;
        }
        progress.setProgress(avg / listService.size());
    }

    private void bindingService() {
        listService.entrySet().stream().forEach((serv) -> {
            serv.getValue().stateProperty().addListener((ObservableValue observable, Object oldValue, Object newValue) -> {
                listenerRunningService();
            });
        });
    }
}
