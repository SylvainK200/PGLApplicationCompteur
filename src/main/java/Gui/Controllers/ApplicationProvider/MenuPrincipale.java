package Gui.Controllers.ApplicationProvider;


import Gui.Controllers.Methods.GeneralMethods;
import Gui.Controllers.Methods.GeneralMethodsImpl;
import Gui.Controllers.Methods.ImportationExportation;
import Gui.Controllers.Methods.ImportationExportationImpl;
import Gui.ModelTabs.MenuPrincipalTable;
import Gui.FacilitatorProviderLinkClient;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static Gui.FacilitatorProviderLinkClient.*;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Interface principale d'un fournisseur.
 */
public class MenuPrincipale  implements Initializable{
    ImportationExportation importationExportation = new ImportationExportationImpl();
    GeneralMethods generalMethods  = new GeneralMethodsImpl();
    @FXML
    private MenuItem modifier_donnee;
    @FXML
    private MenuItem supprimer_donnee;
    @FXML
    private MenuItem importer;
    @FXML
    private MenuItem exporter;
    @FXML
    private MenuItem afficher_contrat;
    @FXML
    private MenuItem etablir_contrat;
    @FXML
    private MenuItem se_deconnecter;

    @FXML
    private Label lblEAN;

    @FXML
    private TextField textEAN;

    @FXML
    private Button Buttonrecherche;

    @FXML
    private Label labelEAN;

    @FXML
    private TextField resultEAN = new TextField();

    @FXML
    private Label lblConsommation;

    @FXML
    private TextField resultConsommation= new TextField();

    @FXML
    private Label lblConsommateur;

    @FXML
    private TextField resultConsommateur  = new TextField();

    @FXML
    private Label lblClient;

    @FXML
    private ComboBox<String> combClient;

    @FXML
    private Label lblPortefeuille;

    @FXML
    private ComboBox<String> combPortefeuille;

    @FXML
    private Label lblCompteur;

    @FXML
    private ComboBox<String> compteur;

    @FXML
    private TableView<MenuPrincipalTable> table;

    @FXML
    private TableColumn<MenuPrincipalTable, String> colEAN;
    @FXML
    private ComboBox<String> type_compteur;

    @FXML
    private TableColumn<MenuPrincipalTable, Double> colConsommation;
    @FXML
    private TableColumn<MenuPrincipalTable, Double> colCout;

    @FXML
    private TableColumn<MenuPrincipalTable, String> colCompteur;

    @FXML
    private TableColumn<MenuPrincipalTable, String> colDateAffectation;
    @FXML
    private ComboBox<String> compteur_importer;
    @FXML
    private TableColumn<MenuPrincipalTable, String> colDateClotur;
    private ArrayList<MenuPrincipalTable> principalList = new ArrayList<>();
    private ArrayList<MenuPrincipalTable> currentList = new ArrayList<>();


    public void onclickrechercher(){
        if(!textEAN.getText().isBlank()){
            JSONArray tmp = generalMethods.find("supplyPoint/ean_18/"+textEAN.getText());
            if(tmp.length() == 0){
                generalMethods.afficherAlert("Aucun element trouv??.");
                return;
            }
            
            JSONObject result = tmp.getJSONObject(0);

            try{
                JSONObject contract = generalMethods.findUnique("contract/ean/"+result.getInt("id"));
                resultConsommateur.setText(contract.getString("client"));
            }catch(Exception e){}

            JSONArray consommations = generalMethods.find("historicalValue");

            resultEAN.setText(result.getString("ean_18"));

            ArrayList<JSONObject> consommationValues = extractConsommations(consommations,result.getLong("id"));

            double consommation = 0;

            for (int i=0; i< consommationValues.size(); i++){
                consommation += consommationValues.get(i).getDouble("consommation");
            }

            resultConsommation.setText(consommation+"");
        }else{
            generalMethods.afficherAlert("Specifiez la valeur de l'EAN");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initialize();
    }
    
    @FXML
    public void initialize()
    {
        type_compteur.getItems().addAll("Electricite","Eau","Gaz");
        compteur.getItems().removeAll(compteur.getItems());
        compteur.getItems().addAll("Non Alloue","Alloue");

        JSONArray clients = generalMethods.find("user");

        for (Object client : clients){
            if (client instanceof  JSONObject){
                combClient.getItems().add(((JSONObject) client).getString("identifiant"));
            }
        }

        JSONArray array = generalMethods.find("supplyPoint");

        for (Object obj : array) {
            if (obj instanceof  JSONObject){
                compteur_importer.getItems().add(((JSONObject)obj).getString("ean_18"));
            }
        }

        // Charger les portefeuilles du client selectionn??
        combClient.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            JSONArray list_portefeuilles = generalMethods.find("wallet/identifiant/"+newValue);

            combPortefeuille.getItems().clear();
            combPortefeuille.getItems().add("");

            for (int i = 0;i<list_portefeuilles.length();i++)
            {
                combPortefeuille.getItems().add( list_portefeuilles.getJSONObject(i).getString("name"));

            }
        }); 

        combClient.valueProperty().addListener((ObservableValue<? extends  String> observable, String oldvalue, String newValue)->{
            
            //generalMethods.log(this.getClass().getName(), "Client modifi??");
            if (newValue!=null)
            {
                initTable();
                compteur_importer.getItems().clear();
                for (MenuPrincipalTable elt : principalList) {
                    compteur_importer.getItems().add(elt.getEan_18());
                }
            }

        });

        combPortefeuille.valueProperty().addListener((ObservableValue<? extends  String> observable, String oldvalue, String newValue)->{
            if (newValue!=null && newValue!="")
            {
                //generalMethods.log(this.getClass().getName(), "Execution de la fonction de filtre combPortefeuille");

                table.getItems().clear();
                currentList.clear();

                for (MenuPrincipalTable elt : principalList){
                    if (elt.getNameWallet().equalsIgnoreCase(combPortefeuille.getValue())){
                        table.getItems().add(elt);
                        currentList.add(elt);
                    }
                }

            }else{
                table.getItems().clear();
                table.getItems().addAll(principalList);
            }

        });
    }

    void initTable(){
        colCompteur.setCellValueFactory(new PropertyValueFactory<MenuPrincipalTable,String>("type_compteur"));
        colEAN.setCellValueFactory(new PropertyValueFactory<MenuPrincipalTable,String>("ean_18"));
        colCout.setCellValueFactory(new PropertyValueFactory<MenuPrincipalTable,Double>("cout"));
        colConsommation.setCellValueFactory(new PropertyValueFactory<MenuPrincipalTable,Double>("consommation"));
        colDateAffectation.setCellValueFactory(new PropertyValueFactory<MenuPrincipalTable,String>("date_affectation"));
        colDateClotur.setCellValueFactory(new PropertyValueFactory<MenuPrincipalTable,String>("date_cloture"));
        //JSONObject client  = generalMethods.findUnique("user/identifiant/"+combClient.getValue());
        
        JSONArray contract_supply =generalMethods.find("contractSupplyPoint/byProvider/"+currentprovider.getInt("id")+"/byClient/"+combClient.getValue());
        //combPortefeuille.getItems().removeAll(combPortefeuille.getItems());
        
        table.getItems().clear();
        principalList.clear();
        currentList.clear();
        if (contract_supply.length() > 0){
            for (int i =0;i<contract_supply.length();i++){
                JSONObject current = contract_supply.getJSONObject(i);

                table.getItems().add(new MenuPrincipalTable(current));
                try{
                    if (Objects.isNull(current.get("wallet"))){
                        JSONObject wallet = current.getJSONObject("wallet");
                        combPortefeuille.getItems().add(wallet.getString("name"));
                    }
                }catch (JSONException e ){
                    
                }
                
                principalList.add(new MenuPrincipalTable(current));
                currentList.add(new MenuPrincipalTable(current));
            }
        }
        else{
            generalMethods.afficherAlert("Ce client n'a pas de contrats.");
        }
    }

    @FXML
    public void goToModifierDonnee(){
        if(compteur_importer.getValue() == null){
            generalMethods.afficherAlert("Veuillez choisir le compteur ?? importer");
            return;
        }
        current_supply_point = generalMethods.find("supplyPoint/ean_18/"+compteur_importer.getValue()).getJSONObject(0);
        FacilitatorProviderLinkClient.stage.close();
        FacilitatorProviderLinkClient.showPages("ModificationDonnees.fxml");
    }

    @FXML
    void goToPointFourniture() {
        FacilitatorProviderLinkClient.stage.close();
        FacilitatorProviderLinkClient.showPages("PointFourniture.fxml");
    }

    @FXML
    public void goToSupprimerDonnee(){
        FacilitatorProviderLinkClient.stage.close();
        FacilitatorProviderLinkClient.showPages("SupprimerDonnes.fxml");
    }

    @FXML
    public void goToImporter(){
       if (type_compteur.getValue()!=null ){
           
        FileChooser js = getFileChooser();

        js.setTitle("Selectionner le fichier ?? importer");

        File result = js.showOpenDialog(null);
        if (result!=null)
        {
            importationExportation.importerFileCSV(result,type_compteur.getValue());
        }
       }else {
            generalMethods.afficherAlert("Veuillez choisir le type de compteur dont les donnees seront importees");
       }


    }

    @FXML
    public void goToExporter(){
        save();
    }

    @FXML
    public void goToAfficherContrat() {
        FacilitatorProviderLinkClient.stage.close();
        FacilitatorProviderLinkClient.showPages("AffichierContrats.fxml");
    }

    @FXML
    public void goToEtablirContrat(){
        FacilitatorProviderLinkClient.stage.close();
        FacilitatorProviderLinkClient.showPages("NouveauContrat.fxml");
    }
    
    @FXML
    public void seDeconnecter(){
        FacilitatorProviderLinkClient.stage.close();
        FacilitatorProviderLinkClient.showPages("login.fxml");
    }

    public void save() {
        FileChooser js = getFileChooser();
        js.setTitle("Selectionner le fichier o?? exporter");
        File result = js.showSaveDialog(null);
        if (result!=null)
        {
            importationExportation.exportToCSV(result,currentList);
        }
    }

    public FileChooser getFileChooser(){

        FileChooser js = new FileChooser();

        js.getExtensionFilters().add(new ExtensionFilter("Supported Files (*.csv|*.json|*.yml|*.Yaml)", "*.csv","*.csv","*.json", "*.yml","*.yaml"));

        js.getExtensionFilters().add(new ExtensionFilter("CSV Files (*.csv)", "*.csv"));

        js.getExtensionFilters().add(new ExtensionFilter("JSON Files (*.json)", "*.json"));

        js.getExtensionFilters().add(new ExtensionFilter("YAML Files (*.yml | *.yaml)", "*.yml","*.yaml"));

        return js;
    }

}
