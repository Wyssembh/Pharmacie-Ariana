package View;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class AccueilController {
    @FXML
    private Button buttonMed;
    @FXML
    private Button buttonPat;
    @FXML
    private Button buttonDash;

    @FXML
    private void handleButtonMed(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Medicament.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) buttonMed.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    @FXML
    private Button buttonLogout;

    @FXML
    private void handleButtonLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login_Register/Login_register.fxml"));
            Parent loginParent = loader.load();
            Scene loginScene = new Scene(loginParent);
            Stage stage = (Stage) buttonLogout.getScene().getWindow();
            stage.setScene(loginScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleButtonPat(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/TableView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) buttonPat.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private Button buttonUsers;

    @FXML
    private void handleButtonUsers() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/users.fxml"));
            Parent loginParent = loader.load();
            Scene loginScene = new Scene(loginParent);
            Stage stage = (Stage) buttonUsers.getScene().getWindow();
            stage.setScene(loginScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleButtonDash(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) buttonDash.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}