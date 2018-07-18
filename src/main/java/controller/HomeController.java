/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.google.gson.JsonElement;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;
import tools.ParseurJson;

/**
 * FXML Controller class
 *
 * @author martinj
 */
public class HomeController implements Initializable {

    @FXML private BorderPane bPane;
    
    @FXML private JFXTextField sourcePath;
    @FXML private JFXTextField destinationPath;

    private File source;
    private File destination;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML public void selectSource(){
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selectionner le ficher csv");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("CSV", "*.csv"));
        Window win = bPane.getScene().getWindow();
        File file = fileChooser.showOpenDialog(win);
        if(file != null){
            source = file;
            sourcePath.setText(source.getAbsolutePath());
        }
    }

    @FXML public void selectDestination(){
      final DirectoryChooser directoryChooser = new DirectoryChooser();
      directoryChooser.setTitle("Selectionner le ficher csv");
      
      Window win = bPane.getScene().getWindow();
      File file = directoryChooser.showDialog(win);
      if(file != null){
          destination = new File(file.getAbsolutePath()+"\\ihmJson.json");
          destinationPath.setText(destination.getAbsolutePath());
      }


    }

    @FXML public void convert() throws IOException{
        ParseurJson parseur = new ParseurJson();  
        parseur.parse(source.getAbsolutePath());
       
        JsonElement myIHM = parseur.extractIHM();
           
        Path path = Paths.get(destination.toURI());
        byte[] strToBytes = myIHM.toString().getBytes();
        Files.write(path, strToBytes);
    }
}
