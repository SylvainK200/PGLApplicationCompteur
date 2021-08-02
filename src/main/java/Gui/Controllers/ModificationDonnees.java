package Gui.Controllers;

import Gui.PortfolioManagementClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ModificationDonnees {
    @FXML
    private Label labelConsommationActuel;

    @FXML
    private TextField textConsommationActuel;

    @FXML
    private Label labelConsommationCorriges;

    @FXML
    private TextField textConsommationCorriges;

    @FXML
    private Button annuler_button;

    @FXML
    private Button valider;

    @FXML
    void annuler(ActionEvent event) {
        PortfolioManagementClient.stage.close();
        PortfolioManagementClient.showPages("MenuPrincipale.fxml");
    }

    @FXML
    void valider(ActionEvent event) {
        PortfolioManagementClient.stage.close();
        PortfolioManagementClient.showPages("MenuPrincipale.fxml");
    }
}
