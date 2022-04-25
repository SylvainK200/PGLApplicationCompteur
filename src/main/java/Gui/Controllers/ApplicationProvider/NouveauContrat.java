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

/**
 * Gestion de la creation de nouveaux contrats.
 */
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

    /**
     * Recupere tous les informations liées à un consommateur ayant cette identifiant.
     * @param identifiant
     * @return
     */
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
            generalMethods.log(this.getClass().getName(), "Response /user/identifiant/"+identifiant+ " : " + res);

            result= new JSONObject(res);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Recupere la liste de tous les consommateurs.
     * @return
     */
    public JSONArray findUsers(){
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

            generalMethods.log(this.getClass().getName(), "Response /user/ : " + res);

            result= new JSONArray(res);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    JSONArray list_portefeuilles;

    /**
     * Initialise les champs de l'interface et configure les listerners pour le filtrage.
     */
    public void initialize(){
        meter_type.getItems().addAll(MeterType.MONOHORARY.toString(),
                MeterType.BIHORARY.toString(),MeterType.EXCLUSIVENIGHT.toString());
        newEnergy.getItems().addAll("Electricite","Eau","Gaz");
        
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
            if( newValue!=null && !newValue.isBlank()){
                updateCompteurComboBox(newValue);
            }else{

            }
        });


        NumeroClient.valueProperty().addListener((ObservableValue<? extends  String>observable,String oldvalue,String newValue)->{
            initTable();

        });

        generalMethods.redefineDatePickerDateFormat(date_debut);
        generalMethods.redefineDatePickerDateFormat(date_fin);

        //initTable();
    }

    /**
     * Charge la liste des compteurs liés au nouveau point de fourniture et met à jour le combobox de selection.
     * @param newValue
     */
    void updateCompteurComboBox(String newValue){
        JSONArray supplyPointList = generalMethods.find("supplyPoint/free/pointFourniture/ean/"+newValue);

        if(supplyPointList == null || supplyPointList.length() <= 0){
            generalMethods.afficherAlert("Aucun compteur associé à ce point de fourniture. Vous pouvez en créer un.");
        }else{
            compteur.getItems().clear();
            for(int i=0; i<supplyPointList.length();i++){
                compteur.getItems().add( supplyPointList.getJSONObject(i).optString("name"));
            }
        }
    }

    /**
     * Charge la liste des contrats liés à ce fournisseur.
     */
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
            JSONObject current = contract_supply.getJSONObject(i);
            if ( current.getString("client").equalsIgnoreCase(client.getString("identifiant")) ){
                table.getItems().add(new NewContractTable(client,current));
            }
        }
    }

    /**
     * Creation d'un nouveau contrat.
     * Tous les champs doivent être remplis.
     * @param event
     */
    @FXML
    void confirmerAjout(ActionEvent event) {

        if(date_debut.getValue()==null || date_fin.getValue()==null || compteur.getValue() ==null || meter_type.getValue()==null || 
           meter_rate.getText().isEmpty() || network_manager_cost.getText().isEmpty() || over_tax_rate.getText().isEmpty() || 
           tax_rate.getText().isEmpty()){
            generalMethods.afficherAlert("Remplissez tous les champs et verifiez que vous avez selectionné un compteur. S'il n'en existe pas, vous pouvez en créer un à la section 'Création compteur");
        }else{
            if(date_fin.getValue().isAfter(date_debut.getValue())){
                String aleatoire = ""+ System.currentTimeMillis() + (char)( (int)(Math.random()*10) );
                JSONObject contract_supply = new JSONObject();

                try{
                    JSONObject wallet = generalMethods.findUnique("wallet/name/"+portefeuille.getValue());
                    JSONObject supplyPoint = generalMethods.findUnique("supplyPoint/name/"+compteur.getValue());

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
                    contract_supply.put("over_tax",Double.parseDouble(over_tax_rate.getText()));
                    contract_supply.put("tax_rate",Double.parseDouble(tax_rate.getText()));
                    contract_supply.put("wallet",wallet);
                    generalMethods.createObject(contract_supply,"contractSupplyPoint");
                    //table.getItems().add(new NewContractTable(client,contract_supply));
                    initTable();

                    meter_rate.clear();
                    network_manager_cost.clear();
                    over_tax_rate.clear();
                    tax_rate.clear();
                    compteur.getItems().clear();
                    
                    generalMethods.afficherAlert("Contract créé avec succès.");
                }catch(Exception e){
                    generalMethods.afficherAlert("La creation du contract a échoué. Regardez les logs.");
                }
            }else{
                generalMethods.afficherAlert("La date de fin doit venir après celle de début.");
            }
        }
    }
   
    /**
     * Creation d'un nouveau compteur.
     * Le point de fourniture doit être selectionné et tous les champs du formulaire rempli.
     * @param event
     */
    @FXML
    void creerCompteur(ActionEvent event){
        String ean = combEAN.getValue();
        if(ean == null || ean.isBlank()){
            generalMethods.afficherAlert("Selectionnez l'EAN du point de fourniture.");
            return;
        }

        if( newEnergy.getValue()==null || newCompteurName.getText() == "" || budget.getText().isBlank() || budgetType.getText().isBlank()){
            generalMethods.afficherAlert("Remplissez tous les champs.");
        }else{

            /*if( ! generalMethods.checkEanValue(newCompteurName.getText())){
                generalMethods.afficherAlert("L'ean doit être composé de 18 chiffres");
                return;
            }
            */

            JSONObject compteur = new JSONObject();
            JSONObject pointFourniture = generalMethods.findUnique("pointFourniture/ean/"+ean);

            pointFourniture = new JSONObject(pointFourniture,"id");

            compteur.put("ean_18",ean);
            compteur.put("energy",newEnergy.getValue());
            compteur.put("name", newCompteurName.getText() );
            compteur.put("pointFourniture", pointFourniture);

            try{
                generalMethods.createObject(compteur,"supplyPoint");

                generalMethods.afficherAlert("La creation du compteur terminee.");
                
                updateCompteurComboBox(ean);


                newCompteurName.clear();
                newEnergy.setValue("");
                
                budget.clear();
                budgetType.clear();

            }catch (Exception e) {
                generalMethods.afficherAlert("La creation du compteur a echoué.");
                generalMethods.log(this.getClass().getName(), "Creating new compteur failed NouveauContract.java -> creerCompteur");
            }
        }
    }

    /**
     * Ferme la page et charge l'interface principale.
     * @param event
     */
    @FXML
    void quitterpage(ActionEvent event) {
        FacilitatorProviderLinkClient.stage.close();
        FacilitatorProviderLinkClient.showPages("MenuPrincipale.fxml");
    }

}
