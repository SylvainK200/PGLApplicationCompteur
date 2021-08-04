package Gui.Controllers;

import Gui.PortfolioManagementClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.util.Collection;

import static Gui.Controllers.NouveauContrat.*;
import static Gui.PortfolioManagementClient.currentprovider;

public class SupprimerDonnees {
    @FXML
    private Label lblEAN;

    @FXML
    private ComboBox<String> compteur;

    @FXML
    private Button cloturerCompteur;

    @FXML
    private Label lblNotification;

    @FXML
    private TextArea notification_suppression;

    @FXML
    private Button supprimer_historique_button;
    @FXML
    private Button quitter;
    JSONObject selectedCompteur = new JSONObject();
    public void initialize(){
        JSONArray compteurs = find("supplyPoint");
        for (int i = 0 ; i<compteurs.length();i++){
            JSONObject compt = compteurs.getJSONObject(i);
            compteur.getItems().add(compt.getString("ean_18"));
        }

    }
    @FXML
    void cloturerCompteurDonnee(ActionEvent event) {
        selectedCompteur = findUnique("supplyPoint/ean_18/"+compteur.getValue());
        selectedCompteur.remove("home");
        selectedCompteur.put("home", (Collection<?>) null);

        updateObject(selectedCompteur,"supplyPoint");

        /*PortfolioManagementClient.stage.close();
        PortfolioManagementClient.showPages("MenuPrincipale.fxml");*/
    }
    @FXML
    void quitter(ActionEvent event) {
        PortfolioManagementClient.stage.close();
        PortfolioManagementClient.showPages("MenuPrincipale.fxml");
    }
    @FXML
    void supprimerHistorique(ActionEvent event) {
        JSONObject notification  = new JSONObject();

        for (int i = 0 ; i < selectedCompteur.getJSONArray("consommationValues").length();i++){

        }

        notification.put("content",notification_suppression.getText());
        notification.put("supplyPoint",selectedCompteur);
        notification.put("provider",currentprovider);
        JSONObject res = createObject(notification,"notification");
        if (!res.isEmpty()){
            JOptionPane.showMessageDialog(null,"Notification cree");

        }

    }
}
