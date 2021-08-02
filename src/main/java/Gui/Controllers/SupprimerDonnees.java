package Gui.Controllers;

import Gui.PortfolioManagementClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class SupprimerDonnees {
    @FXML
    private Label lblEAN;

    @FXML
    private ComboBox<?> compteur;

    @FXML
    private Button cloturerCompteur;

    @FXML
    private Label lblNotification;

    @FXML
    private TextArea notification_suppression;

    @FXML
    private Button supprimer_historique_button;

    @FXML
    void cloturerCompteurDonnee(ActionEvent event) {
        PortfolioManagementClient.stage.close();
        PortfolioManagementClient.showPages("MenuPrincipale.fxml");
    }

    @FXML
    void supprimerHistorique(ActionEvent event) {
        PortfolioManagementClient.stage.close();
        PortfolioManagementClient.showPages("MenuPrincipale.fxml");
    }
}
