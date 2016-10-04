package resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import com.kles.MainApp;

/**
 *
 * @author Jérémy Chaut
 */
public class LanguageUtil {

    public static ArrayList<String> traduce(ResourceBundle resourceMessage, List<String> data) {
        ArrayList<String> a = new ArrayList<>();
        if (resourceMessage == null) {
            return new ArrayList(data);
        }
        data.stream().forEach((data1) -> {
            a.add(resourceMessage.getString(data1));
        });
        return a;
    }

    public static ArrayList<String> traduce(ResourceBundle resourceMessage, String[] data) {
        return traduce(resourceMessage, Arrays.asList(data));
    }

    public static ArrayList<String> traduce(List<String> data) {
        return traduce(MainApp.resourceMessage, data);
    }

    public static ArrayList<String> traduce(String[] data) {
        return traduce(MainApp.resourceMessage, data);
    }
}
