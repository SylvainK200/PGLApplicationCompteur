package Gui.Controllers.ApplicationProvider;

import Gui.Controllers.Methods.GeneralMethods;
import Gui.Controllers.Methods.GeneralMethodsImpl;
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
    GeneralMethods generalMethods = new GeneralMethodsImpl();


    public void initialize(){
        JSONArray compteurs = generalMethods.find("supplyPoint");
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
        String compteurToClose = compteur.getValue();
        if( compteurToClose!=null && (!compteurToClose.isBlank() )){
            selectedCompteur = generalMethods.findUnique("supplyPoint/ean_18/"+compteurToClose);
            JSONObject currentContract = generalMethods.findUnique("contractSuppyPoint/currentcontract/ean/"+selectedCompteur.getString("ean_18"));
            
            currentContract.remove("dateCloture");
            currentContract.put("dateCloture",new Date(System.currentTimeMillis()));
            generalMethods.updateObject(currentContract,"contractSupplyPoint");

            JOptionPane.showMessageDialog(null,"Cloture effectuée", "Message", JOptionPane.INFORMATION_MESSAGE);
            compteur.getItems().remove(compteurToClose);
        }else{
            JOptionPane.showMessageDialog(null,"Veuillez selectionner un compteur", "Message", JOptionPane.INFORMATION_MESSAGE);
        }

    }
    @FXML
    void quitter(ActionEvent event) {
        FacilitatorProviderLinkClient.stage.close();
        FacilitatorProviderLinkClient.showPages("MenuPrincipale.fxml");
    }
    @FXML
    void supprimerHistorique(ActionEvent event) {

        String compteurToClose = compteur.getValue();
        if( compteurToClose!=null && (!compteurToClose.isBlank() )){
            JSONObject notification  = new JSONObject();
            JSONObject selected = generalMethods.findUnique("supplyPoint/ean_18/"+compteurToClose);
            long idSupplyPoint = selected.getLong("id");
            JSONArray arrays = generalMethods.find("consommationValue");
            for (int i = 0 ; i < arrays.length();i++){
                JSONObject o = arrays.getJSONObject(i).getJSONObject("supplyPoint");
                long idCons = arrays.getJSONObject(i).getLong("id");
                long id = o.getLong("id");
                if (id == idSupplyPoint )
                {
                    generalMethods.deleteObject("consommationValue/"+idCons);
                    System.out.println("element numero : "+i);
                }

            }
            selectedCompteur = selected;

            notification.put("content",notification_suppression.getText());
            notification.put("supplyPoint",selectedCompteur);
            notification.put("provider",currentprovider);
            JSONObject res = generalMethods.createObject(notification,"notification");
            if (!res.isEmpty()){
                JOptionPane.showMessageDialog(null,"Notification créée", "Message", JOptionPane.INFORMATION_MESSAGE);

            }else {
                JOptionPane.showMessageDialog(null,"resultat vide ", "Message", JOptionPane.INFORMATION_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(null,"Veuillez selectionner un compteur", "Message", JOptionPane.INFORMATION_MESSAGE);
        }

    }
}
