package insidefx.undecorator;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author JCHAUT
 */
public class UndecoratorScene extends Scene {

    static public final String DEFAULT_STYLESHEET = "skin/undecorator.css";
    static public final String DEFAULT_STYLESHEET_UTILITY = "skin/undecoratorUtilityStage.css";
    static public final String DEFAULT_STAGEDECORATION = "stagedecoration.fxml";
    static public final String DEFAULT_STAGEDECORATION_UTILITY = "stageUtilityDecoration.fxml";
    Undecorator undecorator;

    /**
     * Basic constructor with built-in behavior
     *
     * @param stage The main stage
     * @param root your UI to be displayed in the Stage
     */
    public UndecoratorScene(Stage stage, Region root) {
        this(stage, StageStyle.TRANSPARENT, root, DEFAULT_STAGEDECORATION);
    }

    /**
     * UndecoratorScene constructor
     *
     * @param stage The main stage
     * @param stageStyle could be StageStyle.UTILITY or StageStyle.TRANSPARENT
     * @param root your UI to be displayed in the Stage
     * @param stageDecorationFxml Your own Stage decoration or null to use the
     * built-in one
     */
    public UndecoratorScene(Stage stage, StageStyle stageStyle, Region root, String stageDecorationFxml) {

        super(root);
        
        /*
         * Fxml
         */
        if (stageDecorationFxml == null) {
            if (stageStyle == StageStyle.UTILITY) {
                stageDecorationFxml = DEFAULT_STAGEDECORATION_UTILITY;
            } else {
                stageDecorationFxml = DEFAULT_STAGEDECORATION;
            }
        }
        undecorator = new Undecorator(stage, root, stageDecorationFxml, stageStyle);
        super.setRoot(undecorator);
        
        // Customize it by CSS if needed:
        if (stageStyle == StageStyle.UTILITY) {
            undecorator.getStylesheets().add(DEFAULT_STYLESHEET_UTILITY);
        } else {
            undecorator.getStylesheets().add(DEFAULT_STYLESHEET);
        }

        // Transparent scene and stage
        stage.initStyle(StageStyle.TRANSPARENT);
        super.setFill(Color.TRANSPARENT);
        
        // Default Accelerators
        undecorator.installAccelerators(this);
        
    }
    
    public void removeDefaultStylesheet() {
        undecorator.getStylesheets().remove(DEFAULT_STYLESHEET);
        undecorator.getStylesheets().remove(DEFAULT_STYLESHEET_UTILITY);
    }

    public void addStylesheet(String css) {
        undecorator.getStylesheets().add(css);
    }

    public void setAsStageDraggable(Stage stage, Node node) {
        undecorator.setAsStageDraggable(stage, node);
    }

    public void setBackgroundStyle(String style) {
        undecorator.getShadowNode().setStyle(style);
    }
    public void setBackgroundOpacity(double opacity) {
        undecorator.getShadowNode().setOpacity(opacity);
    }
    public void setBackgroundPaint(Paint paint) {
        undecorator.removeDefaultBackgroundStyleClass();
        undecorator.getShadowNode().setFill(paint);
    }

    public Undecorator getUndecorator() {
        return undecorator;
    }

    public void setFadeInTransition() {
        undecorator.setFadeInTransition();
    }
    public void setFadeOutTransition() {
        undecorator.setFadeOutTransition();
    }
}
