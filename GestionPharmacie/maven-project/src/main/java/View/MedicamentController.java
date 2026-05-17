/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Connexion.LaConnexion;
import Login_Register.AlertMessage;
import Models.Medicament;
import Models.Patient;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author nidha
 */
public class MedicamentController implements Initializable {
     @FXML
    private TableColumn<Medicament, String> DateCol;

    @FXML
    private TableColumn<Medicament, String> NameCol;

    @FXML
    private TableColumn<Medicament, String> QteCol;

    @FXML
    private TableView<Medicament> TableMedicaments;

    @FXML
    private Button btnAdd;
    
    @FXML
    private Button VoirPat;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<Medicament,String> idCol;

    @FXML
    private DatePicker txtDate;

    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtQte;
    
    @FXML
    private TextField txtSearchMedicament;

  
    @FXML
    void Ajouter() {
        AlertMessage alert = new AlertMessage();
        String nom = txtNom.getText() == null ? "" : txtNom.getText().trim();
        String quantite = txtQte.getText() == null ? "" : txtQte.getText().trim();
        LocalDate date = txtDate.getValue();

        if (nom.isEmpty() || quantite.isEmpty() || date == null) {
            alert.errorMessage("Tous les champs sont obligatoires.");
            return;
        }

        int qte;
        try {
            qte = Integer.parseInt(quantite);
        } catch (NumberFormatException e) {
            alert.errorMessage("La quantité doit être un nombre valide.");
            return;
        }

        if (qte < 0) {
            alert.errorMessage("La quantité ne peut pas être négative.");
            return;
        }

        if (!date.isAfter(LocalDate.now())) {
            alert.errorMessage("La date d'expiration doit être supérieure à aujourd'hui.");
            return;
        }

        try {
            con = LaConnexion.seConnecter();
            if (con == null) {
                alert.errorMessage("Connexion base de données indisponible.");
                return;
            }

            if (medicamentNameExists(nom, null)) {
                alert.errorMessage("Un médicament avec ce nom existe déjà.");
                return;
            }

            prepare = con.prepareStatement("insert into medicament (nom,qte,dateexpiration) values (?,?,?)");
            prepare.setString(1, nom);
            prepare.setInt(2, qte);
            prepare.setString(3, date.toString());
            prepare.executeUpdate();
            alert.sucessMessage("Ajouté avec succès");
            table();
            clearForm();
            showLowStockAlerts();
        } catch (SQLException ex) {
            Logger.getLogger(MedicamentController.class.getName()).log(Level.SEVERE, null, ex);
            alert.errorMessage("Erreur lors de l'ajout du médicament.");
        }
    }

   @FXML
    void Modifier() {
        AlertMessage alert = new AlertMessage();
        int myIndex = TableMedicaments.getSelectionModel().getSelectedIndex();
        if (myIndex < 0) {
            alert.errorMessage("Veuillez sélectionner un médicament.");
            return;
        }
        int id = Integer.parseInt(String.valueOf(TableMedicaments.getItems().get(myIndex).getId()));
        String nom = txtNom.getText() == null ? "" : txtNom.getText().trim();
        String quantite = txtQte.getText() == null ? "" : txtQte.getText().trim();
        LocalDate date = txtDate.getValue();

        if (nom.isEmpty() || quantite.isEmpty() || date == null) {
            alert.errorMessage("Tous les champs sont obligatoires.");
            return;
        }

        int qte;
        try {
            qte = Integer.parseInt(quantite);
        } catch (NumberFormatException e) {
            alert.errorMessage("La quantité doit être un nombre valide.");
            return;
        }

        if (qte < 0) {
            alert.errorMessage("La quantité ne peut pas être négative.");
            return;
        }

        if (!date.isAfter(LocalDate.now())) {
            alert.errorMessage("La date d'expiration doit être supérieure à aujourd'hui.");
            return;
        }

        try {
            con = LaConnexion.seConnecter();
            if (con == null) {
                alert.errorMessage("Connexion base de données indisponible.");
                return;
            }

            if (medicamentNameExists(nom, id)) {
                alert.errorMessage("Un médicament avec ce nom existe déjà.");
                return;
            }

            prepare = con.prepareStatement("update medicament set nom = ?, qte = ?, dateexpiration = ? where id = ?");
            prepare.setString(1, nom);
            prepare.setInt(2, qte);
            prepare.setString(3, date.toString());
            prepare.setInt(4, id);
            prepare.executeUpdate();
            alert.sucessMessage("Modifié avec succès");
            table(); 
            clearForm();
            showLowStockAlerts();
        } catch (SQLException ex) {
           Logger.getLogger(MedicamentController.class.getName()).log(Level.SEVERE, null, ex);
           alert.errorMessage("Erreur lors de la modification du médicament.");
        }
    
}


    @FXML
    void Supprimer() {
        AlertMessage alert = new AlertMessage();
        int myIndex = TableMedicaments.getSelectionModel().getSelectedIndex();
        if (myIndex < 0) {
            alert.errorMessage("Veuillez sélectionner un médicament.");
            return;
        }
         
     int id = Integer.parseInt(String.valueOf(TableMedicaments.getItems().get(myIndex).getId()));
             
        try 
        {
            prepare = con.prepareStatement("delete from medicament where id = ? ");
            prepare.setInt(1, id);
            prepare.executeUpdate();
            
            alert.sucessMessage("Supprimé avec succés");
            table();
            clearForm();
        } 
        catch (SQLException ex)
        {
             Logger.getLogger(MedicamentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    String query=null;
    private Connection con=null;
    PreparedStatement prepare=null;
    ResultSet result=null;
    Medicament medicament=null;
    ObservableList<Medicament> MedicamentList= FXCollections.observableArrayList();
    @FXML
   
    public void table() {
        table(txtSearchMedicament != null ? txtSearchMedicament.getText() : null);
    }
    
    public void table(String searchTerm) {
          con=LaConnexion.seConnecter();
        
       try 
       {
         MedicamentList.clear();
         boolean hasSearch = searchTerm != null && !searchTerm.trim().isEmpty();
         query = hasSearch ? "select * from medicament where lower(nom) like ? order by id desc" : "select * from medicament order by id desc";
         prepare=con.prepareStatement(query);
         if (hasSearch) {
             prepare.setString(1, "%" + searchTerm.trim().toLowerCase() + "%");
         }
         result=prepare.executeQuery();
      {
        while (result.next())
        {
           this.MedicamentList.add(new Medicament(result.getInt("id"),
            result.getString("nom"),result.getInt("qte"),result.getString("dateexpiration")));
       }
    } 
                TableMedicaments.setItems(MedicamentList);
                idCol.setCellValueFactory(new PropertyValueFactory<>("id"));   
                NameCol.setCellValueFactory(new PropertyValueFactory<>("nom"));   
                QteCol.setCellValueFactory(new PropertyValueFactory<>("qte"));  
                DateCol.setCellValueFactory(new PropertyValueFactory<>("date"));   
               
       }
       
       catch (SQLException ex) 
       {
             Logger.getLogger(MedicamentController.class.getName()).log(Level.SEVERE, null, ex);
       }
             TableMedicaments.setRowFactory( tv -> {
             TableRow<Medicament> myRow = new TableRow<>();
             myRow.setOnMouseClicked (event -> 
             {
                if (event.getClickCount() == 1 && (!myRow.isEmpty()))
                {
                    int myIndex = TableMedicaments.getSelectionModel().getSelectedIndex();
         
               
                   txtNom.setText(TableMedicaments.getItems().get(myIndex).getNom());
                   txtQte.setText(String.valueOf(TableMedicaments.getItems().get(myIndex).getQte()));
                   String dateString = TableMedicaments.getItems().get(myIndex).getDate();
                    try {
                        LocalDate date = LocalDate.parse(dateString);
                        txtDate.setValue(date);
                    } catch (DateTimeParseException ex) {
                        txtDate.setValue(LocalDate.now().plusDays(1));
                    }


                   
                           
                         
                           
                }
             });
                return myRow;
                   });
    
    
      }
    @FXML
    void Redirect() {
     try {
         FXMLLoader loader = new FXMLLoader(getClass().getResource("TableView.fxml"));
         Parent tableViewParent = loader.load();
         Scene tableViewScene = new Scene(tableViewParent);
         Stage stage = (Stage) VoirPat.getScene().getWindow();
         stage.setScene(tableViewScene);
         stage.show();
     } catch (IOException ex) {
         Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
     }
    }
    public void clearForm(){
         txtNom.setText("");
         txtQte.setText("");
         txtDate.setValue(LocalDate.now());

        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        table();
        if (txtDate != null) {
            txtDate.setValue(LocalDate.now().plusDays(1));
        }
        if (txtSearchMedicament != null) {
            txtSearchMedicament.textProperty().addListener((obs, oldV, newV) -> table(newV));
        }
        showLowStockAlerts();
    }    
    
    private boolean medicamentNameExists(String nom, Integer excludeId) throws SQLException {
        String sql = excludeId == null
                ? "select count(*) as total from medicament where lower(nom)=lower(?)"
                : "select count(*) as total from medicament where lower(nom)=lower(?) and id <> ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, nom);
        if (excludeId != null) {
            stmt.setInt(2, excludeId);
        }
        ResultSet rs = stmt.executeQuery();
        return rs.next() && rs.getInt("total") > 0;
    }
    
    private void showLowStockAlerts() {
        try {
            con = LaConnexion.seConnecter();
            if (con == null) {
                return;
            }
            PreparedStatement stmt = con.prepareStatement(
                    "select nom, qte from medicament where qte <= 10 order by qte asc, nom asc");
            ResultSet rs = stmt.executeQuery();
            StringBuilder msg = new StringBuilder();
            while (rs.next()) {
                msg.append("- ")
                   .append(rs.getString("nom"))
                   .append(" : ")
                   .append(rs.getInt("qte"))
                   .append("\n");
            }
            if (msg.length() > 0) {
                new AlertMessage().warningMessage("Stock faible (<=10):\n" + msg);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MedicamentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  
   
}
