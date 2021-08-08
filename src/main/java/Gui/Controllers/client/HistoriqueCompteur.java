package Gui.Controllers.client;

import Gui.FacilitatorProviderLinkClient;
import Gui.ModelTabs.HistoriqueTable;
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

import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static Gui.Controllers.NouveauContrat.find;
import static Gui.Controllers.NouveauContrat.findUnique;

public class HistoriqueCompteur {

    @FXML
    private ComboBox<String> combEAN;

    @FXML
    private TableView<HistoriqueTable> table;

    @FXML
    private TableColumn<HistoriqueTable, String> colEAN;

    @FXML
    private TableColumn<HistoriqueTable, String> type_energy;

    @FXML
    private TableColumn<HistoriqueTable, String> dateConsommation;

    @FXML
    private TableColumn<HistoriqueTable, String> consommation;

    @FXML
    private TableColumn<HistoriqueTable, String> fournisseur;


    @FXML
    private ComboBox<String> ean_exporter;

    @FXML
    private DatePicker date_debut_importation;

    @FXML
    private DatePicker date_maximale;


    private ObservableList<HistoriqueTable> researchList = FXCollections.observableArrayList();
    private FilteredList<HistoriqueTable> filteredList;
    public void initialize(){
        colEAN.setCellValueFactory(new PropertyValueFactory<HistoriqueTable,String>("ean"));
        type_energy.setCellValueFactory(new PropertyValueFactory<HistoriqueTable,String>("type_energy"));
        dateConsommation.setCellValueFactory(new PropertyValueFactory<HistoriqueTable,String>("date"));
        fournisseur.setCellValueFactory(new PropertyValueFactory<HistoriqueTable,String>("fournisseur"));
        consommation.setCellValueFactory(new PropertyValueFactory<HistoriqueTable,String>("fournisseur"));
        JSONArray suppliesPoint = find("supplyPoint/client/identifiant"+ FacilitatorProviderLinkClient.currentClient.getString("identifiant"));
        for(int i = 0; i<suppliesPoint.length();i++){
            JSONObject currentObject = suppliesPoint.getJSONObject(i);
            JSONArray consommationSupplyPoint = find("consommationValue/historiqueRecent/"+currentObject.getLong("id"));
            for (int j =0;j<consommationSupplyPoint.length();j++){
                JSONObject current_consommation = consommationSupplyPoint.getJSONObject(j);
                JSONObject provider = findUnique("provider/ean/"+currentObject.getString("ean_18"));

                table.getItems().add(new HistoriqueTable(current_consommation,provider.getString("company_name")));
                //listPrincipal.add(new HistoriqueTable(current_consommation,provider.getString("company_name")));
                researchList.add(new HistoriqueTable(current_consommation,provider.getString("company_name")));

            }
        }
        filteredList = new FilteredList<HistoriqueTable>(researchList,p->true);

        combEAN.valueProperty().addListener((ObservableValue<? extends  String> observable, String oldValue, String newValue)->{
            filteredList.setPredicate(
                    contrat->{
                        if (newValue == null||newValue.isEmpty()){
                            return true;
                        }
                        if (contrat.getEan().toLowerCase().contains(newValue.toLowerCase())
                        ){
                            return true;
                        }
                        return false;
                    }
            );
            table.getItems().clear();
            table.getItems().addAll(filteredList);
        });


    }
    @FXML
    void annuler_button(ActionEvent event) {
        date_debut_importation.setValue(null);
        date_maximale.setValue(null);
        ean_exporter.setValue(null);
    }

    @FXML
    void exporterDonnee(ActionEvent event) {
        String date_deb = date_debut_importation.getValue() + "";
        String date_fin = date_maximale.getValue() + "";
        String ean = ean_exporter.getValue();

        JSONObject supply = findUnique("supplyPoint/ean_18/" + ean);
        JSONArray consommations = find("consommationValue/consommations/" + supply.getString("id") +
                "/" + date_deb + "/" + date_fin);
        final String DELIMITER = ";";
        final String SEPARATOR = "\n";
        final String HEADER = "EAN;type energie;cout;date lecture;consommation;fournisseur;";
        final String FOURNISSEUR = findUnique("provider/ean" + ean).getString("company_name");
        FileChooser js = new FileChooser();
        js.setTitle("Export to a csv file");
//        js.setSelectedExtensionFilter(new FileChooser.ExtensionFilter(".sim"));
        File file = js.showSaveDialog(null);
        if (file != null) {
            try {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.append(HEADER);
                fileWriter.append(SEPARATOR);
                DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

                for (int i = 0; i < consommations.length(); i++) {
                    JSONObject elt = consommations.getJSONObject(i);

                    fileWriter.append(elt.getJSONObject("supplyPoint").getString("ean_18"));
                    fileWriter.append(DELIMITER);
                    fileWriter.append("" + elt.getJSONObject("supplyPoint").getString("energy"));
                    fileWriter.append(DELIMITER);
                    fileWriter.append("" + df.format(elt.getLong("date")));
                    fileWriter.append(DELIMITER);
                    fileWriter.append("" + FOURNISSEUR);
                    fileWriter.append(SEPARATOR);

                }
                fileWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    @FXML
    void retour(ActionEvent event) {
        FacilitatorProviderLinkClient.stage.close();
        FacilitatorProviderLinkClient.showPages("client/MenuPrincipaleConsommateur.fxml");
    }

}

