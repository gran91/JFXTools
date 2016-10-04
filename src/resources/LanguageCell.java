package resources;

import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import com.kles.model.Language;

public class LanguageCell extends ListCell<Language> {

    @Override
    public void updateItem(Language item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setGraphic(null);
            setText(null);
        } else {
            ImageView view = new ImageView();
            view.setImage(item.getIcon());
            setGraphic(view);
            setText(item.getCode());
        }
    }
}
