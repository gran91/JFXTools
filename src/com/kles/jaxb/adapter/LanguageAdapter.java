package com.kles.jaxb.adapter;

import com.kles.MainApp;
import com.kles.model.Language;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Adapter (for JAXB) to convert between the LocalDate and the ISO 8601 String
 * representation of the date such as '2012-12-03'.
 *
 * @author Jérémy Chaut
 */
public class LanguageAdapter extends XmlAdapter<String, Language> {

    @Override
    public Language unmarshal(String v) throws Exception {
        for (Language l : MainApp.listLanguage) {
            if (l.getCode().equals(v)) {
                return l;
            }
        }
        return null;
    }

    @Override
    public String marshal(Language v) throws Exception {
        return v.toString();
    }
}
