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
    void Ajouter() {
  
         String nom=txtNom.getText();
         String quanite=txtQte.getText();
         int Qte = Integer.parseInt(quanite);
         String date=String.valueOf(txtDate.getValue());
        AlertMessage alert=new AlertMessage();
    try { 
         prepare=con.prepareStatement("insert into medicament (nom,qte,dateexpiration) values (?,?,?)");
         prepare.setString(1, nom);
         prepare.setInt(2, Qte);
         prepare.setString(3, date);
         prepare.executeUpdate();
         alert.sucessMessage("Ajouté avec succés");
         table();
         clearForm();
     } catch (SQLException ex) {
         Logger.getLogger(MedicamentController.class.getName()).log(Level.SEVERE, null, ex);
     }
        

    }

   @FXML
    void Modifier() {
   int myIndex = TableMedicaments.getSelectionModel().getSelectedIndex();
   int id = Integer.parseInt(String.valueOf(TableMedicaments.getItems().get(myIndex).getId()));
        String nom = txtNom.getText();
        String quanite=txtQte.getText();
        int Qte = Integer.parseInt(quanite);
        String date=String.valueOf(txtDate.getValue());

        try {
            prepare = con.prepareStatement("update medicament set nom = ?, qte = ?, dateexpiration = ? where id = ?");
            prepare.setString(1, nom);
            prepare.setInt(2, Qte);
            prepare.setString(3, date);
            prepare.setInt(4, id);
            prepare.executeUpdate();
            AlertMessage alert = new AlertMessage();
            alert.sucessMessage("Modifié avec succès");
            table(); 
            clearForm();
        } catch (SQLException ex) {
           Logger.getLogger(MedicamentController.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    
}


    @FXML
    void Supprimer() {
   
     int myIndex = TableMedicaments.getSelectionModel().getSelectedIndex();
         
     int id = Integer.parseInt(String.valueOf(TableMedicaments.getItems().get(myIndex).getId()));
             
        try 
        {
            prepare = con.prepareStatement("delete from medicament where id = ? ");
            prepare.setInt(1, id);
            prepare.executeUpdate();
            
            AlertMessage alert = new AlertMessage();
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
   
    public void table()
      {
          con=LaConnexion.seConnecter();
        
       try 
       {
         MedicamentList.clear();
         query="select * from medicament";
         prepare=con.prepareStatement(query);
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
                    LocalDate date = LocalDate.parse(dateString); // Assuming the format of dateString is compatible with ISO_LOCAL_DATE
                   txtDate.setValue(date);


                   
                           
                         
                           
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
        //loadData();
        table();
    }    
  
   
}
