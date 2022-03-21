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
    private ComboBox<String> compteur;

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
    private TextField newCompteurName;
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

    JSONArray list_portefeuilles;

    public void initialize(){
        meter_type.getItems().addAll(MeterType.MONOHORARY.toString(),
                MeterType.BIHORARY.toString(),MeterType.EXCLUSIVENIGHT.toString());
        newEnergy.getItems().addAll("Electricite","Eau","Gaz");
        System.out.println("Execution de initialize");
        JSONArray clients = findUsers();

        JSONObject client = new JSONObject();
        ObservableList<String> clientsList = FXCollections.observableArrayList();
        for (int i = 0;i<clients.length();i++)
        {
            client = clients.getJSONObject(i);
            clientsList.add(client.getString("identifiant"));
        }

        NumeroClient.getItems().addAll(clientsList);

        // Charger les portefeuilles du client selectionné
        NumeroClient.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            /**
             * 
             */
            list_portefeuilles = generalMethods.find("wallet/client/"+newValue+"/provider/"+currentprovider.getString("identifiant"));

            portefeuille.getItems().clear();

            if (list_portefeuilles.length() <= 0){
                generalMethods.afficherAlert("Ce client n'a aucun portefeuille.");
            }else{

                for (int i = 0;i<list_portefeuilles.length();i++)
                {
                    portefeuille.getItems().add( list_portefeuilles.getJSONObject(i).getString("name"));
                }
            }
        }); 


        portefeuille.getSelectionModel().selectedIndexProperty().addListener((options, oldValue, newValue) -> {
            if( (int)newValue > -1){
                JSONArray pointFournitures = list_portefeuilles.getJSONObject( (int)newValue ).optJSONArray("pointFournitures");
                if(pointFournitures == null || pointFournitures.length() <= 0){
                    generalMethods.afficherAlert("Aucun point de fourniture associé à ce portefeuille.");
                }else{
                    combEAN.getItems().clear();
                    for(int i=0; i<pointFournitures.length();i++){
                        combEAN.getItems().add( pointFournitures.getJSONObject(i).optString("ean_18"));
                    }
                }
            }
        });

        combEAN.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if( newValue!=null && newValue.isBlank()){
                JSONArray supplyPointList = generalMethods.find("/supplyPoint/free/pointFourniture/ean/"+newValue);

                if(supplyPointList == null || supplyPointList.length() <= 0){
                    generalMethods.afficherAlert("Aucun compteur associé à ce point de fourniture. Vous pouvez en créer un.");
                }else{
                    compteur.getItems().clear();
                    for(int i=0; i<supplyPointList.length();i++){
                        compteur.getItems().add( supplyPointList.getJSONObject(i).optString("ean_18"));
                    }
                }
            }else{

            }
        });


        NumeroClient.valueProperty().addListener((ObservableValue<? extends  String>observable,String oldvalue,String newValue)->{
            initTable();

        });

        generalMethods.redefineDatePickerDateFormat(date_debut);
        generalMethods.redefineDatePickerDateFormat(date_fin);
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
            generalMethods.afficherAlert("Remplissez tous les champs.");
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
                generalMethods.afficherAlert("La date de fin doit venir après celle de début.");
            }
        }
    }
   
    @FXML
    void creerCompteur(ActionEvent event){
        if( newEnergy.getValue()==null || newCompteurName.getText() == "" ){
            generalMethods.afficherAlert("Remplissez tous les champs.");
        }else{

            if( ! generalMethods.checkEanValue(newCompteurName.getText())){
                generalMethods.afficherAlert("L'ean doit être composé de 18 chiffres");
                return;
            }

            JSONObject compteur = new JSONObject();
            JSONObject pointFourniture = new JSONObject();

            compteur.put("ean_18",newCompteurName.getText());
            compteur.put("energy",newEnergy.getValue());
            compteur.put("name", newCompteurName.getText() + '_' + currentprovider.getString("company_name"));

            pointFourniture.put("ean_18",newCompteurName.getText());
            pointFourniture.put("energy",newEnergy.getValue());
            pointFourniture.put("name", newCompteurName.getText() + '_' + currentprovider.getString("company_name"));
            pointFourniture.put("provider",currentprovider);

            compteur.put("pointFourniture", pointFourniture);

            try{
                generalMethods.createObject(compteur,"/supplyPoint");
                
                combEAN.getItems().add(newCompteurName.getText());

                generalMethods.afficherAlert("La creation du compteur terminee.");

                newCompteurName.setText("");
                newEnergy.setValue("");
                budget.setText("");
                budgetType.setText("");
            }catch (Exception e) {
                generalMethods.afficherAlert("La creation du compteur a echoué.");
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
