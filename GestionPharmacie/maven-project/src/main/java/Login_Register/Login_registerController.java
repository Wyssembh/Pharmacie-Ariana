/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Login_Register;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author nidha
 */
public class Login_registerController implements Initializable {
    // Stocke l'id de l'utilisateur actuellement connecté
    public static int currentUserId = -1;
    public static String currentUsername = "";

      @FXML
    private TextField login_ShowPassword;

    @FXML
    private Button login_btn;

    @FXML
    private Button login_createAccount;

    @FXML
    private AnchorPane login_form;

    @FXML
    private PasswordField login_password;

    @FXML
    private CheckBox login_selectShowPassword;

    @FXML
    private TextField login_username;

    @FXML
    private Button signup_btn;

    @FXML
    private PasswordField signup_cPassword;

    @FXML
    private TextField signup_email;

    @FXML
    private AnchorPane signup_form;

    @FXML
    private Button signup_loginAccount;

    @FXML
    private PasswordField signup_password;

    @FXML
    private TextField signup_username;
    @FXML
     void seDeplacer(){
   
  
         signup_form.setVisible(true);
        login_form.setVisible(false);
    
    }
 
   @FXML
    void redirectSingIn() {
       signup_form.setVisible(false);
        login_form.setVisible(true);
    }
    @FXML
    private Connection connect;
    private PreparedStatement prepar;
    private ResultSet result;
    private Statement statement;
    
      private static Connection con;
   private static String user="root";
    private static String password="root";
    
     public  Connection seConnecter(){
        if(con==null){
            try {
                con=DriverManager.getConnection("jdbc:mysql://localhost:3306/pharmacie",user,password);
                System.out.println("Connexion établie");
            }
            catch(SQLException ex){
                System.out.println("Bd non trouvé ou problème d'identification "+ex.getMessage());
            }
        }
     return con;
    }
    public void login() {
        AlertMessage alert = new AlertMessage();
        String username = login_username.getText();
        String passwordInput = login_password.getText();
        if (username.isEmpty() || passwordInput.isEmpty()) {
            alert.errorMessage("All fields are required !");
        } else {
            // Cas spécial pour superadmin
            if (username.equals("superadmin") && passwordInput.equals("admin")) {
                currentUsername = username;
                alert.sucessMessage("Logged in as superadmin !");
                // Optionally, you can also log superadmin login in the login table if you want
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/accueil.fxml"));
                    Parent accueilParent = loader.load();
                    Scene accueilScene = new Scene(accueilParent);
                    Stage stage = (Stage) login_btn.getScene().getWindow();
                    stage.setScene(accueilScene);
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
            con = this.seConnecter();
            try {
                String selectData = "SELECT id, username, password FROM users WHERE username = ?";
                java.sql.PreparedStatement prepare = con.prepareStatement(selectData);
                prepare.setString(1, username);
                ResultSet result = prepare.executeQuery();
                if (result.next()) {
                    int userId = result.getInt("id");
                    String hashedPasswordDB = result.getString("password");
                    String hashedPasswordInput = hashPassword(passwordInput);
                    if (hashedPasswordDB.equals(hashedPasswordInput)) {
                        // Stocke l'id de l'utilisateur connecté
                        currentUserId = userId;
                        currentUsername = username;
                        System.out.println("Current username: " + currentUsername);
                        // Insertion dans la table login
                        try {
                            String insertLogin = "INSERT INTO login (date_login, id_user) VALUES (CURRENT_TIMESTAMP, ?)";
                            java.sql.PreparedStatement loginStmt = con.prepareStatement(insertLogin);
                            loginStmt.setInt(1, userId);
                            loginStmt.executeUpdate();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        alert.sucessMessage("Logged in sucessfully !");
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/TableView.fxml"));
                        Parent tableViewParent = loader.load();
                        Scene tableViewScene = new Scene(tableViewParent);
                        Stage stage = (Stage) login_btn.getScene().getWindow();
                        stage.setScene(tableViewScene);
                        stage.show();
                    } else {
                        alert.errorMessage("Incorrect username/password !");
                    }
                } else {
                    alert.errorMessage("Incorrect username/password !");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void register(){
        AlertMessage alert=new AlertMessage();
        if(signup_email.getText().isEmpty() || signup_username.getText().isEmpty() || signup_password.getText().isEmpty() || signup_cPassword.getText().isEmpty()){
            alert.errorMessage("All fields are required !");
        }
        else if( signup_password.getText()== signup_cPassword.getText()){
              alert.errorMessage("password dosn't match !");
        }
        else{
            //String checkuser="SELECT * from users where username='"+signup_username.getText()+"'";
             con=this.seConnecter();
             try{
                  String hashedPassword = hashPassword(signup_password.getText());

            String data="insert into users "+"(email,username,password)"+"values(?,?,?)";
                 java.sql.PreparedStatement prepare = con.prepareStatement(data);
                 prepare.setString(1, signup_email.getText());
                 prepare.setString(2, signup_username.getText());
                 prepare.setString(3,   hashedPassword);
                 prepare.executeUpdate();
                 alert.sucessMessage("Registred sucessfully !");
                 this.clearFields();
                 signup_form.setVisible(false);
                 login_form.setVisible(true);
             }
             catch(Exception e){
                 e.printStackTrace();
             }
           
        }
    }
     public String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());
        byte[] hashedPasswordBytes = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : hashedPasswordBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    public void clearFields(){
        signup_email.setText("");
        signup_username.setText("");
        signup_password.setText("");
        signup_cPassword.setText("");
        
        
    }
   public void showPassword(){
       if(login_selectShowPassword.isSelected()){
           login_ShowPassword.setText( login_password.getText());
           login_ShowPassword.setVisible(true);
           login_password.setVisible(false);
       }
       else{
            login_password.setText(login_ShowPassword.getText());
            login_ShowPassword.setVisible(false);
           login_password.setVisible(true);
       }
   }
   
  
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
