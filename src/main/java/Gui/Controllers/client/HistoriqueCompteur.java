package Gui.Controllers.client;

import Gui.ModelTabs.HistoriqueTable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

public class HistoriqueCompteur {

    @FXML
    private ComboBox<?> combEAN;

    @FXML
    private TableView<HistoriqueTable> table;

    @FXML
    private TableColumn<?, ?> colEAN;

    @FXML
    private TableColumn<?, ?> type_energy;

    @FXML
    private TableColumn<?, ?> dateConsommation;

    @FXML
    private TableColumn<?, ?> consommation;

    @FXML
    private TableColumn<?, ?> fournisseur;

    @FXML
    private Button boutton_retour;

    @FXML
    private AnchorPane exportation;

    @FXML
    private ComboBox<?> ean_exporter;

    @FXML
    private DatePicker date_debut_importation;

    @FXML
    private DatePicker date_maximale;

    @FXML
    private Button exporter_button;

    @FXML
    private Button annuler;

    @FXML
    void annuler_button(ActionEvent event) {

    }

    @FXML
    void exporterDonnee(ActionEvent event) {

    }

    @FXML
    void retour(ActionEvent event) {

    }

}
