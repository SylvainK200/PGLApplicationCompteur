package Gui.Controllers.ApplicationProvider;

import Gui.FacilitatorProviderLinkClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;

import javax.swing.JOptionPane;

import static Gui.Controllers.ApplicationProvider.CreerCompte.JSON;
import static Gui.Controllers.Methods.GeneralMethodsImpl.API_URL;

public class RetrouverCompte {

    @FXML
    private TextField identifiant;

    @FXML
    private Button verify_question;

    @FXML
    private Button quitter;

    @FXML
    private TextArea question_secrete;
    @FXML
    private TextArea reponse_secrete;

    @FXML
    private PasswordField new_password;

    @FXML
    private PasswordField confirm_password;

    @FXML
    private Button valider;
    private JSONObject currentUser;
    public void initialize(){
        new_password.setDisable(true);
        confirm_password.setDisable(true);
        valider.setDisable(true);
    }
    @FXML
    void quitterPage(ActionEvent event) {
        FacilitatorProviderLinkClient.stage.close();
        FacilitatorProviderLinkClient.showPages("login.fxml");

    }

    @FXML
    void validerMotDePasse(ActionEvent event) {
        currentUser.remove("password");
        if (new_password.getText().equals(confirm_password.getText()))
        {
            currentUser.put("password",new_password.getText());
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();

            RequestBody formBody = RequestBody.create(JSON, currentUser.toString());
            Request request = new Request.Builder()
                    .url(API_URL + "/provider")
                    .put(formBody)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()){
                    System.out.println("Enregistrement termine");
                    FacilitatorProviderLinkClient.stage.close();
                    FacilitatorProviderLinkClient.showPages("login.fxml");

                }
                response.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }else
        {
            JOptionPane.showMessageDialog(null,"le mot de passe et sa confirmation sont differents", "Message", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    @FXML
    void verifierQuestion(ActionEvent event) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(API_URL+"/provider/identifiant/"+identifiant.getText())
                .method("GET", null)
                .build();
        try {
            Response response = client.newCall(request).execute();

            JSONObject user =new JSONObject(response.body().string());
            currentUser= user;
            if (user.get("reponse_secrete").equals(reponse_secrete.getText()))
            {

                new_password.setDisable(false);
                confirm_password.setDisable(false);
                valider.setDisable(false);
            }
            response.close();

        }catch (Exception e ){
            e.printStackTrace();
        }

    }

}
