package com.kles.fx.custom;

import com.kles.MainApp;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import com.kles.view.util.ProgressDialogController;
import java.util.Optional;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.paint.Color;
import javafx.stage.Window;
import resources.Resource;

/**
 *
 * @author JCHAUT
 */
public class FxUtil {

    public enum AutoCompleteMode {
        STARTS_WITH, CONTAINING,;
    }

    public static Stage showInDialog(Node node, String title) {
        return showInDialog(null, node, title);
    }

    public static Stage showInDialog(Window owner, Node node, String title) {
        Stage dialogStage = new Stage();
        dialogStage.setTitle(title);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        if (owner != null) {
            dialogStage.initOwner(owner);
        }
        dialogStage.getIcons().add(Resource.LOGO_ICON_32);
        StackPane stack = new StackPane(node);
        Scene scene = new Scene(stack, Color.TRANSPARENT);
        scene.getStylesheets().add(MainApp.class.getResource("application.css").toExternalForm());
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
        return dialogStage;

    }

    public static <T> void autoCompleteComboBox(ComboBox<T> comboBox, AutoCompleteMode mode) {
        ObservableList<T> data = comboBox.getItems();

        comboBox.setEditable(true);
        comboBox.getEditor().focusedProperty().addListener(observable -> {
            if (comboBox.getSelectionModel().getSelectedIndex() < 0) {
                comboBox.getEditor().setText(null);
            }
        });
        comboBox.addEventHandler(KeyEvent.KEY_PRESSED, t -> comboBox.hide());
        comboBox.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {

            private boolean moveCaretToPos = false;
            private int caretPos;

            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.UP) {
                    caretPos = -1;
                    moveCaret(comboBox.getEditor().getText().length());
                    return;
                } else if (event.getCode() == KeyCode.DOWN) {
                    if (!comboBox.isShowing()) {
                        comboBox.show();
                    }
                    caretPos = -1;
                    moveCaret(comboBox.getEditor().getText().length());
                    return;
                } else if (event.getCode() == KeyCode.BACK_SPACE) {
                    moveCaretToPos = true;
                    caretPos = comboBox.getEditor().getCaretPosition();
                } else if (event.getCode() == KeyCode.DELETE) {
                    moveCaretToPos = true;
                    caretPos = comboBox.getEditor().getCaretPosition();
                }

                if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT
                        || event.isControlDown() || event.getCode() == KeyCode.HOME
                        || event.getCode() == KeyCode.END || event.getCode() == KeyCode.TAB) {
                    return;
                }

                ObservableList<T> list = FXCollections.observableArrayList();
                for (T aData : data) {
                    if (mode.equals(AutoCompleteMode.STARTS_WITH) && aData.toString().toLowerCase().startsWith(comboBox.getEditor().getText().toLowerCase())) {
                        list.add(aData);
                    } else if (mode.equals(AutoCompleteMode.CONTAINING) && aData.toString().toLowerCase().contains(comboBox.getEditor().getText().toLowerCase())) {
                        list.add(aData);
                    }
                }
                String t = comboBox.getEditor().getText();

                comboBox.setItems(list);
                comboBox.getEditor().setText(t);
                if (!moveCaretToPos) {
                    caretPos = -1;
                }
                moveCaret(t.length());
                if (!list.isEmpty()) {
                    comboBox.show();
                }
            }

            private void moveCaret(int textLength) {
                if (caretPos == -1) {
                    comboBox.getEditor().positionCaret(textLength);
                } else {
                    comboBox.getEditor().positionCaret(caretPos);
                }
                moveCaretToPos = false;
            }
        });
    }

    public static <T> T getComboBoxValue(ComboBox<T> comboBox) {
        if (comboBox.getSelectionModel().getSelectedIndex() < 0) {
            return null;
        } else {
            return comboBox.getItems().get(comboBox.getSelectionModel().getSelectedIndex());
        }
    }

    public static <S> void addAutoScroll(final ListView<S> view) {
        if (view == null) {
            throw new NullPointerException();
        }

        view.getItems().addListener((ListChangeListener<S>) (c -> {
            c.next();
            final int size = view.getItems().size();
            if (size > 0) {
                view.scrollTo(size - 1);
            }
        }));
    }

    public static <S> void addAutoScroll(final TableView<S> view) {
        if (view == null) {
            throw new NullPointerException();
        }

        view.getItems().addListener((ListChangeListener<S>) (c -> {
            c.next();
            final int size = view.getItems().size();
            if (size > 0) {
                view.scrollTo(size - 1);
            }
        }));
    }

    public static Optional<ButtonType> showAlert(Alert.AlertType type, String title, String header, String message) {
        return showAlert(null, type, title, header, message);
    }

    public static Optional<ButtonType> showAlert(Alert alert, Alert.AlertType type, String title, String header, String message) {
        if (alert == null) {
            alert = new Alert(type);
        } else {
            alert.setAlertType(type);
        }
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);

        return alert.showAndWait();
    }

    public static Optional<ButtonType> showAlert(Alert.AlertType type, String title, String header, String message, Exception ex) {
        return showAlert(null, type, title, header, message, ex);
    }

    public static Optional<ButtonType> showAlert(Alert alert, Alert.AlertType type, String title, String header, String message, Exception ex) {
        if (alert == null) {
            alert = new Alert(type);
        } else {
            alert.setAlertType(type);
        }
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();
        ResourceBundle resourceMessage = ResourceBundle.getBundle("resources/language", Locale.getDefault());
        Label label = new Label(resourceMessage.getString("exception.message"));

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);
        return alert.showAndWait();
    }

    public static ProgressDialogController showProgressDialog(Stage primaryStage) {
        return showProgressDialog(primaryStage, ResourceBundle.getBundle("resources.language", Locale.getDefault()));
    }

    public static ProgressDialogController showProgressDialog(Stage primaryStage, ResourceBundle res) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(ResourceBundle.getBundle("resources.language", Locale.getDefault()));
            loader.setLocation(FxUtil.class.getResource("/com/kles/view/util/ProgressDialog.fxml"));
            StackPane page = (StackPane) loader.load();

            Stage dialogStage = new Stage(StageStyle.UNDECORATED);
            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.getIcons().add(Resource.LOGO_ICON_32);
            Scene scene = new Scene(page, Color.TRANSPARENT);
            dialogStage.setScene(scene);

            ProgressDialogController controller = loader.getController();
            controller.setStage(dialogStage);
            return controller;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
