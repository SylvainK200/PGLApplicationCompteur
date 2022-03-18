package Gui.Controllers.ApplicationProvider;


import static Gui.Controllers.Methods.GeneralMethodsImpl.API_URL;
import static Gui.FacilitatorProviderLinkClient.currentprovider;

import Gui.Controllers.Methods.GeneralMethods;
import Gui.Controllers.Methods.GeneralMethodsImpl;
import Gui.ModelTabs.NewContractTable;
import Gui.FacilitatorProviderLinkClient;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;

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

    GeneralMethods generalMethods = new GeneralMethodsImpl();

    public JSONObject findUserByIdentifiant(String identifiant){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(API_URL+"/user/identifiant/"+identifiant)
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
                .url(API_URL+"/user")
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

        // Charger les portefeuilles du client selectionné
        NumeroClient.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            JSONArray list_portefeuilles = generalMethods.find("wallet/identifiant/"+newValue);

            portefeuille.getItems().clear();

            for (int i = 0;i<list_portefeuilles.length();i++)
            {
                portefeuille.getItems().add( list_portefeuilles.getJSONObject(i).getString("name"));

            }
        }); 

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

            if(current.has("contract")){
                if (current.get("contract") instanceof JSONObject){
                    if (((JSONObject) current.get("contract")).getString("client").equals(client.getString("identifiant"))){
                        table.getItems().add(new NewContractTable(client,contract_supply.getJSONObject(i)));
                    }
                }
            }

        }
    }


    @FXML
    void confirmerAjout(ActionEvent event) {

        if(date_debut.getValue()==null || date_fin.getValue()==null || portefeuille.getValue() ==null || meter_type.getValue()==null || 
           meter_rate.getText().isEmpty() || network_manager_cost.getText().isEmpty() || over_tax_rate.getText().isEmpty() || 
           tax_rate.getText().isEmpty()){
            JOptionPane.showMessageDialog(null,"Remplissez tous les champs.", "Message", JOptionPane.INFORMATION_MESSAGE);
        }else{
            if(date_fin.getValue().isAfter(date_debut.getValue())){
                String aleatoire = ""+ System.currentTimeMillis() + (char)( (int)(Math.random()*10) );
                JSONObject contract_supply = new JSONObject();
                JSONObject wallet = generalMethods.findUnique("wallet/name/"+portefeuille.getValue());
                JSONObject supplyPoint = generalMethods.findUnique("supplyPoint/ean_18/"+combEAN.getValue());

                supplyPoint = new JSONObject(supplyPoint, "id");
                wallet = new JSONObject(wallet, "id");

                contract_supply.put("date_begin",date_debut.getValue());
                contract_supply.put("date_end",date_fin.getValue());
                contract_supply.put("numero_contract","num"+aleatoire);
                contract_supply.put("client",NumeroClient.getValue());
                contract_supply.put("provider",currentprovider);
                contract_supply.put("meter_type",meter_type.getValue());
                contract_supply.put("meter_rate",meter_rate.getText());
                contract_supply.put("supplyPoint",supplyPoint);
                contract_supply.put("network_manager_cost",Double.parseDouble(network_manager_cost.getText()));
                contract_supply.put("over_tax_rate",Double.parseDouble(over_tax_rate.getText()));
                contract_supply.put("tax_rate",Double.parseDouble(tax_rate.getText()));
                contract_supply.put("wallet",wallet);

                JSONObject client  = this.findUserByIdentifiant(NumeroClient.getValue());
                generalMethods.createObject(contract_supply,"contractSupplyPoint");
                table.getItems().add(new NewContractTable(client,contract_supply));
            }else{
                JOptionPane.showMessageDialog(null,"La date de fin doit venir après celle de début.", "Message", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
   
    @FXML
    void creerCompteur(ActionEvent event){
        if( newEnergy.getValue()==null || newEAN.getText() == "" ){
            JOptionPane.showMessageDialog(null,"Remplissez tous les champs.", "Message", JOptionPane.INFORMATION_MESSAGE);
        }else{
            JSONObject compteur = new JSONObject();
            JSONObject pointFourniture = new JSONObject();

            compteur.put("ean_18",newEAN.getText());
            compteur.put("energy",newEnergy.getValue());
            compteur.put("name", newEAN.getText() + '_' + currentprovider.getString("company_name"));

            pointFourniture.put("ean_18",newEAN.getText());
            pointFourniture.put("energy",newEnergy.getValue());
            pointFourniture.put("name", newEAN.getText() + '_' + currentprovider.getString("company_name"));
            pointFourniture.put("provider",currentprovider);

            compteur.put("pointFourniture", pointFourniture);

            try{
                generalMethods.createObject(compteur,"/supplyPoint");
                
                combEAN.getItems().add(newEAN.getText());
                JOptionPane.showMessageDialog(null,"La creation du compteur terminee", "Message", JOptionPane.INFORMATION_MESSAGE);

                newEAN.setText("");
                newEnergy.setValue("");
                budget.setText("");
                budgetType.setText("");
            }catch (Exception e) {
                JOptionPane.showMessageDialog(null,"La creation du compteur a echoué", "Message", JOptionPane.INFORMATION_MESSAGE);
                System.out.println("Creating new compteur failed NouveauContract.java -> creerCompteur");
            }
        }
    }

    @FXML
    void quitterpage(ActionEvent event) {
        FacilitatorProviderLinkClient.stage.close();
        FacilitatorProviderLinkClient.showPages("MenuPrincipale.fxml");
    }

}
