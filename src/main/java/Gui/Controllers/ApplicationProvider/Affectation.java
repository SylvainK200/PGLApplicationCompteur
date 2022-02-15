package Gui.Controllers.ApplicationProvider;
import Gui.FacilitatorProviderLinkClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class Affectation {

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
