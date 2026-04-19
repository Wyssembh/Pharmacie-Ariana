/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Login_Register;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 *
 * @author nidha
 */
public class AlertMessage {
    private Alert alert;
    
    public void errorMessage(String message){
        alert=new Alert(AlertType.ERROR);
        alert.setTitle("Error message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void sucessMessage(String message){
          alert=new Alert(AlertType.INFORMATION);
        alert.setTitle("Information message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

   
}
