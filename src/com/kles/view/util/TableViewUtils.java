package com.kles.view.util;

import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import com.sun.javafx.scene.control.skin.TableViewSkin;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class TableViewUtils {

    public static void addCustomTableMenu(TableView tableView) {
        tableView.setTableMenuButtonVisible(true);
        setCustomContextMenu(tableView);
    }

    private static void setCustomContextMenu(TableView table) {
        if (table.getSkin() == null) {
            table.skinProperty().set(new TableViewSkin(table));
        }
        TableViewSkin<?> tableSkin = (TableViewSkin<?>) table.getSkin();
        ObservableList<Node> children = tableSkin.getChildren();
        for (Node node : children) {
            if (node instanceof TableHeaderRow) {
                TableHeaderRow tableHeaderRow = (TableHeaderRow) node;
                double defaultHeight = tableHeaderRow.getHeight();
                tableHeaderRow.setPrefHeight(defaultHeight);
                for (Node child : tableHeaderRow.getChildren()) {
                    if (child.getStyleClass().contains("show-hide-columns-button")) {
                        ContextMenu columnPopupMenu = createContextMenu(table);
                        child.setOnMousePressed(me -> {
                            columnPopupMenu.show(child, Side.BOTTOM, 0, 0);
                            me.consume();
                        });
                    }
                }

            }
        }
    }

    /**
     * Create a menu with custom items. The important thing is that the menu
     * remains open while you click on the menu items.
     *
     * @param cm
     * @param table
     */
    private static ContextMenu createContextMenu(TableView table) {
        ContextMenu cm = new ContextMenu();
        CustomMenuItem cmi;

        Label selectAll = new Label("Select all");
        selectAll.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            table.getColumns().stream().forEach((obj) -> {
                ((TableColumn<?, ?>) obj).setVisible(true);
            });
        });
        cmi = new CustomMenuItem(selectAll);
        cmi.setHideOnClick(false);
        cm.getItems().add(cmi);

        Label deselectAll = new Label("Deselect all");
        deselectAll.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            table.getColumns().stream().forEach((obj) -> {
                ((TableColumn<?, ?>) obj).setVisible(false);
            });
        });

        cmi = new CustomMenuItem(deselectAll);
        cmi.setHideOnClick(false);
        cm.getItems().add(cmi);
        cm.getItems().add(new SeparatorMenuItem());

        for (Object obj : table.getColumns()) {
            TableColumn<?, ?> tableColumn = (TableColumn<?, ?>) obj;
            CheckBox cb = new CheckBox(tableColumn.getText());
            cb.selectedProperty().bindBidirectional(tableColumn.visibleProperty());
            cmi = new CustomMenuItem(cb);
            cmi.setHideOnClick(false);
            cm.getItems().add(cmi);
        }
        return cm;
    }

    public static void addHeaderFilter(TableView<?> table) {
        table.getColumns().stream().forEach((tableColumn) -> {
            Label label = new Label(tableColumn.getText());
            HBox lableBox = new HBox(label);
            lableBox.getStyleClass().add("labelBoxTxt");
            lableBox.setAlignment(Pos.CENTER);

            StackPane HL = new StackPane();
            HL.getStyleClass().add("greyBorder");

            TextField textField = new TextField();
            HBox.setHgrow(textField, Priority.ALWAYS);
            HBox.setMargin(textField, new Insets(3));
            HBox textInputBox = new HBox(textField);

            VBox vBox = new VBox(lableBox, HL, textInputBox);
            tableColumn.setText("");
            tableColumn.setGraphic(vBox);

            List<String> columnData = new ArrayList<>();
            for (Object item : table.getItems()) {
                columnData.add(tableColumn.getCellObservableValue(0).getValue().toString());
            }
        }
        );
    }
}
