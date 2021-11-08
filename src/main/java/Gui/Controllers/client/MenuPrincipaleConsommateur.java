package Gui.Controllers.client;

import Gui.Controllers.Methods.GeneralMethods;
import Gui.Controllers.Methods.GeneralMethodsImpl;
import Gui.FacilitatorProviderLinkClient;
import Gui.ModelTabs.MenuPrincipalConsommateurTable;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

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
    private ObservableList researchList = FXCollections.observableArrayList();
    private FilteredList<MenuPrincipalConsommateurTable> filteredList;
    private JSONObject currentSupplyPoint;
    GeneralMethods generalMethods = new GeneralMethodsImpl();
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
    }
    
    void initConsommation(){
        JSONArray supply_points = generalMethods.find("supplyPoint/client/identifiant/"+FacilitatorProviderLinkClient.currentClient.getString("identifiant"));
        for(int i = 0 ; i<supply_points.length();i++){
            ean_18.getItems().add(supply_points.getJSONObject(i).getString("ean_18"));
        }
        ean_18.valueProperty().addListener((ObservableValue<?extends String>observable,String oldValue,String newValue)->{
            JSONObject currentSupp = generalMethods.findUnique("supplyPoint/ean_18/"+newValue);
            currentSupplyPoint = currentSupp;
            JSONArray historique = generalMethods.find("consommationValue/historiqueRecent/"+currentSupp.getLong("id"));
            if (historique.length()>0){
                JSONObject current = historique.getJSONObject(0);
                consommation_recente.setText(current.getLong("value")+"");
                date_recente.setText(current.getLong("value")+"");
            }else {
                consommation_recente.setText("0");
                date_recente.setText("Jusqu'a aujourd'hui");
            }
        });
    }
    void initTable(){
        cloture.setCellValueFactory(new PropertyValueFactory<MenuPrincipalConsommateurTable,String>("cloture"));
        ean.setCellValueFactory(new PropertyValueFactory<MenuPrincipalConsommateurTable,String>("ean_18"));
        type_energie.setCellValueFactory(new PropertyValueFactory<MenuPrincipalConsommateurTable,String>("type_energie"));
        type_compteur.setCellValueFactory(new PropertyValueFactory<MenuPrincipalConsommateurTable,String>("type_compteur"));
        date_affectation.setCellValueFactory(new PropertyValueFactory<MenuPrincipalConsommateurTable,String>("date_affectation"));
        date_cloture.setCellValueFactory(new PropertyValueFactory<MenuPrincipalConsommateurTable,String>("date_cloture"));
        fournisseur.setCellValueFactory(new PropertyValueFactory<MenuPrincipalConsommateurTable,String>("fournisseur"));
        consommation.setCellValueFactory(new PropertyValueFactory<MenuPrincipalConsommateurTable,String>("consommation"));
        System.out.println(FacilitatorProviderLinkClient.currentClient.getString("identifiant"));
        JSONArray contract_supply_points = generalMethods.find("contractSupplyPoint/client/identifiant/"+FacilitatorProviderLinkClient.currentClient.getString("identifiant"));
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
                        if (contrat.getEan_18().toLowerCase().contains(newValue.toLowerCase()) ||
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
                        if (newValue=="cloture" && contrat.getCloture().contains("non")){
                            return true;
                        }
                        if (newValue=="ouvert" && !contrat.getCloture().contains("non")) {
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
    @FXML
    public void registerConsommation(ActionEvent event){
        LocalDate date_lect = date_lecture.getValue();
        String value = valeur_vue.getText();
        if (currentSupplyPoint == null ) {
            JOptionPane.showMessageDialog(null,"S'il vous plait choisissez le compteur dans la partie consommation");
        }
        else {
            JSONObject newConsommation = new JSONObject();
            newConsommation.put("value",value);
            newConsommation.put("date",date_lect);
            newConsommation.put("supplyPoint",currentSupplyPoint);

            JSONObject created = generalMethods.createObject(newConsommation,"consommationValue");
            if (created.isEmpty()){
                JOptionPane.showMessageDialog(null,"Echec lors de l'ajout de la nouvelle consommation");
            }else {
                JOptionPane.showMessageDialog(null,"Enregistrement reussi de la nouvelle consommation");
            }
        }

    }
    @FXML
    void annuler_button(ActionEvent event) {
        date_debut_importation.setValue(null);
        date_maximale.setValue(null);
        ean_exporter.setValue(null);
    }
    @FXML
    void exporterDonnee(ActionEvent event) {
        String date_deb = date_debut_importation.getValue()+"";
        String date_fin = date_maximale.getValue()+"";
        String ean = ean_exporter.getValue();

        JSONObject supply = generalMethods.findUnique("supplyPoint/ean_18/"+ean);
        JSONArray consommations = generalMethods.find("consommationValue/consommations/"+supply.getString("id")+
                "/"+date_deb+"/"+date_fin);
        final String DELIMITER = ";";
        final String SEPARATOR = "\n";
        final String HEADER = "EAN;type energie;cout;date lecture;consommation;fournisseur;";
        final String FOURNISSEUR = generalMethods.findUnique("provider/ean"+ean).getString("company_name");
        FileChooser js = new FileChooser();
        js.setTitle("Export to a csv file");
//        js.setSelectedExtensionFilter(new FileChooser.ExtensionFilter(".sim"));
        File file = js.showSaveDialog(null);
        if (file!=null)
        {
            try{
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.append(HEADER);
            fileWriter.append(SEPARATOR);
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

            for (int i = 0 ; i<consommations.length();i++){
                JSONObject elt  = consommations.getJSONObject(i);

                fileWriter.append(elt.getJSONObject("supplyPoint").getString("ean_18"));
                fileWriter.append(DELIMITER);
                fileWriter.append(""+elt.getJSONObject("supplyPoint").getString("energy"));
                fileWriter.append(DELIMITER);
                fileWriter.append(""+df.format(elt.getLong("date")) );
                fileWriter.append(DELIMITER);
                fileWriter.append(""+FOURNISSEUR);
                fileWriter.append(SEPARATOR);

            }
            fileWriter.close();
        }catch (Exception e ){
            e.printStackTrace();
        }

    }


    }
    @FXML
    void deconnecter(ActionEvent event) {
        FacilitatorProviderLinkClient.stage.close();
        FacilitatorProviderLinkClient.showPages("login.fxml");
    }

    @FXML
    void goToContrats(ActionEvent event) {
        FacilitatorProviderLinkClient.stage.close();
        FacilitatorProviderLinkClient.showPages("client/AffichierContratsConsommateur.fxml");
    }

    @FXML
    void goToHistorique(ActionEvent event) {
        FacilitatorProviderLinkClient.stage.close();
        FacilitatorProviderLinkClient.showPages("client/HistoriqueCompteur.fxml");
    }

}
