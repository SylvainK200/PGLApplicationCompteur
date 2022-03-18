package Gui.Controllers.ApplicationProvider;

import Gui.Controllers.Methods.GeneralMethods;
import Gui.Controllers.Methods.GeneralMethodsImpl;
import Gui.ModelTabs.AllContract;
import Gui.FacilitatorProviderLinkClient;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import static Gui.FacilitatorProviderLinkClient.currentprovider;

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
    private TextField recherche;


    @FXML
    private Button quitter;
    private ObservableList<AllContract> contrats = FXCollections.observableArrayList();
    private FilteredList<AllContract> contratsList;
    GeneralMethods generalMethods = new GeneralMethodsImpl();
    @FXML
    void quitterPage(ActionEvent event) {
        FacilitatorProviderLinkClient.stage.close();
        FacilitatorProviderLinkClient.showPages("MenuPrincipale.fxml");
    }

    public void initialize(){
        initTable();
        contratsList = new FilteredList<>(contrats,p->true);
        recherche.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) ->{
            contratsList.setPredicate(
                    contrat->{
                        if (newValue==null|| newValue.isEmpty()){

                            return true;
                        }
                        if (contrat.getNum_contrat().toLowerCase().contains(newValue.toLowerCase()) ||
                            contrat.getNom_client().toLowerCase().contains(newValue.toLowerCase()) ||
                                contrat.getEnergy().toLowerCase().contains(newValue.toLowerCase()) ||
                                        contrat.getCompteur().toLowerCase().toLowerCase().contains((newValue.toLowerCase())) ||
                        contrat.getEtat_compteur().toLowerCase().contains(newValue.toLowerCase())) {

                            return true;
                        }

                        return false;
                    }
            );
            table_contrat.getItems().clear();
            table_contrat.getItems().addAll(contratsList);
        });

    }
    void initTable(){
        colCompteur.setCellValueFactory(new PropertyValueFactory<AllContract,String>("compteur"));
        colDebutContrat.setCellValueFactory(new PropertyValueFactory<AllContract,String>("debut_contrat"));
        colFinContrat.setCellValueFactory(new PropertyValueFactory<AllContract,String>("fin_contrat"));
        colNumeroContrat.setCellValueFactory(new PropertyValueFactory<AllContract,String>("num_contrat"));
        colTypeContart.setCellValueFactory(new PropertyValueFactory<AllContract,String>("type_contrat"));
        colEtatCompteur.setCellValueFactory(new PropertyValueFactory<AllContract,String>("etat_compteur"));
        colTypeEnergie.setCellValueFactory(new PropertyValueFactory<AllContract,String>("energy"));
        colNomClient.setCellValueFactory(new PropertyValueFactory<AllContract,String>("nom_client"));
        col_meter_type.setCellValueFactory(new PropertyValueFactory<AllContract,String>("meter_rate"));
        col_network_manager_cost.setCellValueFactory(new PropertyValueFactory<AllContract,Integer>("network_manager_cost"));
        col_tax_rate.setCellValueFactory(new PropertyValueFactory<AllContract,Integer>("tax_rate"));
        col_over_tax_rate.setCellValueFactory(new PropertyValueFactory<AllContract,Integer>("over_tax_rate"));

        JSONArray contract_supply = generalMethods.find("contractSupplyPoint/byProvider/"+currentprovider.getInt("id"));
        if(contract_supply.length()>0){
            for (int i =0;i<contract_supply.length();i++){
                // formation de chaque ligne du tableau a remplir
                JSONObject client = this.findClientOfContract(contract_supply.getJSONObject(i));
                if (!client.isEmpty()){
                    contrats.add(new AllContract(client,contract_supply.getJSONObject(i)));
                    table_contrat.getItems().add(new AllContract(client,contract_supply.getJSONObject(i)));}
            }
            System.out.println("remplissage termine");
        }else{
            System.out.println("Ce provider n'a pas de contracts");
        }
    }

    // recherche un client en fontion du nom contenu dans le contrat pour un supply point contract
    private  JSONObject findClientOfContract(JSONObject contract_supply_point){
        JSONObject client = new JSONObject();
        String identifiant = contract_supply_point.getString("client");
        System.out.println("identifiant" + identifiant);
        client = generalMethods.findUnique("user/identifiant/"+identifiant);
        return client;
    }

}

