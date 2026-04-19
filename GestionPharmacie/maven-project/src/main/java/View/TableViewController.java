/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Connexion.LaConnexion;
import Login_Register.AlertMessage;
import Models.Patient;
import java.io.IOException;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


/**
 * FXML Controller class
 *
 * @author nidha
 */
public class TableViewController implements Initializable {
    @FXML
    private Button buttonLogout;

    @FXML
    private void handleButtonLogout() {
        // Met à jour la date de logout pour la dernière session de l'utilisateur connecté
        try {
            // Récupérer l'utilisateur connecté (id_user)
            // Ici, on suppose que l'id_user est stocké dans une variable statique ou session
            // À adapter selon la gestion de session de votre application
            int userId = getCurrentUserId();
            Connection con = LaConnexion.seConnecter();
            // Met à jour la date_logout pour la dernière entrée login de cet utilisateur
            String updateLogout = "UPDATE login SET date_logout = CURRENT_TIMESTAMP WHERE id_user = ? AND date_logout IS NULL ORDER BY date_login DESC LIMIT 1";
            PreparedStatement stmt = con.prepareStatement(updateLogout);
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // Redirection vers la page de login
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

    // Méthode utilitaire à adapter selon la gestion de session
    private int getCurrentUserId() {
        // TODO: Remplacer par la vraie logique de récupération de l'utilisateur connecté
        // Par exemple, stocker l'id dans une variable statique lors du login
        return Login_Register.Login_registerController.currentUserId;
    }
 @FXML
    private TableColumn<Patient, String> AdresseCol;

    @FXML
    private TableColumn<Patient, String> EditCol;

    @FXML
    private TableColumn<Patient, String> NameCol;
    
     @FXML
    private TableColumn<Patient, String> idCol;

    @FXML
    private TableColumn<Patient, String> PrenomCol;

    @FXML
    private TableView<Patient> TablePatients;

    @FXML
    private TableColumn<Patient, String> TelCol;

   
    @FXML
    private Button btnAdd;
     @FXML
    private Button afficherMed;

    @FXML
    private Button btnAddMed;
    @FXML
    private Button VoirMed;
    
    @FXML
    private Button btnDelete;

    @FXML
    private Button btnUpdate;

    @FXML
    private TextField txtAdresse;

    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtPrenom;

    @FXML
    private TextField txtTel;
    @FXML
    private Button btnRetour;

    @FXML
    private ComboBox<String> cmbMedicaments;
     @FXML
    private TextField ListeNom;

    @FXML
    private TextField ListePrenom;
    @FXML
    private Button btnSuppMed;

   @FXML
    private ComboBox<String> ComboMed;
      @FXML
    private AnchorPane ListeMedicaments;
    @FXML
    void Ajouter() {
  
         String nom=txtNom.getText();
         String prenom=txtPrenom.getText();
         String tels=txtTel.getText();
         String adresse=txtTel.getText();
         int tel = Integer.parseInt(tels);
        AlertMessage alert=new AlertMessage();
    try { 
         prepare=con.prepareStatement("insert into patient (nom,prenom,tel,adresse) values (?,?,?,?)");
         prepare.setString(1, nom);
         prepare.setString(2, prenom);
         prepare.setInt(3, tel);
         prepare.setString(4, adresse);
         prepare.executeUpdate();
         alert.sucessMessage("Ajouté avec succés");
         table();
         clearForm();
     } catch (SQLException ex) {
         Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
     }
        

    }
     @FXML
    void AfficherAjouterMedicament() {
        btnAddMed.setVisible(true);
        cmbMedicaments.setVisible(true);
        afficherMed.setVisible(false);
    }
     @FXML
    void AjouterMedicament() {
        afficherMed.setVisible(true);
         cmbMedicaments.setVisible(false);
         String nom=txtNom.getText();
         String prenom=txtPrenom.getText();
         String tels=txtTel.getText();
         String adresse=txtTel.getText();
         int tel = Integer.parseInt(tels);
         String selectedMedication = cmbMedicaments.getValue();
        AlertMessage alert=new AlertMessage();
    try { 
         prepare = con.prepareStatement("INSERT INTO listemedicaments (id_pat, id_med) SELECT p.id, m.id FROM patient p, medicament m WHERE p.nom = ? AND m.nom = ?");
        prepare.setString(1, nom);
        prepare.setString(2, selectedMedication);
        prepare.executeUpdate();
        alert.sucessMessage("Médicament Ajouté avec succès");
        table();
        clearForm();
     } catch (SQLException ex) {
         Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
     }
        

    }

   @FXML
    void Modifier() {
   int myIndex = TablePatients.getSelectionModel().getSelectedIndex();
   int id = Integer.parseInt(String.valueOf(TablePatients.getItems().get(myIndex).getId()));
        String nom = txtNom.getText();
        String prenom = txtPrenom.getText();
        String tels = txtTel.getText();
        String adresse = txtAdresse.getText();
        int tel = Integer.parseInt(tels);

        try {
            prepare = con.prepareStatement("update patient set nom = ?, prenom = ?, tel = ?, adresse = ? where id = ?");
            prepare.setString(1, nom);
            prepare.setString(2, prenom);
            prepare.setInt(3, tel);
            prepare.setString(4, adresse);
            prepare.setInt(5, id);
            prepare.executeUpdate();
            AlertMessage alert = new AlertMessage();
            alert.sucessMessage("Modifié avec succès");
            table(); 
            clearForm();
        } catch (SQLException ex) {
            Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    
}
     @FXML
    void Redirect() {
     try {
         FXMLLoader loader = new FXMLLoader(getClass().getResource("Medicament.fxml"));
         Parent tableViewParent = loader.load();
         Scene tableViewScene = new Scene(tableViewParent);
         Stage stage = (Stage) VoirMed.getScene().getWindow();
         stage.setScene(tableViewScene);
         stage.show();
     } catch (IOException ex) {
         Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
     }
    }
     @FXML
    void RetourPatient() {
     try {
         FXMLLoader loader = new FXMLLoader(getClass().getResource("TableView.fxml"));
         Parent tableViewParent = loader.load();
         Scene tableViewScene = new Scene(tableViewParent);
         Stage stage = (Stage) btnRetour.getScene().getWindow();
         stage.setScene(tableViewScene);
         stage.show();
     } catch (IOException ex) {
         Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
     }
    }

    @FXML
    void Supprimer() {
   
     int myIndex = TablePatients.getSelectionModel().getSelectedIndex();
         
     int id = Integer.parseInt(String.valueOf(TablePatients.getItems().get(myIndex).getId()));
             
        try 
        {
            prepare = con.prepareStatement("delete from patient where id = ? ");
            prepare.setInt(1, id);
            prepare.executeUpdate();
            
            AlertMessage alert = new AlertMessage();
            alert.sucessMessage("Supprimé avec succés");
            table();
            clearForm();
        } 
        catch (SQLException ex)
        {
            Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     ObservableList<String> MedList= FXCollections.observableArrayList();
     ResultSet result2=null;
     ResultSet result3=null;
     ResultSet result4=null;
     PreparedStatement prepare2=null;
     PreparedStatement prepare3=null;
    @FXML



    void Lister() {
    ListeMedicaments.setVisible(true);
    int myIndex = TablePatients.getSelectionModel().getSelectedIndex();
    int id = Integer.parseInt(String.valueOf(TablePatients.getItems().get(myIndex).getId()));

    try {
        prepare = con.prepareStatement("Select nom,prenom from patient where id = ? ");
        prepare.setInt(1, id);
        result2 = prepare.executeQuery();

        while (result2.next()) {
            ListeNom.setText(result2.getString("nom"));
            ListePrenom.setText(result2.getString("prenom"));
        }

        prepare2 = con.prepareStatement("select id_med from listemedicaments where id_pat = ?");
        prepare2.setInt(1, id);
        result3 = prepare2.executeQuery();

        MedList.clear();
        while (result3.next()) {
            int idmed = result3.getInt("id_med");

            prepare3 = con.prepareStatement("select nom from medicament where id = ?");
            prepare3.setInt(1, idmed);
            result4 = prepare3.executeQuery();

            while (result4.next()) {
                String medicamentNom = result4.getString("nom");
                MedList.add(medicamentNom);
                //System.out.println("medicament ajouté : " + medicamentNom);
            }
        }

        ComboMed.setItems(MedList);
      
    } catch (SQLException ex) {
        Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
    }
}

 @FXML



    void onCLick() {
    String selectedValue = ComboMed.getValue();
    System.out.println("Selected value: " + selectedValue);
      try 
        {
            prepare=con.prepareStatement("select id from medicament where nom = ?");
            prepare.setString(1,selectedValue);
            result=prepare.executeQuery();
            while(result.next()){
                int id=result.getInt("id");
                prepare2 = con.prepareStatement("delete from listemedicaments where id_med = ? LIMIT 1 ");
                prepare2.setInt(1, id);
                prepare2.executeUpdate();
                 ComboMed.getItems().clear(); 
                  Lister();
            
            AlertMessage alert = new AlertMessage();
            alert.sucessMessage("Supprimé avec succés");
            
            }
          
            
        } 
        catch (SQLException ex)
        {
            Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    String query=null;
    private Connection con=null;
    PreparedStatement prepare=null;
    ResultSet result=null;
    Patient patient=null;
    ObservableList<Patient> PatientsList= FXCollections.observableArrayList();
    @FXML
    /*void Refresh() {
     try {
         PatientsList.clear();
         query="select * from patient";
         prepare=con.prepareStatement(query);
         result=prepare.executeQuery();
         while(result.next()){
            this.PatientsList.add(new Patient(
            result.getString("nom"),result.getString("prenom"),result.getInt("tel"),result.getString("adresse")));
            TablePatients.setItems(PatientsList);
         }
     } catch (SQLException ex) {
         Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
     }
    }*/
    public void table()
      {
          con=LaConnexion.seConnecter(); 
       
       try 
       {
         PatientsList.clear();
         query="select * from patient";
         prepare=con.prepareStatement(query);
         result=prepare.executeQuery();
      {
        while (result.next())
        {
           this.PatientsList.add(new Patient(result.getInt("id"),
            result.getString("nom"),result.getString("prenom"),result.getInt("tel"),result.getString("adresse")));
       }
    } 
                TablePatients.setItems(PatientsList);
                 idCol.setCellValueFactory(new PropertyValueFactory<>("id"));   
                 NameCol.setCellValueFactory(new PropertyValueFactory<>("nom"));   
                PrenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));  
                 AdresseCol.setCellValueFactory(new PropertyValueFactory<>("adresse"));
                TelCol.setCellValueFactory(new PropertyValueFactory<>("tel"));   
               
       }
       
       catch (SQLException ex) 
       {
            Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
       }
             TablePatients.setRowFactory( tv -> {
             TableRow<Patient> myRow = new TableRow<>();
             myRow.setOnMouseClicked (event -> 
             {
                if (event.getClickCount() == 1 && (!myRow.isEmpty()))
                {
                    int myIndex = TablePatients.getSelectionModel().getSelectedIndex();
         
               
                   txtNom.setText(TablePatients.getItems().get(myIndex).getNom());
                   txtPrenom.setText(TablePatients.getItems().get(myIndex).getPrenom());
                   txtTel.setText(String.valueOf(TablePatients.getItems().get(myIndex).getTel()));
                   txtAdresse.setText(TablePatients.getItems().get(myIndex).getAdresse());
                   
                           
                         
                           
                }
             });
                return myRow;
                   });
    
    
      }
    public void clearForm(){
         txtNom.setText("");
         txtPrenom.setText("");
         txtTel.setText("");
         txtAdresse.setText("");
    }
    private void loadMedicaments() {
        con=LaConnexion.seConnecter(); 
    try {
        String query = "select nom from medicament";
        prepare = con.prepareStatement(query);
        result = prepare.executeQuery();
        ObservableList<String> medicationNames = FXCollections.observableArrayList();
        while (result.next()) {
            medicationNames.add(result.getString("nom"));
        }
        cmbMedicaments.setItems(medicationNames);
    } catch (SQLException ex) {
        Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
    }
}
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //loadData();
        loadMedicaments();
        table();
    }    
   /* public void loadData(){
       //con=LaConnexion.seConnecter();
       //this.Refresh();
        //NameCol.setCellValueFactory(new PropertyValueFactory<>("nom"));   
       // PrenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));  
        //AdresseCol.setCellValueFactory(new PropertyValueFactory<>("adresse"));
       // NameCol.setCellValueFactory(new PropertyValueFactory<>("nom")); 
        //TelCol.setCellValueFactory(new PropertyValueFactory<>("tel"));   
    }*/



    @FXML
    private Button btnAccueil;
    @FXML
    private void goAccueil(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/accueil.fxml"));
            Parent accueilParent = loader.load();
            Scene accueilScene = new Scene(accueilParent);
            Stage stage = null;
            if (btnAccueil != null && btnAccueil.getScene() != null) {
                stage = (Stage) btnAccueil.getScene().getWindow();
            } else if (event != null && event.getSource() instanceof Button) {
                Button sourceButton = (Button) event.getSource();
                if (sourceButton.getScene() != null) {
                    stage = (Stage) sourceButton.getScene().getWindow();
                }
            }
            if (stage != null) {
                stage.setScene(accueilScene);
            } else {
                System.err.println("Unable to get the current stage for navigation.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
