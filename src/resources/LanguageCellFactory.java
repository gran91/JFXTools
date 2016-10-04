package resources;

/**
 *
 * @author Jeremy.CHAUT
 */
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import com.kles.model.Language;

public class LanguageCellFactory implements Callback<ListView<Language>, ListCell<Language>> {

    @Override
    public ListCell<Language> call(ListView<Language> listview) {
        return new LanguageCell();
    }
}
