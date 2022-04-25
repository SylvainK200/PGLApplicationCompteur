package Gui.Controllers.ApplicationClient;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import Gui.FacilitatorProviderLinkClient;
import Gui.Controllers.Methods.GeneralMethods;
import Gui.Controllers.Methods.GeneralMethodsImpl;
import Gui.ModelTabs.MenuPrincipalConsommateurTable;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

/**
 * Interface principale d'un consommateur.
 */
public class MenuPrincipaleConsommateur{

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
    private ComboBox<String> ean_18;

    @FXML
    private Label consommation_recente;

    @FXML
    private Label utilisateur;
    @FXML
    private ComboBox<String> etat_compteur;

    @FXML
    private Label nom;
    @FXML
    private TableView<MenuPrincipalConsommateurTable> table;

    @FXML
    private TableColumn<MenuPrincipalConsommateurTable, String> ean;

    @FXML
    private TableColumn<MenuPrincipalConsommateurTable, String> type_energie;

    @FXML
    private TableColumn <MenuPrincipalConsommateurTable, String> type_compteur;

    @FXML
    private TableColumn <MenuPrincipalConsommateurTable, String> consommation;

    @FXML
    private TableColumn<MenuPrincipalConsommateurTable, String> fournisseur;

    @FXML
    private TableColumn<MenuPrincipalConsommateurTable, String> date_affectation;

    @FXML
    private TableColumn<MenuPrincipalConsommateurTable, String> date_cloture;

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
    private TableColumn<MenuPrincipalConsommateurTable, String> cloture;

    @FXML
    private ComboBox<String> ean_exporter;

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

    private ArrayList<MenuPrincipalConsommateurTable> listPrincipal = new ArrayList<>();
    private ObservableList<MenuPrincipalConsommateurTable> researchList = FXCollections.observableArrayList();
    private FilteredList<MenuPrincipalConsommateurTable> filteredList;
    private JSONObject currentSupplyPoint;
    
    GeneralMethods generalMethods = new GeneralMethodsImpl();

    /**
     * Initialise tous les champs.
     */
    public void initialize(){
        etat_compteur.getItems().addAll("cloture","ouvert");
        utilisateur.setText(FacilitatorProviderLinkClient.currentClient.getString("identifiant"));
        nom.setText(FacilitatorProviderLinkClient.currentClient.getString("name"));
        JSONArray walets = generalMethods.find ("wallet/user/"+ FacilitatorProviderLinkClient.currentClient.getLong("id"));

        for (Object wal : walets){
            if (wal instanceof JSONObject){
                wallets.getItems().add(((JSONObject)wal).getString("name"));
            }
        }
        table.getItems().clear();
        initTable();
        initConsommation();

        generalMethods.redefineDatePickerDateFormat(date_lecture);
        generalMethods.redefineDatePickerDateFormat(date_maximale);    
        generalMethods.redefineDatePickerDateFormat(date_debut_importation); 
    }
    
    /**
     * Met à jour tous les champs liés à la consommation
     */
    void initConsommation(){
        ean_18.getItems().clear();
        ean_exporter.getItems().clear();
        JSONArray supply_points = generalMethods.find("supplyPoint/client/identifiant/"+FacilitatorProviderLinkClient.currentClient.getString("identifiant"));
        
        for(int i = 0 ; i<supply_points.length();i++){
            ean_18.getItems().add(supply_points.getJSONObject(i).getString("name"));
            ean_exporter.getItems().add(supply_points.getJSONObject(i).getString("name"));
        }
        
        ean_18.valueProperty().addListener((ObservableValue<?extends String>observable,String oldValue,String newValue)->{
            if(newValue!=null && !newValue.isBlank()){
                JSONObject currentSupp = generalMethods.findUnique("supplyPoint/name/"+newValue);
                currentSupplyPoint = currentSupp;
                JSONArray historique = generalMethods.find("historicalValue/historiqueRecent/"+currentSupp.getLong("id"));
                if (historique.length()>0){
                    int consommationTotal = 0;
                    for(int i=0; i< historique.length();i++){
                        consommationTotal += historique.getJSONObject(i).getDouble("consommation");
                    }
                    consommation_recente.setText(consommationTotal+"");
                    date_recente.setText("Jusqu'a aujourd'hui");
                }else {
                    consommation_recente.setText("0");
                    date_recente.setText("Jusqu'a aujourd'hui");
                }
            }
        });
    }
    
    /**
     * Charge la liste des contracts qui lie un portefeuille de l'utilisateur.
     * Configure les listeners pour le filtrage sur le tableau.
     */
    void initTable(){
        cloture.setCellValueFactory(new PropertyValueFactory<MenuPrincipalConsommateurTable,String>("cloture"));
        ean.setCellValueFactory(new PropertyValueFactory<MenuPrincipalConsommateurTable,String>("name"));
        type_energie.setCellValueFactory(new PropertyValueFactory<MenuPrincipalConsommateurTable,String>("type_energie"));
        type_compteur.setCellValueFactory(new PropertyValueFactory<MenuPrincipalConsommateurTable,String>("type_compteur"));
        date_affectation.setCellValueFactory(new PropertyValueFactory<MenuPrincipalConsommateurTable,String>("date_affectation"));
        date_cloture.setCellValueFactory(new PropertyValueFactory<MenuPrincipalConsommateurTable,String>("date_cloture"));
        fournisseur.setCellValueFactory(new PropertyValueFactory<MenuPrincipalConsommateurTable,String>("fournisseur"));
        consommation.setCellValueFactory(new PropertyValueFactory<MenuPrincipalConsommateurTable,String>("consommation"));

        generalMethods.log(this.getClass().getName(),FacilitatorProviderLinkClient.currentClient.getString("identifiant"));
        
        JSONArray contract_supply_points = generalMethods.find("contractSupplyPoint/wallet/identifiant/"+FacilitatorProviderLinkClient.currentClient.getString("identifiant"));
        
        researchList.clear();
        table.getItems().clear();
        listPrincipal.clear();

        for (int i = 0;i<contract_supply_points.length();i++){
            JSONObject contract_supply_point = contract_supply_points.getJSONObject(i);
            table.getItems().add(new MenuPrincipalConsommateurTable(contract_supply_point));
            listPrincipal.add(new MenuPrincipalConsommateurTable(contract_supply_point));
            researchList.add(new MenuPrincipalConsommateurTable(contract_supply_point));
        }
        filteredList = new FilteredList<MenuPrincipalConsommateurTable>(researchList,p->true);
        rechercher.textProperty().addListener((ObservableValue<? extends  String> observable,String oldValue,String newValue)->{
            filteredList.setPredicate(
                    contrat->{
                        if (newValue == null||newValue.isEmpty()){
                            return true;
                        }
                        if (contrat.getName().toLowerCase().contains(newValue.toLowerCase()) ||
                                contrat.getFournisseur().toLowerCase().contains(newValue.toLowerCase())||
                                contrat.getType_energie().toLowerCase().contains(newValue.toLowerCase())

                        ){
                            return true;
                        }
                        return false;
                    }
            );
            table.getItems().clear();
            table.getItems().addAll(filteredList);
        });

        etat_compteur.valueProperty().addListener((ObservableValue<? extends  String> observable,String oldValue,String newValue)->{
            filteredList.setPredicate(
                    contrat->{
                        if (newValue=="ouvert" && contrat.getCloture().contains("non")){
                            return true;
                        }
                        if (newValue=="cloture" && !contrat.getCloture().contains("non")) {
                            return true;
                        }
                        else
                        {
                            return false;
                        }
                    }
            );
            table.getItems().clear();
            table.getItems().addAll(filteredList);

        });
    }

    /**
     * Ajout d'une nouvelle consommation.
     * Notons que l'utilisateur doit obligatoirement remplir tous les champs.
     * La date de lecture doit être inferieur ou éagle à celle du jour courant.
     * @param event
     */
    @FXML
    public void registerConsommation(ActionEvent event){
        LocalDate date_lect = date_lecture.getValue();
        String newValue = valeur_vue.getText();
        if (currentSupplyPoint == null ) {
            generalMethods.afficherAlert("Veuillez choisir le compteur dans la partie consommation.");
        }else if (newValue == null || newValue.isEmpty() || newValue.isBlank()){
            generalMethods.afficherAlert("Veuillez spécifier la valeur lue.");
        }
        else if (date_lect.isAfter(LocalDate.now())){
            generalMethods.afficherAlert("La date de lecture est après la date du jour. Modifiez la date de lecture et réessayez.");
        }else{
            JSONObject newConsommation = new JSONObject();
            newConsommation.put("consommation",newValue);
            newConsommation.put("date",date_lect);
            newConsommation.put("supplyPoint", new JSONObject(currentSupplyPoint,"id"));

            JSONObject created = generalMethods.createObject(newConsommation,"historicalValue");
            if (created.isEmpty()){
                generalMethods.afficherAlert("Echec lors de l'ajout de la nouvelle consommation.");
            }else {                
                generalMethods.afficherAlert("Enregistrement reussi de la nouvelle consommation.");
                // Update table
                initTable();
                //initConsommation();
                ean_18.setValue(ean_18.getValue());
            }
        }

    }

    /**
     * Reinitialise les champs à remplir pour exporter les données du l'utilisateur.
     * @param event
     */
    
    @FXML
    void annuler_button(ActionEvent event) {
        date_debut_importation.setValue(null);
        date_maximale.setValue(null);
        ean_exporter.setValue(null);
    }

    /**
     * Exportation des données du client. 
     * L'utilisateur doit OBLIGATOIREMENT specifier la date minimale d'exportation.
     * Par contre, il peut ne pas specifier la date maximale. Dans ce cas, on utilise la date du jour courant.
     * @param event
     */
    @FXML
    void exporterDonnee(ActionEvent event) {
        String ean = ean_exporter.getValue();

        if(ean==null || ean.isEmpty() || ean.isBlank()){
            generalMethods.afficherAlert("Veuillez selectionner le ean à exporter.");
            return;
        }

        LocalDate d_debut = date_debut_importation.getValue();
        LocalDate d_fin = date_maximale.getValue();

        if(d_debut == null){
            generalMethods.afficherAlert("Veuillez selectionner la date minimale d'exportation");
            return;
        }

        if(d_fin == null){
            d_fin = LocalDate.now();
            generalMethods.afficherAlert("La date maximale n'est pas specifiée. Nous utiliserons la date d'aujourd'hui. Soit : " + d_fin.toString());
        }

        if( d_debut.isAfter(d_fin)){
            generalMethods.afficherAlert("La date de debut doit être avant celle de fin.");
            return;
        }

        String date_deb = d_debut.toString();
        String date_fin = d_fin.toString();

        JSONObject supply = generalMethods.findUnique("supplyPoint/name/"+ean);
        JSONArray consommations = generalMethods.find("historicalValue/consommations/"+supply.getLong("id")+
                "/"+date_deb+"/"+date_fin);
        final String DELIMITER = ";";
        final String SEPARATOR = "\n";
        final String HEADER = "EAN Compteur;Nom Compteur;Type energie;Date lecture;consommation;fournisseur;";
        final String FOURNISSEUR = supply.getJSONObject("pointFourniture").getJSONObject("provider").getString("identifiant");
        FileChooser js = generalMethods.getFileChooser();
        
        js.setTitle("Export to");
        File file = js.showSaveDialog(null);
        if (file!=null)
        {
            try{
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.append(HEADER);
                fileWriter.append(SEPARATOR);

                for (int i = 0 ; i<consommations.length();i++){
                    JSONObject elt  = consommations.getJSONObject(i);
                    fileWriter.append(elt.getJSONObject("supplyPoint").getString("ean_18"));
                    fileWriter.append(DELIMITER);
                    fileWriter.append(elt.getJSONObject("supplyPoint").getString("name"));
                    fileWriter.append(DELIMITER);
                    fileWriter.append(""+elt.getJSONObject("supplyPoint").getString("energy"));
                    fileWriter.append(DELIMITER);
                    fileWriter.append(""+elt.getString("date").split("T")[0]);
                    fileWriter.append(DELIMITER);
                    fileWriter.append("" + elt.getLong("consommation"));
                    fileWriter.append(DELIMITER);
                    fileWriter.append(""+FOURNISSEUR);
                    fileWriter.append(SEPARATOR);

                }
                fileWriter.close();
                generalMethods.afficherAlert("Exportation effectuée avec succès dans le fichier : " + file.getAbsolutePath());
            }catch (Exception e ){
                e.printStackTrace();
                generalMethods.afficherAlert("Exportation a échoué. Regardez les logs.");
            }
        }
    }

    /**
     * Deconnecte l'utilisateur et charge l'interface de login.
     * @param event
     */
    @FXML
    void deconnecter(ActionEvent event) {
        FacilitatorProviderLinkClient.stage.close();
        FacilitatorProviderLinkClient.showPages("login.fxml");
    }

    /**
     * Charge l'interface de la liste des contrats du consommateur.
     * @param event
     */
    @FXML
    void goToContrats(ActionEvent event) {
        FacilitatorProviderLinkClient.stage.close();
        FacilitatorProviderLinkClient.showPages("client/AffichierContratsConsommateur.fxml");
    }

    /**
     * Charge l'interface de gestion de l'historique des consommations.
     * @param event
     */
    @FXML
    void goToHistorique(ActionEvent event) {
        FacilitatorProviderLinkClient.stage.close();
        FacilitatorProviderLinkClient.showPages("client/HistoriqueCompteur.fxml");
    }

}
