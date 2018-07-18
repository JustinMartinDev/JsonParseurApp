package com.st.jsonparseurapp;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;
import tools.SwitchView;


public class MainApp extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        SwitchView switchView = SwitchView.newPage("home", Constant.HOME_TITLE);
        switchView.showScene();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
