package Gui.Controllers;


import Gui.PortfolioManagementClient;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuPrincipale  implements Initializable {
    @FXML
    private MenuItem modifier_donnee;
    @FXML
    private MenuItem supprimer_donnee;
    @FXML
    private MenuItem importer;
    @FXML
    private MenuItem exporter;
    @FXML
    private MenuItem afficher_contrat;
    @FXML
    private MenuItem etablir_contrat;
    @FXML
    private MenuItem se_deconnecter;

    @FXML
    private Label lblEAN;

    @FXML
    private TextField textEAN;

    @FXML
    private Button Buttonrecherche;

    @FXML
    private Label labelEAN;

    @FXML
    private TextField resultEAN;

    @FXML
    private Label lblConsommation;

    @FXML
    private TextField resultConsommation;

    @FXML
    private Label lblConsommateur;

    @FXML
    private TextField resultConsommateur;

    @FXML
    private Label lblClient;

    @FXML
    private ComboBox<?> combClient;

    @FXML
    private Label lblPortefeuille;

    @FXML
    private ComboBox<?> combPortefeuille;

    @FXML
    private Label lblCompteur;

    @FXML
    private ComboBox<?> compteur;

    @FXML
    private TableView<?> table;

    @FXML
    private TableColumn<?, ?> colEAN;

    @FXML
    private TableColumn<?, ?> colConsommation;

    @FXML
    private TableColumn<?, ?> colCout;

    @FXML
    private TableColumn<?, ?> colCompteur;

    @FXML
    private TableColumn<?, ?> colDateAffectation;

    @FXML
    private TableColumn<?, ?> colDateClotur;


    public void onclickrechercher(){
        resultEAN.setText(textEAN.getText());
        resultConsommation.setText(textEAN.getText());
        resultConsommateur.setText(textEAN.getText());
    }

    // Add a public no-args constructor
    public MenuPrincipale()
    {
    }

    @FXML
    private void initialize()
    {
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    @FXML
    public void goToModifierDonnee(){
        PortfolioManagementClient.stage.close();
        PortfolioManagementClient.showPages("ModificationDonnees.fxml");
    }
    @FXML
    public void goToSupprimerDonnee(){

        PortfolioManagementClient.stage.close();
        PortfolioManagementClient.showPages("SupprimerDonnes.fxml");
    }
    @FXML
    public void goToImporter(){

    }
    @FXML
    public void goToExporter(){
        PortfolioManagementClient.stage.close();
        PortfolioManagementClient.showPages("Exportation.fxml");
    }
    @FXML
    public void goToAfficherContrat() {
        PortfolioManagementClient.stage.close();
        PortfolioManagementClient.showPages("AffichierContrats.fxml");
    }
    @FXML
    public void goToEtablirContrat(){
        PortfolioManagementClient.stage.close();
        PortfolioManagementClient.showPages("NouveauContrat.fxml");
    }
    @FXML
    public void seDeconnecter(){
        PortfolioManagementClient.stage.close();
        PortfolioManagementClient.showPages("login.fxml");
    }
}
