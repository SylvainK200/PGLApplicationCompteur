package Gui.Controllers.ApplicationProvider;

import Gui.Controllers.Methods.GeneralMethods;
import Gui.Controllers.Methods.GeneralMethodsImpl;
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
import java.lang.Exception;
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
    GeneralMethods generalMethods = new GeneralMethodsImpl();
    JSONObject current ;
    JSONObject consommation ;

    public void initialize(){
        try{
            current = current_supply_point;
            JSONArray consommationsValues =  generalMethods.find("supplyPoint");
            List<JSONObject> consommations = extractConsommations(consommationsValues,current.getLong("id"));
            if (consommations.size()>0){
                consommation = consommations.get(consommations.size()-1);
                textConsommationActuel.setText(String.valueOf(consommation.getDouble("value")));
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Aucune consommation trouvée.", "Message", JOptionPane.INFORMATION_MESSAGE);
            valider.setDisable(true);
            annuler_button.setDisable(true);
            textConsommationCorriges.setDisable(true);
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
                    generalMethods.updateObject(consommation,"consommationValue");
                }
        }
    }
    @FXML
    void quitterPage(ActionEvent event) {
        FacilitatorProviderLinkClient.stage.close();
        FacilitatorProviderLinkClient.showPages("MenuPrincipale.fxml");

    }
}
