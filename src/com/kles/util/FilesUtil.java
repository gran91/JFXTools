/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kles.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jchau
 */
public class FilesUtil {

    public static URL[] createURLList(String[] list) {
        ArrayList<URL> ar = new ArrayList<>();
        for (String s : list) {
            try {
                ar.add(new URL("file:/" + s));

            } catch (MalformedURLException ex) {
                Logger.getLogger(FilesUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return ar.toArray(new URL[ar.size()]);
    }
}
