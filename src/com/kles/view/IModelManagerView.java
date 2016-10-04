/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kles.view;

import javafx.fxml.FXML;

/**
 *
 * @author JCHAUT
 */
public interface IModelManagerView {

    public void newInstance();

    @FXML
    public void handleNew();

    @FXML
    public void handleEdit();

    @FXML
    public void handleCopy();

    @FXML
    public void handleDelete();
}
