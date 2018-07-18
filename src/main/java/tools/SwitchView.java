/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;
import com.st.jsonparseurapp.Constant;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.scene.layout.BorderPane;
/**
 *
 * @author martinj
 *
 * This class manage the the switch of view
 */
public class SwitchView {

    private BorderPane borderPane;
    
    private static Stage newStage;
    
    private boolean showAndWait = false;
    private boolean popup = false;

    private final static String STYLECSS = "styles.css";

    /**
     * 
     * Create a new window with title and view 
     * @param view name of the view to show (exemple : "home" for "home.fxml")
     * @param title title of the new window
     * @return the SwitchView object generated
     */
    public static SwitchView newPage(String view, String title){
        newStage = new Stage();
        return new SwitchView(view, title);
     }
     
     /**
     * 
     * Create a new window with title, view and borderPaner
     * @param view name of the view to show (exemple : "home" for "home.fxml")
     * @param title title of the new window
     * @param bpane boderPane of the actual window
     * @return the SwitchView object generated
     */
    public static SwitchView newPage(String view, String title, BorderPane bpane){
        newStage = new Stage();
        return new SwitchView(view, title, bpane);
    }
 
    /**
     * Main constructor of SwitchView, will load the fxml, css and generate the Stage 
     * @param view name of the view to show (exemple : "home" for "home.fxml")
     * @param title new title of the window
     * @param bpane boderPane of the actual window
     */
    public SwitchView(String view, String title, BorderPane bpane) {
        borderPane = bpane;
        try {
            FXMLLoader load = new FXMLLoader(getClass().getResource(Constant.LAYOUT_PATH + view + ".fxml"));
            Scene scene = new Scene((Parent) load.load());
            scene.getStylesheets().add(Constant.STYLE_PATH + STYLECSS);
            
            newStage.setScene(scene);
            newStage.setTitle(title + " - " + Constant.TITLE_APP);

            newStage.setMinHeight(Constant.MIN_HEIGHT);
            newStage.setMinWidth(Constant.MIN_WIDTH);
            newStage.setHeight(Constant.PREF_HEIGHT);
            newStage.setWidth(Constant.PREF_WIDTH);

            newStage.setMaximized(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Main constructor of SwitchView, will load the fxml, css and generate the Stage 
     * @param view name of the view to show (exemple : "home" for "home.fxml")
     * @param title new title of the window
     * @param bpane boderPane of the actual window
     */
    public SwitchView(String view, String title) {
        this(view, title, null);
    }
    
    /**
     * constructor of SwitchView, will load the fxml, css and generate a PopUp 
     * @param view name of the view to show (exemple : "home" for "home.fxml")
     * @param title title of the PopUp
     * @param showAndWait true if the popUp should block the other view unit it's closed
     */
    public SwitchView(String view, String title, boolean showAndWait) {
        this(view, title, null);
        this.showAndWait = showAndWait;
    }

    public void setPopUp(boolean popup) {
        this.popup = popup;
    }

     /**
     * show the Scene loaded on the SwitchView
     */
    public void showScene() {
        if (!popup) {
            newStage.setMaximized(false);
        }
        if (showAndWait)
            newStage.showAndWait();
        else {
            newStage.show();
        }
        if (borderPane != null) {
                        /*
              * DEBUG
              *
              * Stage stage = (Stage) borderPane.getScene().getWindow();
             * stage.close();
              */
        }
    }
}
