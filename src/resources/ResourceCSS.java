package resources;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.util.ArrayList;
import java.util.List;
import static javafx.collections.FXCollections.fill;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Jeremy.CHAUT
 */
public class ResourceCSS {

    public static final String IN_PROGRESS_STYLE = "-fx-fill: linear-gradient(#70B4E5 0%, #247CBC 70%, #2C85C1 85%)";
    public static final String SUCCESS_STYLE = "-fx-fill: linear-gradient(#00FF00 0%, #00A900 70%, #008600 85%)";
    public static final String WARNING_STYLE = "-fx-fill: linear-gradient(#FFFFAA 0%, #FFFF00 70%, #AAAA00 85%)";
    public static final String FAIL_STYLE = "-fx-fill: linear-gradient(#990000 0%, #DD0000 70%, #FF0000 85%)";
    public static final String INFORMATION_STYLE = "-fx-fill: linear-gradient(#70B4E5 0%, #247CBC 70%, #2C85C1 85%)";
    public static final String WORKING_STYLE = "-fx-fill: linear-gradient(#CCCCCC 0%, #88888888 70%, #555555 85%)";

    public static final String BACKGROUND_TRANSPARENT = "-fx-background-color: rgba(0, 0, 0, 0.1)";
    public static final String BACKGROUND_RADIUS_10 = "-fx-background-radius: 10";

    public static Paint inprogressFill() {
        List<Stop> listStop = new ArrayList<>();
        listStop.add(new Stop(0, Color.web("#70B4E5")));
        listStop.add(new Stop(0.7, Color.web("#247CBC")));
        listStop.add(new Stop(0.85, Color.web("#2C85C1")));
        LinearGradient paint = new LinearGradient(0, 0, 1, 1, true, CycleMethod.REFLECT, listStop);
        return paint;
    }

    public static Paint sucessFill() {
        List<Stop> listStop = new ArrayList<>();
        listStop.add(new Stop(0, Color.web("#00FF00")));
        listStop.add(new Stop(0.7, Color.web("#00A900")));
        listStop.add(new Stop(0.85, Color.web("#008600")));
        LinearGradient paint = new LinearGradient(0, 0, 1, 1, true, CycleMethod.REFLECT, listStop);
        return paint;
    }

    public static Paint warningFill() {
        List<Stop> listStop = new ArrayList<>();
        listStop.add(new Stop(0, Color.web("#FFFFAA")));
        listStop.add(new Stop(0.7, Color.web("#FFFF00")));
        listStop.add(new Stop(0.85, Color.web("#AAAA00")));
        LinearGradient paint = new LinearGradient(0, 0, 1, 1, true, CycleMethod.REFLECT, listStop);
        return paint;
    }

    public static Paint failedFill() {
        List<Stop> listStop = new ArrayList<>();
        listStop.add(new Stop(0, Color.web("#990000")));
        listStop.add(new Stop(0.7, Color.web("#DD0000")));
        listStop.add(new Stop(0.85, Color.web("#FF0000")));
        LinearGradient paint = new LinearGradient(0, 0, 1, 1, true, CycleMethod.REFLECT, listStop);
        return paint;
    }
    
     public static Paint informationFill() {
        List<Stop> listStop = new ArrayList<>();
        listStop.add(new Stop(0, Color.web("#70B4E5")));
        listStop.add(new Stop(0.7, Color.web("#247CBC")));
        listStop.add(new Stop(0.85, Color.web("#2C85C1")));
        LinearGradient paint = new LinearGradient(0, 0, 1, 1, true, CycleMethod.REFLECT, listStop);
        return paint;
    }
}
