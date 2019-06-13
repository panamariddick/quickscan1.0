/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickscan1.pkg0;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Riddick
 */
public class AcercaDeController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Button btn_backMain;
    @FXML
    private Button btn_salir;
    Node node;
    Stage stage;
    Parent parent;
    Scene scene;
    @FXML
    private void salir(ActionEvent event) {
    Platform.exit();
    }
  
    @FXML
    private void goQuickScan(ActionEvent event){
          try {
            node = (Node) event.getSource();
            stage = (Stage) node.getScene().getWindow();

            parent = FXMLLoader.load(getClass().getResource("Inicio.fxml"));

            scene = new Scene(parent);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setTitle("QuickScan 1.0");
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(AcercaDeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
