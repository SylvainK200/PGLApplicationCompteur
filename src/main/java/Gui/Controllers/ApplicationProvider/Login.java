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
import static Gui.FacilitatorProviderLinkClient.currentClient;

/**
 * Controlleur de Gestion du login
 * Processus : 
 *  1. Il entre son identifiant, son mot de passe et selectionne son type ( Consommateur/Fournisseur)
 *  2. On encrypte son mot de passe 
 *  3. On fait une requete d'authentification au backend avec son identifiant et son password encrypté
 *  4. Si c'est ok, on charge l'interface principale dépendamment de son type, sinon une notification est affiché disant ce qui c'est mal passé.
 * Contrainte :
 *  1. Le type d'utilisateur doit être spécifié
 */
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

    /**
     * Action effectuée quand on click sur le bouton "se connecter"
     * On recupere le username et le password. Puis, on encrypte le password et on fait la
     * requête de connexion au backend. Si elle reussie on charge la page principale ( dépendamment du type d'utilisateur)
     * Sinon, on affiche un message expressif.
     * @param event
     */
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

                JSONObject user = generalMethods.signin(username, FacilitatorProviderLinkClient.generateHash(password), !type_utilisateur.getValue().equals("Consommateur"));
                
                if ( user!=null  && !user.isEmpty()) {
                    FacilitatorProviderLinkClient.stage.close();
                    if (this.type_utilisateur.getValue().equals("Fournisseur"))
                    {
                        currentprovider = user;
                        generalMethods.log(this.getClass().getName(),"Current provider :" + currentprovider.toString());
                        
                        FacilitatorProviderLinkClient.showPages("MenuPrincipale.fxml");
                    }else {
                        currentClient = user;
                        generalMethods.log(this.getClass().getName(), "current provider :" + currentClient.toString());
                        
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
    /**
     * Quand on click sur "creer un compte", on charge la page de creation de compte.
     * @param event
     */
    @FXML
    void creerCompte(MouseEvent event) {
        FacilitatorProviderLinkClient.stage.close();
        FacilitatorProviderLinkClient.showPages("creer_compte.fxml");
    }

    /**
     * Quand on click sur "Mot de passe oublié", on charge la page de reinitialisation du mot de passe.
     * @param event
     */
    @FXML
    void retrouverCompte(MouseEvent event) {
        FacilitatorProviderLinkClient.stage.close();
        FacilitatorProviderLinkClient.showPages("recover_password.fxml");
    }

}
