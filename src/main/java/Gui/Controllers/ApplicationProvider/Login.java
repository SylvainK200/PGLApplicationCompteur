package Gui.Controllers.ApplicationProvider;

import Gui.FacilitatorProviderLinkClient;
import Gui.Controllers.Methods.GeneralMethodsImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import static Gui.FacilitatorProviderLinkClient.currentprovider;
import static Gui.FacilitatorProviderLinkClient.currentClient;;
public class Login {

    @FXML
    private TextField identifiant;

    @FXML
    private PasswordField mot_de_passe;

    @FXML
    private Button connect_button;
    @FXML
    private ComboBox<String> type_utilisateur;
    @FXML
    private Text mot_de_passe_perdu;

    @FXML
    private Text creer_compte;
    public void initialize(){
        type_utilisateur.getItems().addAll("Consommateur","Fournisseur");
    }

    public GeneralMethodsImpl generalMethods = new GeneralMethodsImpl();

    @FXML
    void connect(ActionEvent event) {
        
        if (Objects.nonNull(type_utilisateur.getValue()) && type_utilisateur.getValue()!="") {
            String username = identifiant.getText() ;
            String password = mot_de_passe.getText();

            if(username.isBlank() || password.isBlank()){
                generalMethods.afficherAlert("Veuillez entrer le nom d'utilisateur et le password.");
                return;
            }

            try {

                JSONObject user = generalMethods.signin(username, password, !type_utilisateur.getValue().equals("Consommateur"));
                
                if ( user!=null  && !user.isEmpty()) {
                    FacilitatorProviderLinkClient.stage.close();
                    if (this.type_utilisateur.getValue().equals("Fournisseur"))
                    {
                        currentprovider = user;
                        System.out.println("current provider :" + currentprovider.toString());
                        FacilitatorProviderLinkClient.showPages("MenuPrincipale.fxml");
                    }else {
                        currentClient = user;
                        System.out.println("current provider :" + currentClient.toString());
                        FacilitatorProviderLinkClient.showPages("client/MenuPrincipaleConsommateur.fxml");
                    }

                }else{
                    generalMethods.afficherAlert("Vos identifiants semblent incorrets! Verifiez les et reessayer.");
                }
            } catch (JSONException e) {
                generalMethods.afficherAlert("Vos identifiants semblent incorrets! Verifiez les et reessayer.");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            generalMethods.afficherAlert("Veuillez choisir le type d'utilisateur.");
        }
        }
    @FXML
    void creerCompte(MouseEvent event) {
        FacilitatorProviderLinkClient.stage.close();
        FacilitatorProviderLinkClient.showPages("creer_compte.fxml");
    }

    @FXML
    void retrouverCompte(MouseEvent event) {
        /*
        FacilitatorProviderLinkClient.stage.close();
        FacilitatorProviderLinkClient.showPages("retrouverCompte.fxml");
        */
        generalMethods.afficherAlert("Désolé, fonctionnalité pas prise en compte.");
    }

}
