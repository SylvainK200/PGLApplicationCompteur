package Gui.Controllers;


import static Gui.FacilitatorProviderLinkClient.currentprovider;

import Gui.Controllers.Methods.GeneralMethods;
import Gui.Controllers.Methods.GeneralMethodsImpl;
import Gui.ModelTabs.NewContractTable;
import Gui.FacilitatorProviderLinkClient;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

public class NouveauContrat {
    public enum MeterType {
        MONOHORARY, BIHORARY, EXCLUSIVENIGHT
    }

    @FXML
    private Label lblNumeroClient;

    @FXML
    private ComboBox<String> NumeroClient;

    @FXML
    private Label lblEAN;

    @FXML
    private ComboBox<String> combEAN;

    @FXML
    private Button confirmation_ajout;

    @FXML
    private TableView<NewContractTable> table;

    @FXML
    private TableColumn<NewContractTable, String> colNomClient;

    @FXML
    private TableColumn<NewContractTable, String> colNumeroContrat;

    @FXML
    private TableColumn<NewContractTable, String> colTypeContart;

    @FXML
    private TableColumn<NewContractTable, String> colDebutContrat;

    @FXML
    private TableColumn<NewContractTable, String> colFinContrat;

    @FXML
    private TableColumn<NewContractTable, String> colCompteur;

    @FXML
    private TableColumn<NewContractTable, String> colTypeEnergie;

    @FXML
    private TableColumn<NewContractTable, String> colEtatCompteur;
    @FXML
    private Button quitter;
    @FXML
    private DatePicker date_fin;
    @FXML
    private TextField network_manager_cost;

    @FXML
    private TextField tax_rate;

    @FXML
    private TextField over_tax_rate;
    @FXML
    private DatePicker date_debut;
    @FXML
    private Button creer_compteur;
    @FXML
    private ComboBox<String> meter_type;

    @FXML
    private TextField meter_rate;
    @FXML
    private TextField newEAN;
    @FXML
    private TextField budget;

    @FXML
    private TextField budgetType;
    @FXML
    private ComboBox<String> newEnergy;

    @FXML
    private ComboBox<String> portefeuille;
    private FilteredList<NewContractTable> contracts;

    GeneralMethods generalMethods = new GeneralMethodsImpl();

    public JSONObject findUserByIdentifiant(String identifiant){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:8085/energy-management/user/identifiant/"+identifiant)
                .method("GET", null)
                .build();
        JSONObject result = null;
        try {
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            System.out.println(res);
            result= new JSONObject(res);

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public static JSONArray findUsers(){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:8085/energy-management/user")
                .method("GET", null)
                .build();
        JSONArray result = null;
        try {
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            System.out.println(res);
            result= new JSONArray(res);

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }


    public void initialize(){
        meter_type.getItems().addAll(MeterType.MONOHORARY.toString(),
                MeterType.BIHORARY.toString(),MeterType.EXCLUSIVENIGHT.toString());
        newEnergy.getItems().addAll("Electricite","Eau","Gaz");
        System.out.println("Execution de initialize");
        JSONArray clients = findUsers();
        JSONArray supplies = generalMethods.find("supplyPoint/free");
        JSONObject client = new JSONObject();
        JSONObject supply = new JSONObject();
        ObservableList<String> clientsList = FXCollections.observableArrayList();
        ObservableList<String> eansList  = FXCollections.observableArrayList();
        for (int i = 0;i<clients.length();i++)
        {
            client = clients.getJSONObject(i);
            clientsList.add(client.getString("identifiant"));

        }
        for (int i = 0;i<supplies.length();i++){
            supply = supplies.getJSONObject(i);
            eansList.add(supply.getString("ean_18"));
        }

        NumeroClient.getItems().addAll(clientsList);
        combEAN.getItems().addAll(eansList);
        NumeroClient.valueProperty().addListener((ObservableValue<? extends  String>observable,String oldvalue,String newValue)->{
            initTable();

        });
    }
    void initTable(){
        colCompteur.setCellValueFactory(new PropertyValueFactory<NewContractTable,String>("compteur"));
        colDebutContrat.setCellValueFactory(new PropertyValueFactory<NewContractTable,String>("debut_contrat"));
        colFinContrat.setCellValueFactory(new PropertyValueFactory<NewContractTable,String>("fin_contrat"));
        colNumeroContrat.setCellValueFactory(new PropertyValueFactory<NewContractTable,String>("num_contrat"));
        colEtatCompteur.setCellValueFactory(new PropertyValueFactory<NewContractTable,String>("etat_compteur"));
        colTypeEnergie.setCellValueFactory(new PropertyValueFactory<NewContractTable,String>("typeEnergie"));
        colNomClient.setCellValueFactory(new PropertyValueFactory<NewContractTable,String>("nom_client"));
        JSONObject client  = this.findUserByIdentifiant(NumeroClient.getValue());
        JSONArray contract_supply = generalMethods.find("contractSupplyPoint/byProvider/"+currentprovider.getInt("id"));
        table.getItems().removeAll(table.getItems());

        for (int i =0;i<contract_supply.length();i++){
            System.out.println("Dans la boucle for");
            JSONObject current = contract_supply.getJSONObject(i);
            if (current.get("contract") instanceof JSONObject){
                if (((JSONObject) current.get("contract")).getString("client").equals(client.getString("identifiant"))){
                    table.getItems().add(new NewContractTable(client,contract_supply.getJSONObject(i)));
                }
            }

        }
    }


    @FXML
    void confirmerAjout(ActionEvent event) {
        int aleatoire = 100 + (int)((Math.random()+0.002)*10000);
        JSONObject contract_supply = new JSONObject();
        JSONObject contract = new JSONObject();
        JSONObject wallet = generalMethods.findUnique("wallet/name/"+portefeuille.getValue());
        JSONObject supplyPoint = generalMethods.findUnique("supplyPoint/ean_18/"+combEAN.getValue());

        contract.put("date_begin",date_debut.getValue());
        contract.put("date_end",date_fin.getValue());
        contract.put("numero_contract","num"+aleatoire);
        contract.put("client",NumeroClient.getValue());
        contract.put("provider",currentprovider);
        contract = generalMethods.createObject(contract,"contract");
        contract_supply.put("meter_type",meter_type.getValue());
        contract_supply.put("meter_rate",meter_rate.getText());
        contract_supply.put("contract",contract);
        contract_supply.put("supplyPoint",supplyPoint);
        contract_supply.put("network_manager_cost",Double.parseDouble(network_manager_cost.getText()));
        contract_supply.put("over_tax_rate",Double.parseDouble(over_tax_rate.getText()));
        contract_supply.put("tax_rate",Double.parseDouble(tax_rate.getText()));
        contract_supply.put("wallet",wallet);

        JSONObject client  = this.findUserByIdentifiant(NumeroClient.getValue());
        generalMethods.createObject(contract_supply,"contractSupplyPoint");
        table.getItems().add(new NewContractTable(client,contract_supply));

    }
    @FXML
    void creerCompteur(ActionEvent event){
        JSONObject compteur = new JSONObject();
        compteur.put("ean_18",newEAN.getText());
        compteur.put("energy",newEnergy.getValue());
        try{
            JSONObject res = generalMethods.createObject(compteur,"/supplyPoint");
            if (!res.isEmpty()){
                combEAN.getItems().add(newEAN.getText());
            }
            newEAN.setText("");
            newEnergy.setValue("");
            budget.setText("");
            budgetType.setText("");
        }catch (Exception e) {

        }
    }

    @FXML
    void quitterpage(ActionEvent event) {
        FacilitatorProviderLinkClient.stage.close();
        FacilitatorProviderLinkClient.showPages("MenuPrincipale.fxml");
    }

}
