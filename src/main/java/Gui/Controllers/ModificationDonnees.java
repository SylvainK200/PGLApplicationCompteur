package Gui.Controllers;

import Gui.FacilitatorProviderLinkClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.util.List;

import static Gui.Controllers.NouveauContrat.find;
import static Gui.Controllers.NouveauContrat.updateObject;
import static Gui.FacilitatorProviderLinkClient.*;

public class ModificationDonnees {
    @FXML
    private Label labelConsommationActuel;

    @FXML
    private TextField textConsommationActuel = new TextField();

    @FXML
    private Label labelConsommationCorriges;

    @FXML
    private TextField textConsommationCorriges =new TextField();

    @FXML
    private Button annuler_button;

    @FXML
    private Button valider;

    JSONObject current ;
    JSONObject consommation ;
    public void initialize(){
        current = current_supply_point;
        JSONArray consommationsValues =  find("consommationValue/historiqueRecent/"+current_supply_point.getLong("id"));
        List<JSONObject> consommations = extractConsommations(consommationsValues,current.getLong("id"));
        if (consommationsValues.length()>0){
            consommation = consommationsValues.getJSONObject(0);
            textConsommationActuel.setText(String.valueOf(consommation.getDouble("value")));
        }

    }
    @FXML
    void annuler(ActionEvent event) {
        textConsommationCorriges.setText("");
    }

    @FXML
    void valider(ActionEvent event) {
        if (!textConsommationCorriges.getText().equals("") && !textConsommationCorriges.getText().isEmpty()){
            int retour = JOptionPane.showConfirmDialog(null,"Etes vous sur de vouloir modifier la valeur de la consommation ? ");
                if (retour == 0) {
                    consommation.remove("value");
                    consommation.put("value",Double.parseDouble(textConsommationCorriges.getText()));
                    updateObject(consommation,"consommationValue");
                }
        }
    }
    @FXML
    void quitterPage(ActionEvent event) {
        FacilitatorProviderLinkClient.stage.close();
        FacilitatorProviderLinkClient.showPages("MenuPrincipale.fxml");

    }
}
