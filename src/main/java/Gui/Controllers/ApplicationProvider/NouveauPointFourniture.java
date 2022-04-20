package Gui.Controllers.ApplicationProvider;

import java.net.URL;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONObject;

import static Gui.FacilitatorProviderLinkClient.currentprovider;

import Gui.FacilitatorProviderLinkClient;
import Gui.Controllers.Methods.GeneralMethods;
import Gui.Controllers.Methods.GeneralMethodsImpl;
import Gui.ModelTabs.PointFourniture;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
public class NouveauPointFourniture  implements Initializable{

    @FXML
    private TableColumn<PointFourniture, String> eanCol;

    @FXML
    private TableColumn<PointFourniture, String> energyCol;

    @FXML
    private Button modifier;

    @FXML
    private TableColumn<PointFourniture, String> nameCol;

    @FXML
    private TextField newEan;

    @FXML
    private ComboBox<String> newEnergy;

    @FXML
    private TextField newName;

    @FXML
    private Button quitter;
    
    @FXML
    private Button creerButton;

    @FXML
    private TableView<PointFourniture> table;

    PointFourniture current = null;
    
    @FXML
    void creer(ActionEvent event) {
        String ean = newEan.getText();
        String name = newName.getText();
        String energy = newEnergy.getValue();

        if(ean.isBlank() || name.isBlank() || energy==null || energy.isBlank()){
            generalMethods.afficherAlert("Remplissez tous les champs.");
        }else{
            if(generalMethods.checkEanValue(ean)){
                JSONObject newPointFourniture = new JSONObject();

                if(this.current !=null){
                    newPointFourniture.put("id", current.getId());
                }

                newPointFourniture.put("ean_18", ean);
                newPointFourniture.put("name", name);
                newPointFourniture.put("energy", energy);

                newPointFourniture.put("provider", new JSONObject(currentprovider, "id"));

                try{
                    if(this.current == null ){
                        generalMethods.createObject(newPointFourniture, "pointFourniture");
                    }else{
                        generalMethods.updateObject(newPointFourniture, "pointFourniture");
                    }
                    generalMethods.afficherAlert("Point de fourniture " + this.current==null ? "créé." : "mis à jour.");

                    initTable();
                    if(this.current !=null){
                        this.current = null;
                        creerButton.setText("Créer");
                    }
                    newEan.clear();
                    newName.clear();
                    newEnergy.setValue("");

                }catch(Exception e){
                    generalMethods.afficherAlert("La creation a échoué, regardez les logs.");
                }

            }else{
                generalMethods.afficherAlert("L'Ean doit etre constitué de 18 chiffres");
            }
        }
    }

    @FXML
    void modifier(ActionEvent event) {
        current = table.getSelectionModel().getSelectedItem();
        if(current ==null){
            generalMethods.afficherAlert("Selectionnez un point de fourniture dans le tableau.");
            return;
        }

        newEan.setText(current.getEan());
        newName.setText(current.getNom());
        newEnergy.setValue(current.getEnergy());

        creerButton.setText("Modifier");
    }

    
    @FXML
    void delete(ActionEvent event) {
        current = table.getSelectionModel().getSelectedItem();
        if(current ==null){
            generalMethods.afficherAlert("Selectionnez un point de fourniture dans le tableau.");
            return;
        }
        try{
            generalMethods.deleteObject("pointFourniture/"+current.getId());
            initTable();
            generalMethods.afficherAlert("Point de fourniture supprimé.");
        }catch(Exception e){
            generalMethods.afficherAlert("La Suppression a échoué.");
        }

    }

    @FXML
    void quitter(ActionEvent event) {        
        FacilitatorProviderLinkClient.stage.close();
        FacilitatorProviderLinkClient.showPages("MenuPrincipale.fxml");
    }

    GeneralMethods generalMethods = new GeneralMethodsImpl();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initialize();
    }

    public void initialize() {
        newEnergy.getItems().addAll("ELECTRICITE", "GAZ","EAU");
        initTable();
    }

    public void initTable(){
        eanCol.setCellValueFactory(new PropertyValueFactory<PointFourniture,String>("ean"));
        energyCol.setCellValueFactory(new PropertyValueFactory<PointFourniture,String>("energy"));
        nameCol.setCellValueFactory(new PropertyValueFactory<PointFourniture,String>("nom"));

        JSONArray pointFournitures = generalMethods.find("pointFourniture/provider/"+currentprovider.getString("identifiant"));
        table.getItems().removeAll(table.getItems());

        for (int i =0;i<pointFournitures.length();i++){
            table.getItems().add(new PointFourniture(pointFournitures.getJSONObject(i)));
        }
    }

}
