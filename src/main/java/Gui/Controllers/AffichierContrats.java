package Gui.Controllers;

import Gui.ModelTabs.AllContract;
import Gui.ModelTabs.NewContractTable;
import Gui.PortfolioManagementClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import static Gui.Controllers.NouveauContrat.find;
import static Gui.Controllers.NouveauContrat.findUnique;
import static Gui.PortfolioManagementClient.currentprovider;

public class AffichierContrats {
    @FXML
    private TableView<AllContract> table_contrat;

    @FXML
    private TableColumn<AllContract, String> colNomClient;

    @FXML
    private TableColumn<AllContract, String> colNumeroContrat;

    @FXML
    private TableColumn<AllContract, String> colTypeContart;

    @FXML
    private TableColumn<AllContract,String> colDebutContrat;

    @FXML
    private TableColumn<AllContract,String> colFinContrat;

    @FXML
    private TableColumn<AllContract,String> colCompteur;

    @FXML
    private TableColumn<AllContract,String> colTypeEnergie;

    @FXML
    private TableColumn<AllContract,String> colEtatCompteur;
    @FXML
    private TableColumn<AllContract, String> col_meter_type;

    @FXML
    private TableColumn<AllContract, Integer> col_network_manager_cost;

    @FXML
    private TableColumn<AllContract, Integer> col_tax_rate;

    @FXML
    private TableColumn<AllContract, Integer> col_over_tax_rate;


    @FXML
    private Button quitter;

    @FXML
    void quitterPage(ActionEvent event) {
        PortfolioManagementClient.stage.close();
        PortfolioManagementClient.showPages("MenuPrincipale.fxml");
    }

    public void initialize(){
        initTable();
    }
    void initTable(){
        colCompteur.setCellValueFactory(new PropertyValueFactory<AllContract,String>("compteur"));
        colDebutContrat.setCellValueFactory(new PropertyValueFactory<AllContract,String>("debut_contrat"));
        colFinContrat.setCellValueFactory(new PropertyValueFactory<AllContract,String>("fin_contrat"));
        colNumeroContrat.setCellValueFactory(new PropertyValueFactory<AllContract,String>("num_contrat"));
        colTypeContart.setCellValueFactory(new PropertyValueFactory<AllContract,String>("type_contrat"));
        colEtatCompteur.setCellValueFactory(new PropertyValueFactory<AllContract,String>("etat_compteur"));
        //colTypeEnergie.setCellValueFactory(new PropertyValueFactory<AllContract,String>("typeEnergie"));
        colNomClient.setCellValueFactory(new PropertyValueFactory<AllContract,String>("nom_client"));
        col_meter_type.setCellValueFactory(new PropertyValueFactory<AllContract,String>("meter_rate"));
        col_network_manager_cost.setCellValueFactory(new PropertyValueFactory<AllContract,Integer>("network_manager_cost"));
        col_tax_rate.setCellValueFactory(new PropertyValueFactory<AllContract,Integer>("tax_rate"));
        col_over_tax_rate.setCellValueFactory(new PropertyValueFactory<AllContract,Integer>("over_tax_rate"));
        JSONArray contract_supply = find("contractSupplyPoint/byProvider/"+currentprovider.getInt("id"));
        for (int i =0;i<contract_supply.length();i++){
            // formation de chaque ligne du tableau a remplir
            JSONObject client = this.findClientOfContract(contract_supply.getJSONObject(i));
            if (!client.isEmpty()){
            table_contrat.getItems().add(new AllContract(client,contract_supply.getJSONObject(i)));}
        }
        System.out.println("remplissage termine");
    }

    // recherche un client en fontion du nom contenu dans le contrat pour un supply point contract
    private  JSONObject findClientOfContract(JSONObject contract_supply_point){
        JSONObject client = new JSONObject();
        Object json = contract_supply_point.get("contract");
        if (json instanceof  JSONObject) {
            String identifiant = ((JSONObject) json).getString("client");
            System.out.println("identifiant" + identifiant);
            client = findUnique("user/identifiant/"+identifiant);
        }
        if (client == null){
            return new JSONObject();
        }
        return client;
    }

}

