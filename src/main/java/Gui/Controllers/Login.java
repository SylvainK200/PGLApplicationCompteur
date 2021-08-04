package Gui.Controllers;

import Gui.PortfolioManagementClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

import static Gui.PortfolioManagementClient.currentprovider;
public class Login {

    @FXML
    private TextField identifiant;

    @FXML
    private TextField mot_de_passe;

    @FXML
    private Button connect_button;

    @FXML
    private Text mot_de_passe_perdu;

    @FXML
    private Text creer_compte;
    @FXML
    void connect(ActionEvent event) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:8085/energy-management/provider/identifiant/"
                        +identifiant.getText()+
                        "/"+mot_de_passe.getText())
                .method("GET", null)
                .build();
        try {
            Response response = client.newCall(request).execute();

            JSONObject user = new JSONObject(response.body().string());
            if (!user.isEmpty())
            {
                PortfolioManagementClient.stage.close();
                currentprovider = user;
                System.out.println("current provider :" +currentprovider.toString());
                PortfolioManagementClient.showPages("MenuPrincipale.fxml");
                response.close();
            }


        }catch (JSONException e ){
            System.out.println("reponse vide");

        }
        catch (Exception e ){
            e.printStackTrace();
        }

        }
    @FXML
    void creerCompte(MouseEvent event) {
        PortfolioManagementClient.stage.close();
        PortfolioManagementClient.showPages("creer_compte.fxml");
    }

    @FXML
    void retrouverCompte(MouseEvent event) {
        PortfolioManagementClient.stage.close();
        PortfolioManagementClient.showPages("retrouverCompte.fxml");
    }

}
