/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kles.util;

import javafx.collections.ObservableList;

/**
 *
 * @author jeremy.chaut
 */
public class ObservableListUtil {

    public static String[] toArrayString(ObservableList list) {
        String[] listString = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            listString[i] = list.get(i).toString();
        }
        return listString;
    }
}
