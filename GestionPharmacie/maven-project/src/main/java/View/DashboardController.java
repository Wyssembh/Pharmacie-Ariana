package View;

import Connexion.LaConnexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class DashboardController {

    @FXML
    private BarChart<String, Number> patientMedicamentsChart;

    @FXML
    private BarChart<String, Number> medicamentQteChart;

    @FXML
    private PieChart topUsersChart;

    @FXML
    private BarChart<String, Number> expiringMedicamentsChart;

    @FXML
    private Button btnRetourAccueil;

    @FXML
    public void initialize() {
        loadPatientMedicamentsChart();
        loadMedicamentQteChart();
        loadTopUsersChart();
        loadTopExpiringMedicamentsChart();
    }

    private void loadPatientMedicamentsChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Nombre de medicaments");

        String sql = "SELECT CONCAT(p.nom, ' ', p.prenom) AS patient_name, COUNT(lm.id_med) AS total_med " +
                     "FROM patient p " +
                     "LEFT JOIN listemedicaments lm ON p.id = lm.id_pat " +
                     "GROUP BY p.id, p.nom, p.prenom " +
                     "ORDER BY total_med DESC";

        try {
            Connection con = LaConnexion.seConnecter();
            if (con == null) {
                return;
            }
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String patientName = rs.getString("patient_name");
                int totalMed = rs.getInt("total_med");
                series.getData().add(new XYChart.Data<>(patientName, totalMed));
            }
            patientMedicamentsChart.getData().clear();
            patientMedicamentsChart.getData().add(series);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadMedicamentQteChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Quantite");

        String sql = "SELECT nom, qte FROM medicament ORDER BY qte DESC";

        try {
            Connection con = LaConnexion.seConnecter();
            if (con == null) {
                return;
            }
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String nomMedicament = rs.getString("nom");
                int qte = rs.getInt("qte");
                series.getData().add(new XYChart.Data<>(nomMedicament, qte));
            }
            medicamentQteChart.getData().clear();
            medicamentQteChart.getData().add(series);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTopUsersChart() {
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();

        String sql = "SELECT u.username, COUNT(l.id) AS total_logins " +
                     "FROM users u " +
                     "JOIN login l ON u.id = l.id_user " +
                     "GROUP BY u.id, u.username " +
                     "ORDER BY total_logins DESC";

        try {
            Connection con = LaConnexion.seConnecter();
            if (con == null) {
                return;
            }
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String username = rs.getString("username");
                int totalLogins = rs.getInt("total_logins");
                pieData.add(new PieChart.Data(username + " (" + totalLogins + ")", totalLogins));
            }
            topUsersChart.setData(pieData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTopExpiringMedicamentsChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Top 5 expirant ce mois");

        String sql = "SELECT nom, qte, " +
                     "COALESCE(STR_TO_DATE(dateexpiration, '%Y-%m-%d'), STR_TO_DATE(dateexpiration, '%d/%m/%Y')) AS exp_date " +
                     "FROM medicament " +
                     "WHERE COALESCE(STR_TO_DATE(dateexpiration, '%Y-%m-%d'), STR_TO_DATE(dateexpiration, '%d/%m/%Y')) IS NOT NULL " +
                     "AND MONTH(COALESCE(STR_TO_DATE(dateexpiration, '%Y-%m-%d'), STR_TO_DATE(dateexpiration, '%d/%m/%Y'))) = MONTH(CURDATE()) " +
                     "AND YEAR(COALESCE(STR_TO_DATE(dateexpiration, '%Y-%m-%d'), STR_TO_DATE(dateexpiration, '%d/%m/%Y'))) = YEAR(CURDATE()) " +
                     "ORDER BY exp_date ASC LIMIT 5";

        try {
            Connection con = LaConnexion.seConnecter();
            if (con == null) {
                return;
            }
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String nomMedicament = rs.getString("nom");
                int qte = rs.getInt("qte");
                series.getData().add(new XYChart.Data<>(nomMedicament, qte));
            }
            expiringMedicamentsChart.getData().clear();
            expiringMedicamentsChart.getData().add(series);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goAccueil(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/accueil.fxml"));
            Parent accueilParent = loader.load();
            Scene accueilScene = new Scene(accueilParent);
            Stage stage = (Stage) btnRetourAccueil.getScene().getWindow();
            stage.setScene(accueilScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
