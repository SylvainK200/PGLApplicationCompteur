package Gui.Controllers;

import Gui.FacilitatorProviderLinkClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.sql.Date;
import java.util.Objects;

import static Gui.Controllers.NouveauContrat.*;
import static Gui.FacilitatorProviderLinkClient.currentprovider;

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
        if (Objects.nonNull(compteurs))
        {
            for (int i = 0 ; i<compteurs.length();i++){
                JSONObject compt = compteurs.getJSONObject(i);

                compteur.getItems().add(compt.getString("ean_18"));
            }
        }

    }
    @FXML
    void cloturerCompteurDonnee(ActionEvent event) {
        selectedCompteur = findUnique("supplyPoint/ean_18/"+compteur.getValue());
        //selectedCompteur.remove("home");
       // selectedCompteur.put("home", (Collection<?>) null);
        JSONObject currentContract = findUnique("contractSupplyPoint/currentcontract/ean/"+selectedCompteur.getString("ean_18"));
        currentContract.remove("date_cloture");
        currentContract.put("date_cloture",new Date(System.currentTimeMillis()));
        updateObject(currentContract,"contractSupplyPoint");

        /*PortfolioManagementClient.stage.close();
        PortfolioManagementClient.showPages("MenuPrincipale.fxml");*/
    }
    @FXML
    void quitter(ActionEvent event) {
        FacilitatorProviderLinkClient.stage.close();
        FacilitatorProviderLinkClient.showPages("MenuPrincipale.fxml");
    }
    @FXML
    void supprimerHistorique(ActionEvent event) {
        JSONObject notification  = new JSONObject();
        JSONObject selected = findUnique("supplyPoint/ean_18/"+compteur.getValue());
        long idSupplyPoint = selected.getLong("id");
        JSONArray arrays = find("consommationValue");
        for (int i = 0 ; i < arrays.length();i++){
            JSONObject o = arrays.getJSONObject(i).getJSONObject("supplyPoint");
            long idCons = arrays.getJSONObject(i).getLong("id");
            long id = o.getLong("id");
            //System.out.println(" id : "+id + " idCons : "+idSupplyPoint);
            if (id == idSupplyPoint )
            {
                JSONObject deleted = deleteObject("consommationValue/"+idCons);
                System.out.println("element numero : "+i);
            }

        }
        selectedCompteur = selected;

        notification.put("content",notification_suppression.getText());
        notification.put("supplyPoint",selectedCompteur);
        notification.put("provider",currentprovider);
        JSONObject res = createObject(notification,"notification");
        if (!res.isEmpty()){
            JOptionPane.showMessageDialog(null,"Notification cree");

        }else {
            JOptionPane.showMessageDialog(null,"resultat vide ");
        }

    }
}
