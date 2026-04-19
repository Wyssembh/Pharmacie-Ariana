/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Login_Register;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author nidha
 */
public class Launch extends Application {

    @Override
    public void start(Stage stage) throws Exception {
       Parent root=FXMLLoader.load(getClass().getResource("Login_register.fxml"));
       stage.setScene(new Scene(root));
       stage.setTitle("Login");
       stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
 
}
