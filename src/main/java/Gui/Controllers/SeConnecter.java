package Gui.Controllers;

import Gui.FacilitatorProviderLinkClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SeConnecter implements Initializable {
    @FXML
    private Button ButtonQuitter;
    @FXML
    private Label  labelmessage;
    @FXML
    private TextField TextIdentifiant ;
    @FXML
    private TextField  TextPassword;
    @Override
    public void initialize(URL url , ResourceBundle resourceBundle){

    }
    public void ButtonConnecter( ActionEvent event){
        if (TextIdentifiant.getText().isBlank()==false && TextPassword.getText().isBlank()==false) {
            labelmessage.setText("votre mot de passe ou identifiant pas correcte");
        }else {
            labelmessage.setText("remplire s'il vous plaît identifiant et mot de passe");
        }
    }
    public void Quitter(ActionEvent event){
        FacilitatorProviderLinkClient.stage.close();
        FacilitatorProviderLinkClient.showPages("SupprimerDonnes.fxml");

    }

}
