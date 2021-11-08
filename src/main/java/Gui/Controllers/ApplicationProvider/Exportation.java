package Gui.Controllers.ApplicationProvider;

import Gui.FacilitatorProviderLinkClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Exportation {
    @FXML
    private Label lblEAN;


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
        FacilitatorProviderLinkClient.stage.close();
        FacilitatorProviderLinkClient.showPages("MenuPrincipale.fxml");

    }

}
