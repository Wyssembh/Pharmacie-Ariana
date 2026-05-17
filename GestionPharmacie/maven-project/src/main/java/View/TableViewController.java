/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Connexion.LaConnexion;
import Login_Register.AlertMessage;
import Login_Register.Login_registerController;
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
    private TextField txtSearchPatient;
    
    @FXML
    private TextField txtSearchMedicament;
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
    private Button buttonLogout;

    @FXML
    private void handleButtonLogout() {
        try {
            int userId = getCurrentUserId();
            Connection con = LaConnexion.seConnecter();
            String updateLogout = "UPDATE login SET date_logout = CURRENT_TIMESTAMP WHERE id_user = ? AND date_logout IS NULL ORDER BY date_login DESC LIMIT 1";
            PreparedStatement stmt = con.prepareStatement(updateLogout);
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

    private int getCurrentUserId() {
        return Login_Register.Login_registerController.currentUserId;
    }

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
    private final ObservableList<String> allMedicaments = FXCollections.observableArrayList();
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
    public void table() {
        table(txtSearchPatient != null ? txtSearchPatient.getText() : null);
    }

    public void table(String searchTerm) {
        con=LaConnexion.seConnecter(); 
       
        try 
        {
         PatientsList.clear();
         boolean hasSearch = searchTerm != null && !searchTerm.trim().isEmpty();
         query = hasSearch ? "select * from patient where lower(nom) like ? order by id desc" : "select * from patient order by id desc";
         prepare=con.prepareStatement(query);
         if (hasSearch) {
             prepare.setString(1, "%" + searchTerm.trim().toLowerCase() + "%");
        }
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
        String search = txtSearchMedicament != null ? txtSearchMedicament.getText() : null;
        boolean hasSearch = search != null && !search.trim().isEmpty();
        String query = hasSearch ? "select nom from medicament where lower(nom) like ? order by nom" : "select nom from medicament order by nom";
        prepare = con.prepareStatement(query);
        if (hasSearch) {
            prepare.setString(1, "%" + search.trim().toLowerCase() + "%");
        }
        result = prepare.executeQuery();
        allMedicaments.clear();
        while (result.next()) {
            allMedicaments.add(result.getString("nom"));
        }
        cmbMedicaments.setItems(FXCollections.observableArrayList(allMedicaments));
    } catch (SQLException ex) {
        Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
    }
}
    
    private void setupMedicamentsComboSearch() {
        if (cmbMedicaments == null) {
            return;
        }
        cmbMedicaments.setEditable(true);
        cmbMedicaments.getEditor().textProperty().addListener((obs, oldV, newV) -> {
            String typed = newV == null ? "" : newV.trim().toLowerCase();
            ObservableList<String> filtered = FXCollections.observableArrayList();
            for (String med : allMedicaments) {
                if (typed.isEmpty() || med.toLowerCase().contains(typed)) {
                    filtered.add(med);
                }
            }
            cmbMedicaments.setItems(filtered);
            cmbMedicaments.show();
        });
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
        String selectedMedication = cmbMedicaments.getValue();
         if ((selectedMedication == null || selectedMedication.trim().isEmpty()) && cmbMedicaments.getEditor() != null) {
             selectedMedication = cmbMedicaments.getEditor().getText();
         }
        AlertMessage alert=new AlertMessage();
        int myIndex = TablePatients.getSelectionModel().getSelectedIndex();
        if (myIndex < 0) {
            alert.errorMessage("Veuillez sélectionner un patient.");
            return;
        }
        if (selectedMedication == null || selectedMedication.trim().isEmpty()) {
            alert.errorMessage("Veuillez sélectionner un médicament.");
            return;
        }
        int patientId = TablePatients.getItems().get(myIndex).getId();
        try {
            con = LaConnexion.seConnecter();
            if (con == null) {
                alert.errorMessage("Connexion base de données indisponible.");
                return;
            }
            con.setAutoCommit(false);
            PreparedStatement getMedStmt = con.prepareStatement("select id, qte from medicament where nom = ?");
            getMedStmt.setString(1, selectedMedication);
            ResultSet medRs = getMedStmt.executeQuery();
            if (!medRs.next()) {
                alert.errorMessage("Médicament introuvable.");
                con.rollback();
                return;
            }
            int medId = medRs.getInt("id");
            int qte = medRs.getInt("qte");
            if (qte <= 0) {
                alert.errorMessage("Stock insuffisant pour ce médicament.");
                con.rollback();
                return;
            }

            PreparedStatement insertStmt = con.prepareStatement("insert into listemedicaments (id_pat, id_med) values (?,?)");
            insertStmt.setInt(1, patientId);
            insertStmt.setInt(2, medId);
            insertStmt.executeUpdate();

            PreparedStatement updateQteStmt = con.prepareStatement("update medicament set qte = qte - 1 where id = ? and qte > 0");
            updateQteStmt.setInt(1, medId);
            int updated = updateQteStmt.executeUpdate();
            if (updated == 0) {
                alert.errorMessage("Impossible de décrémenter le stock (déjà à 0).");
                con.rollback();
                return;
            }

            con.commit();
            alert.sucessMessage("Médicament ajouté avec succès au patient.");
            if (qte - 1 <= 10) {
                alert.warningMessage("Attention: stock faible pour " + selectedMedication + " (" + (qte - 1) + " restants).");
            }
            loadMedicaments();
            table();
            clearForm();
        } catch (SQLException ex) {
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException rollbackEx) {
                Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, rollbackEx);
            }
            Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
            alert.errorMessage("Erreur lors de l'ajout du médicament au patient.");
        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                }
            } catch (SQLException ex) {
                Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (txtSearchPatient != null) {
            txtSearchPatient.textProperty().addListener((obs, oldV, newV) -> table(newV));
        }
        if (txtSearchMedicament != null) {
            txtSearchMedicament.textProperty().addListener((obs, oldV, newV) -> loadMedicaments());
        }
        setupMedicamentsComboSearch();
        loadMedicaments();
        table();
        if (btnAccueil != null) {
            boolean isSuperadmin = "superadmin".equalsIgnoreCase(Login_registerController.currentUsername);
            btnAccueil.setVisible(isSuperadmin);
            btnAccueil.setManaged(isSuperadmin);
        }
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
            Parent accueilParent = FXMLLoader.load(getClass().getResource("/View/accueil.fxml"));
            Scene accueilScene = new Scene(accueilParent);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(accueilScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
