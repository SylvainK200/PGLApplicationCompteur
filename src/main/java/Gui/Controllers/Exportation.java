package Gui.Controllers;

import Gui.PortfolioManagementClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Exportation {
    @FXML
    private Label lblEAN;

    @FXML
    private ComboBox<?> compteur;

    @FXML
    private TextField textEAN;

    @FXML
    private Button ajouter_compteur_button;

    @FXML
    private Button exporter_donnee;

    @FXML
    private Button quitter;

    @FXML
    void AjouterCompteur(ActionEvent event) {

    }

    @FXML
    void exporterDonnee(ActionEvent event) {

    }

    @FXML
    void quitterPage(ActionEvent event) {
        PortfolioManagementClient.stage.close();
        PortfolioManagementClient.showPages("MenuPrincipale.fxml");

    }

}
