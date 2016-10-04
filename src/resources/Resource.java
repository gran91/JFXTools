/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.image.Image;

/**
 *
 * @author JCHAUT
 */
public class Resource {

    public static final String LANGAGEPATH = "resources/language";
    public static final String TITLE = "PMS070Tools";
    public static final String VERSION = "1.0.0";
    public static final Image LOGO_ICON_128 = new Image(Resource.class.getResourceAsStream("/resources/images/3KLES_128.png"));
    public static final Image LOGO_ICON_64 = new Image(Resource.class.getResourceAsStream("/resources/images/3KLES_64.png"));
    public static final Image LOGO_ICON_32 = new Image(Resource.class.getResourceAsStream("/resources/images/3KLES_32.png"));
    public static final Image LOGO_ICON_16 = new Image(Resource.class.getResourceAsStream("/resources/images/3KLES_16.png"));

    /*Format Date*/
    public static final SimpleDateFormat sdfYMD8 = new SimpleDateFormat("YYYYMMdd");
    public static final SimpleDateFormat sdfYMD8_HHmmss = new SimpleDateFormat("YYYYMMdd_HHmmss");

    public static String parseContent(String content, String regex, int group) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        if (m.find()) {
            return m.group(group);
        }
        return null;
    }

    public static String[] getListLanguage(String[] ids, Locale local) {
        ResourceBundle resourceMessage = ResourceBundle.getBundle("resources/language", local);
        return getListLanguage(ids, resourceMessage);
    }

    public static String[] getListLanguage(String[] ids, ResourceBundle resourceMessage) {
        String[] lng = new String[ids.length];
        for (int i = 0; i < ids.length; i++) {
            lng[i] = resourceMessage.getString(ids[i]);
        }
        return lng;
    }
}
