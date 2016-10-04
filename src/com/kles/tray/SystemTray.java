package com.kles.tray;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

import javafx.application.Platform;
import javafx.stage.WindowEvent;
import com.kles.MainApp;

public final class SystemTray {

    MainApp main;
    private TrayIcon trayIcon;

    public SystemTray(MainApp m) {
        main = m;
        Platform.setImplicitExit(false);
        if (java.awt.SystemTray.isSupported()) {
            java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();
            java.awt.Image image = Toolkit.getDefaultToolkit().getImage(SystemTray.class.getResource("/resources/images/3KLES_16.png"));
            main.getPrimaryStage().setOnCloseRequest((WindowEvent t) -> {
                Platform.runLater(() -> {
                    if (java.awt.SystemTray.isSupported()) {
                        main.getPrimaryStage().hide();
                        showProgramIsMinimizedMsg();
                    } else {
                        System.exit(0);
                    }
                });
            });
            final ActionListener closeListener = (java.awt.event.ActionEvent e) -> {
                System.exit(0);
            };

            final ActionListener showListener = (java.awt.event.ActionEvent e) -> {
                Platform.runLater(() -> {
                    main.getPrimaryStage().show();
                });
            };

            PopupMenu popup = new PopupMenu();

            MenuItem showItem = new MenuItem(MainApp.resourceMessage.getString("main.open"));
            showItem.addActionListener(showListener);
            popup.add(showItem);

            MenuItem closeItem = new MenuItem(MainApp.resourceMessage.getString("main.quit"));
            closeItem.addActionListener(closeListener);
            popup.add(closeItem);

            trayIcon = new TrayIcon(image, resources.Resource.TITLE, popup);
            trayIcon.addActionListener(showListener);
            trayIcon.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (e.getClickCount() == 2 && !e.isConsumed()) {
                        e.consume();
                        Platform.runLater(() -> {
                            main.getPrimaryStage().show();
                        });
                    }
                }
            });

            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println(e);
            }

        }
    }

    public void showProgramIsMinimizedMsg() {
        trayIcon.displayMessage(MainApp.resourceMessage.getString("main.appisrunningtitle"), MainApp.resourceMessage.getString("main.appisrunning"), TrayIcon.MessageType.INFO);
    }

    public static SystemTray addSystemTray(MainApp m) {
        return new SystemTray(m);
    }
}
