package Gui.Controllers;

import Gui.PortfolioManagementClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import static Gui.Controllers.NouveauContrat.updateObject;
import static Gui.PortfolioManagementClient.current_supply_point;

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

    JSONObject current ;
    JSONObject consommation ;
    public void initialize(){
        current = current_supply_point;
        JSONArray consommations = current.getJSONArray("consommationValues");
        consommation = consommations.getJSONObject(consommations.length()-1);
        textConsommationActuel.setText(String.valueOf(consommation.getDouble("value")));
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
        PortfolioManagementClient.stage.close();
        PortfolioManagementClient.showPages("MenuPrincipale.fxml");

    }
}
