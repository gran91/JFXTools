package com.kles.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author JCHAUT
 */
public abstract class AbstractDataModel implements IDataModelManager, Cloneable, Serializable {

    public AbstractDataModel() {
    }
    private transient AbstractDataModel parent;
    public transient String datamodelname;

    public AbstractDataModel(String datamodel) {
        datamodelname = datamodel;
    }

    @Override
    public ArrayList<?> extractData() {
        ArrayList<?> a = new ArrayList();
        return a;
    }

    @Override
    public void populateData(ArrayList<?> data) {
        if (data != null) {

        }
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    public AbstractDataModel clone() throws CloneNotSupportedException {
        AbstractDataModel model = null;
        try {
            model = (AbstractDataModel) super.clone();
        } catch (CloneNotSupportedException cnse) {
            cnse.printStackTrace(System.err);
        }
        return model;
    }

    public String datamodelName() {
        return datamodelname;
    }

    public void datamodelName(String datamodelname) {
        this.datamodelname = datamodelname;
    }

    public AbstractDataModel getParent() {
        return parent;
    }

    public void setParent(AbstractDataModel parent) {
        this.parent = parent;
    }
}
