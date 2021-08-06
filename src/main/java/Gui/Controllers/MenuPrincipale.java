package Gui.Controllers;


import Gui.ModelTabs.MenuPrincipalTable;
import Gui.FacilitatorProviderLinkClient;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;

import static Gui.Controllers.NouveauContrat.*;
import static Gui.FacilitatorProviderLinkClient.*;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class MenuPrincipale  {
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
        JSONObject result = findUnique("supplyPoint/ean_18/"+textEAN.getText());
        JSONObject contract = findUnique("contract/ean/"+result.getInt("id"));
        JSONArray consommations = find("supplyPoint");
        //JSONObject user = findUnique("user/identifiant/"+contract.getString("client"));

        resultEAN.setText(result.getString("ean_18"));
        resultConsommateur.setText(contract.getString("client"));
        ArrayList<JSONObject> consommationValues = extractConsommations(consommations,result.getLong("id"));
        if (consommationValues.size()>1)
        {
            JSONObject derniereConsommation = consommationValues.get(consommationValues.size()-1);
            resultConsommation.setText(derniereConsommation.getString("value"));
        }



    }

    // Add a public no-args constructor
    public MenuPrincipale()
    {
    }

    @FXML
    public void initialize()
    {
        type_compteur.getItems().addAll("Electricite","Eau","Gaz");
        compteur.getItems().removeAll(compteur.getItems());
        compteur.getItems().addAll("Non Alloue","Alloue");
        JSONArray clients = find("user");
        for (Object client : clients){
            if (client instanceof  JSONObject){
                combClient.getItems().add(((JSONObject) client).getString("identifiant"));
            }
        }
        JSONArray array = find("supplyPoint");
        for (Object obj : array) {
            if (obj instanceof  JSONObject){
                compteur_importer.getItems().add(((JSONObject)obj).getString("ean_18"));
            }
        }
        combClient.valueProperty().addListener((ObservableValue<? extends  String> observable, String oldvalue, String newValue)->{
           System.out.println("client modifie");
            if (combClient.getValue()!=null)
            {
                System.out.println("Execution de la fonction inittable");
                initTable();
                compteur_importer.getItems().removeAll(compteur_importer.getItems());
                for (MenuPrincipalTable elt : principalList) {
                    compteur_importer.getItems().add(elt.getEan_18());
                }
            }

        });
        combPortefeuille.valueProperty().addListener((ObservableValue<? extends  String> observable, String oldvalue, String newValue)->{
            if (combPortefeuille.getValue()!=null)
            {
                System.out.println("Execution de la fonction de filtre combPortefeuille");
                ArrayList<MenuPrincipalTable>  listeFiltree = new ArrayList<MenuPrincipalTable>();
                listeFiltree.addAll(principalList);
                table.getItems().removeAll(table.getItems());
                currentList.removeAll(currentList);
                for (MenuPrincipalTable elt : listeFiltree){
                    if (elt.getNameWallet().equals(combPortefeuille.getValue())){
                        table.getItems().add(elt);
                        currentList.add(elt);
                    }
                }
            }

        });
        compteur.valueProperty().addListener(
                (ObservableValue<? extends  String> observable, String oldvalue, String newValue)->{
                    table.getItems().removeAll(table.getItems());
                    JSONArray contract_supply =find("contractSupplyPoint/byProvider/"+currentprovider.getInt("id"));
                    for (Object elt : contract_supply){
                        if (elt instanceof  JSONObject){

                        }
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
        JSONObject client  = findUnique("user/identifiant/"+combClient.getValue());
        //System.out.println("currentprovider 2 : "+currentprovider.getInt("id"));
        JSONArray contract_supply =find("contractSupplyPoint/byProvider/"+currentprovider.getInt("id"));
        combPortefeuille.getItems().removeAll(combPortefeuille.getItems());
        table.getItems().removeAll(table.getItems());
        principalList.removeAll(principalList);
        currentList.removeAll(currentList);
        for (int i =0;i<contract_supply.length();i++){
            JSONObject current = contract_supply.getJSONObject(i);
            if (current.get("contract") instanceof JSONObject){
                if (((JSONObject) current.get("contract")).getString("client").equals(client.getString("identifiant"))){
                    table.getItems().add(new MenuPrincipalTable(contract_supply.getJSONObject(i)));
                    if (Objects.isNull(contract_supply.getJSONObject(i).get("wallet"))){
                        JSONObject wallet = contract_supply.getJSONObject(i).getJSONObject("wallet");
                        combPortefeuille.getItems().add(wallet.getString("name"));
                    }
                    principalList.add(new MenuPrincipalTable(contract_supply.getJSONObject(i)));
                    currentList.add(new MenuPrincipalTable(contract_supply.getJSONObject(i)));
                }
            }
        }
            System.out.println("remplissage termine");
    }

    @FXML
    public void goToModifierDonnee(){
        current_supply_point = findUnique("supplyPoint/ean_18/"+compteur_importer.getValue());
        FacilitatorProviderLinkClient.stage.close();
        FacilitatorProviderLinkClient.showPages("ModificationDonnees.fxml");
    }
    @FXML
    public void goToSupprimerDonnee(){
        //current_supply_point = findUnique("supplyPoint/ean_18/"+compteur_importer.getValue());
//        System.out.println("current supply point to supprimer : " +current_supply_point.toString());
        FacilitatorProviderLinkClient.stage.close();
        FacilitatorProviderLinkClient.showPages("SupprimerDonnes.fxml");
    }
    @FXML
    public void goToImporter(){
       if (type_compteur.getValue()!=null ){
        FileChooser js = new FileChooser();
        js.setTitle("Export to a csv file");

           File result = js.showOpenDialog(null);
        if (result!=null)
        {
            importerFileCSV(result,type_compteur.getValue());
        }
       }else {
           JOptionPane.showMessageDialog(null,"Veuillez choisir le type de compteur dont \n les donnees seront importees");
       }


    }
    @FXML
    public void goToExporter(){
        save();
        //PortfolioManagementClient.stage.close();
        //PortfolioManagementClient.showPages("Exportation.fxml");
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

    public void save()
    {
        FileChooser js = new FileChooser();
        js.setTitle("Export to a csv file");
//        js.setSelectedExtensionFilter(new FileChooser.ExtensionFilter(".sim"));
        File result = js.showSaveDialog(null);
        if (result!=null)
        {
            FacilitatorProviderLinkClient.eportToCSV(result,currentList);
        }
    }
    public static void importerFileCSV(File file,String typeCompteur){
        //JSONObject compteur = findUnique("supplyPoint/ean_18/"+compteur_importer.getValue());
        try {
            JSONObject result = new JSONObject() ;
            Scanner sc = new Scanner(file);
            sc.useDelimiter("\n");
            int i = 0 ;
            int j = 0;
            while (sc.hasNext())
            {

                if (i > 0) {
                    String ligne = sc.next();
                    System.out.println(ligne);
                    String[] elts = ligne.split(";");



                    if (i==1){
                        JSONObject compteur = new JSONObject();
                        compteur.put("ean_18",elts[0]);
                        compteur.put("energy",typeCompteur);
                         result = createObject(compteur,"supplyPoint");
                        i+=1;
                    }
                    String newElement = elts[2].substring(0,5);
                    enregistrer (result,elts[1],Integer.parseInt(newElement));

                }
                else {
                    i++;
                    String ligne = sc.next();
                }

            }
            sc.close();
            JOptionPane.showMessageDialog(null,"Importation terminee");
        }catch (Exception e ) {
            e.printStackTrace();
        }

    }
    public static void enregistrer(JSONObject result,String date,long index){

        JSONObject consommationValue = new JSONObject();


        consommationValue.put("value",index);
        try{
            consommationValue.put("date", Date.valueOf(date));
        }catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(" result : " + result.toString());
        consommationValue.put("supplyPoint",result);
        JSONObject result2 = createObject(consommationValue,"consommationValue");

        if (!result2.isEmpty()){
            System.out.println(result2);
        }
    }

}
