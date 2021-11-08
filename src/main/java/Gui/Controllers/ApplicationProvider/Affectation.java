package Gui.Controllers.ApplicationProvider;
import Gui.FacilitatorProviderLinkClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Affectation {

    @FXML
    private Label labelConsommationActuel;

    @FXML
    private TextField textConsommationActuel;

    @FXML
    private Label labelNouveauConsommateur;

    @FXML
    private TextField textNouveauConsommation;

    @FXML
    private Button affecter_button;

    @FXML
    private Button reaffecter;

    @FXML
    private Button desaffecter;

    @FXML
    void affecterConsommation(ActionEvent event) {
        FacilitatorProviderLinkClient.stage.close();
        FacilitatorProviderLinkClient.showPages("MenuPrincipale.fxml");
    }

    @FXML
    void desaffecterConsommation(ActionEvent event) {
        FacilitatorProviderLinkClient.stage.close();
        FacilitatorProviderLinkClient.showPages("MenuPrincipale.fxml");
    }

    @FXML
    void reaffecterConsomation(ActionEvent event) {
        FacilitatorProviderLinkClient.stage.close();
        FacilitatorProviderLinkClient.showPages("MenuPrincipale.fxml");
    }

}
