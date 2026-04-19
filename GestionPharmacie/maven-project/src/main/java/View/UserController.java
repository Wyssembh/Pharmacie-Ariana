package View;

import javafx.beans.property.SimpleStringProperty;
import Connexion.LaConnexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserController {

    public static class UserLoginRow {
        private final SimpleStringProperty username;
        private final SimpleStringProperty email;
        private final SimpleStringProperty dateLogin;
        private final SimpleStringProperty dateLogout;

        public UserLoginRow(String username, String email, String dateLogin, String dateLogout) {
            this.username = new SimpleStringProperty(username);
            this.email = new SimpleStringProperty(email);
            this.dateLogin = new SimpleStringProperty(dateLogin);
            this.dateLogout = new SimpleStringProperty(dateLogout);
        }
        public String getUsername() { return username.get(); }
        public String getEmail() { return email.get(); }
        public String getDateLogin() { return dateLogin.get(); }
        public String getDateLogout() { return dateLogout.get(); }
    }

    @FXML
    private TableView<UserLoginRow> userLoginTable;
    @FXML
    private TableColumn<UserLoginRow, String> usernameCol;
    @FXML
    private TableColumn<UserLoginRow, String> emailCol;
    @FXML
    private TableColumn<UserLoginRow, String> dateLoginCol;
    @FXML
    private TableColumn<UserLoginRow, String> dateLogoutCol;

    @FXML
    private Button btnAccueil;

    @FXML
    public void initialize() {
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        dateLoginCol.setCellValueFactory(new PropertyValueFactory<>("dateLogin"));
        dateLogoutCol.setCellValueFactory(new PropertyValueFactory<>("dateLogout"));
        userLoginTable.setItems(getUserLoginRows());
    }

    private ObservableList<UserLoginRow> getUserLoginRows() {
        ObservableList<UserLoginRow> rows = FXCollections.observableArrayList();
        try {
            Connection con = LaConnexion.seConnecter();
            String sql = "SELECT u.username, u.email, l.date_login, l.date_logout " +
                         "FROM users u JOIN login l ON u.id = l.id_user ORDER BY l.date_login DESC";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String username = rs.getString("username");
                String email = rs.getString("email");
                String dateLogin = rs.getString("date_login");
                String dateLogout = rs.getString("date_logout");
                if (dateLogout == null) {
                    dateLogout = "utilisateur en train de travailler";
                }
                rows.add(new UserLoginRow(username, email, dateLogin, dateLogout));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows;
    }



   
    @FXML
    private void goAccueil(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/accueil.fxml"));
            Parent accueilParent = loader.load();
            Scene accueilScene = new Scene(accueilParent);
            Stage stage = (Stage) btnAccueil.getScene().getWindow();
            stage.setScene(accueilScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
