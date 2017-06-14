/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kles.util;

import com.kles.MainApp;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.prefs.Preferences;
import javafx.stage.FileChooser;
import javafx.stage.Window;

/**
 *
 * @author Jeremy.CHAUT
 */
public class ChooserFileUtil {

    public static File openFileChooserWithCopy(Window owner, FileChooser.ExtensionFilter filter) {
        FileChooser f = new FileChooser();
        f.setSelectedExtensionFilter(filter);
        File file = f.showOpenDialog(owner);
        if (file != null) {
            Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
            String filePath = prefs.get("filePath", null);
            File fileTarg;
            if (filePath != null) {
                fileTarg = new File(filePath + System.getProperty("file.separator") + System.currentTimeMillis() + "_" + file.getName());
                try {
                    Files.copy(Paths.get(file.toURI()), Paths.get(fileTarg.toURI()), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    fileTarg = file;
                }
            } else {
                fileTarg = file;
            }
            return fileTarg;
        }
        return null;
    }
}
