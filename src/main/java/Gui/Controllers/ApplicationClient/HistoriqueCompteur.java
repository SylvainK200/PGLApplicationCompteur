package Gui.Controllers.ApplicationClient;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;

import org.json.JSONArray;
import org.json.JSONObject;

import Gui.FacilitatorProviderLinkClient;
import Gui.Controllers.Methods.GeneralMethods;
import Gui.Controllers.Methods.GeneralMethodsImpl;
import Gui.ModelTabs.HistoriqueTable;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

/**
 * Gestion de l'historique de consommation du client.
 */
public class HistoriqueCompteur {

    GeneralMethods generalMethods = new GeneralMethodsImpl();
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

    /**
     * On charge la liste des consommations du client.
     */
    public void initialize(){
        colEAN.setCellValueFactory(new PropertyValueFactory<HistoriqueTable,String>("name"));
        type_energy.setCellValueFactory(new PropertyValueFactory<HistoriqueTable,String>("type_energy"));
        dateConsommation.setCellValueFactory(new PropertyValueFactory<HistoriqueTable,String>("date"));
        fournisseur.setCellValueFactory(new PropertyValueFactory<HistoriqueTable,String>("fournisseur"));
        consommation.setCellValueFactory(new PropertyValueFactory<HistoriqueTable,String>("consommation"));
        JSONArray suppliesPoint = generalMethods.find("supplyPoint/client/identifiant/"+ FacilitatorProviderLinkClient.currentClient.getString("identifiant"));
        for(int i = 0; i<suppliesPoint.length();i++){
            JSONObject currentObject = suppliesPoint.getJSONObject(i);
            JSONArray consommationSupplyPoint = generalMethods.find("historicalValue/historiqueRecent/"+currentObject.getLong("id"));
            combEAN.getItems().add(currentObject.getString("name"));
            ean_exporter.getItems().add(currentObject.getString("name"));
            for (int j =0;j<consommationSupplyPoint.length();j++){
                JSONObject current_consommation = consommationSupplyPoint.getJSONObject(j);
                JSONObject provider = generalMethods.findUnique("provider/ean/"+currentObject.getString("ean_18"));

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
                        if (contrat.getName().toLowerCase().contains(newValue.toLowerCase())
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

    /**
     * Fonction permettant l'exportation des données de consommation du client.
     * L'utilisateur doit OBLIGATOIREMENT specifier la date minimale d'exportation.
     * Par contre, il peut ne pas specifier la date maximale. Dans ce cas, on utilise la date du jour courant.
     * @param event
     */
    @FXML
    void exporterDonnee(ActionEvent event) {
        String ean = ean_exporter.getValue();

        if(ean==null || ean.isEmpty() || ean.isBlank()){
            generalMethods.afficherAlert("Veuillez selectionner le Compteur à exporter");
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

        JSONObject supply = generalMethods.findUnique("supplyPoint/name/" + ean);
        JSONArray consommations = generalMethods.find("historicalValue/consommations/" + supply.getLong("id") +
                "/" + date_deb + "/" + date_fin);
        final String DELIMITER = ";";
        final String SEPARATOR = "\n";
        final String HEADER = "EAN Compteur; Nom Compteur; type energie;date lecture;consommation;fournisseur";
        final String FOURNISSEUR = generalMethods.findUnique("provider/ean/" + supply.getString("ean_18")).getString("company_name");
        FileChooser js = generalMethods.getFileChooser();
        js.setTitle("Export to ");
        File file = js.showSaveDialog(null);
        if (file != null) {
            try {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.append(HEADER);
                fileWriter.append(SEPARATOR);

                for (int i = 0; i < consommations.length(); i++) {
                    JSONObject elt = consommations.getJSONObject(i);

                    fileWriter.append(elt.getJSONObject("supplyPoint").getString("ean_18"));
                    fileWriter.append(DELIMITER);
                    fileWriter.append(elt.getJSONObject("supplyPoint").getString("name"));
                    fileWriter.append(DELIMITER);
                    fileWriter.append("" + elt.getJSONObject("supplyPoint").getString("energy"));
                    fileWriter.append(DELIMITER);
                    fileWriter.append("" + elt.getString("date").split("T")[0]);
                    fileWriter.append(DELIMITER);
                    fileWriter.append("" + elt.getLong("consommation"));
                    fileWriter.append(DELIMITER);
                    fileWriter.append("" + FOURNISSEUR);
                    fileWriter.append(SEPARATOR);
                }
                fileWriter.close();

                generalMethods.afficherAlert("Exportation effectuée avec succès dans le fichier : " + file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
                generalMethods.afficherAlert("Exportation a échoué. Regardez les logs.");
            }
        }
    }
    /**
     * On ferme l'interface et on charge l'interface principale.
     * @param event
     */
    @FXML
    void retour(ActionEvent event) {
        FacilitatorProviderLinkClient.stage.close();
        FacilitatorProviderLinkClient.showPages("client/MenuPrincipaleConsommateur.fxml");
    }

}

