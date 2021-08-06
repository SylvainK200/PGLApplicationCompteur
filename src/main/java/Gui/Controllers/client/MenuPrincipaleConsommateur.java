package Gui.Controllers.client;

import Gui.FacilitatorProviderLinkClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.json.JSONArray;
import org.json.JSONObject;

import static Gui.Controllers.NouveauContrat.find;

public class MenuPrincipaleConsommateur {

    @FXML
    private MenuItem contrats_button;

    @FXML
    private Menu historique;

    @FXML
    private Menu exportationDonnees;

    @FXML
    private Menu deconnecter;

    @FXML
    private RadioButton compteur_ouvert;

    @FXML
    private RadioButton compteur_cloturer;

    @FXML
    private ComboBox<?> ean_18;

    @FXML
    private Label consommation_recente;

    @FXML
    private Label utilisateur;

    @FXML
    private Label nom;
    @FXML
    private TableView<?> table;

    @FXML
    private TableColumn<?, ?> ean;

    @FXML
    private TableColumn<?, ?> type_energie;

    @FXML
    private TableColumn<?, ?> type_compteur;

    @FXML
    private TableColumn<?, ?> consommation;

    @FXML
    private TableColumn<?, ?> fournisseur;

    @FXML
    private TableColumn<?, ?> date_affectation;

    @FXML
    private TableColumn<?, ?> date_cloture;

    @FXML
    private TextField rechercher;

    @FXML
    private DatePicker date_lecture;

    @FXML
    private TextField valeur_vue;

    @FXML
    private Label date_recente;

    @FXML
    private Button valider;

    @FXML
    private ComboBox<?> ean_exporter;

    @FXML
    private DatePicker date_debut_importation;

    @FXML
    private DatePicker date_maximale;

    @FXML
    private Button exporter_button;

    @FXML
    private Button annuler;
    @FXML
    private ComboBox<String> wallets;
    public void initialize(){
        utilisateur.setText(FacilitatorProviderLinkClient.currentClient.getString("identifiant"));
        nom.setText(FacilitatorProviderLinkClient.currentClient.getString("name"));
        JSONArray walets = find ("/wallet/user"+ FacilitatorProviderLinkClient.currentClient.getLong("id"));

        for (Object wal : walets){
            if (wal instanceof JSONObject){
                wallets.getItems().add(((JSONObject)wal).getString("name"));
            }
        }
    }


    @FXML
    void annuler_button(ActionEvent event) {

    }
    @FXML
    void exporterDonnee(ActionEvent event) {

    }
    @FXML
    void deconnecter(ActionEvent event) {

    }

    @FXML
    void exporterDonnees(ActionEvent event) {

    }

    @FXML
    void goToContrats(ActionEvent event) {

    }

    @FXML
    void goToHistorique(ActionEvent event) {

    }

}
