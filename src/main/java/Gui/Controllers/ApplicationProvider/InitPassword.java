package Gui.Controllers.ApplicationProvider;

import java.net.URL;
import java.util.ResourceBundle;

import org.json.JSONObject;

import Gui.FacilitatorProviderLinkClient;
import Gui.Controllers.Methods.GeneralMethods;
import Gui.Controllers.Methods.GeneralMethodsImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class InitPassword implements Initializable {
    @FXML
    private Text login;

    @FXML
    private Label password_label;

    @FXML
    private PasswordField mot_de_passe;

    @FXML
    private Label question;

    @FXML
    private Button renitialise;

    @FXML
    private Button verifier;

    @FXML
    private TextField reponse;

    @FXML
    private ComboBox<String> typeUser;

    @FXML
    private TextField identifiant;

    @FXML
    void backToLogin(MouseEvent event) {
        FacilitatorProviderLinkClient.stage.close();
        FacilitatorProviderLinkClient.showPages("login.fxml");
    }

    @FXML
    void renitialise(ActionEvent event) {
        this.renitialise.setDisable(true);
        System.out.println("user before" + user.toString());
        this.user.put("password", FacilitatorProviderLinkClient.generateHash(mot_de_passe.getText()));
        this.user = generalMethods.updateObject(this.user, ( this.typeUser.getValue()=="Fournisseur" ? "provider" : "user"));
        System.out.println("user after" + user.toString());
        backToLogin(null);
    }

    @FXML
    void verifier(ActionEvent event) {
        if( this.typeUser.getValue() != null ){
            this.user = generalMethods.findUnique( ( this.typeUser.getValue()=="Fournisseur" ? "provider" : "user") +"/identifiant/"+identifiant.getText());

            if(this.user!=null && this.user.has("question_secrete")){
                identifiant.setDisable(true);
                verifier.setDisable(true);
                typeUser.setDisable(true);
                reponse.setDisable(false);

                this.question.setText(this.user.getString("question_secrete"));
            }else{
                generalMethods.afficherAlert("Aucun utilisateur avec cette identifiant. Verifiez le. Si le Probleme persiste, contacter un administrateur à <Lien ADMIN>");
            }

        }else{
            generalMethods.afficherAlert("Selectionner le type d'utilisateur");
        }
    }

    JSONObject user = null;
    
    GeneralMethods generalMethods  = new GeneralMethodsImpl();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reponse.setDisable(true);
        verifier.setDisable(true);
        mot_de_passe.setDisable(true);
        renitialise.setDisable(true);

        this.reponse.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean tmp = ! newValue.equalsIgnoreCase(user.getString("reponse_secrete"));
            mot_de_passe.setDisable(tmp);
            renitialise.setDisable(tmp);
        });

        this.identifiant.textProperty().addListener((observable, oldValue, newValue) -> {
            verifier.setDisable(newValue.isBlank());
        });

        this.typeUser.getItems().addAll("Fournisseur","Consommateur");
    }

}
