package Gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.*;

import javax.crypto.spec.SecretKeySpec;
import static Gui.Encryption.encrypt;

/**
 * This class implement the main window of the portfolio management application
 */
public class FacilitatorProviderLinkClient extends Application {
    public static JSONObject currentClient ;
    public static JSONObject currentprovider;
    public static JSONObject current_supply_point;
    public static Stage  stage = new Stage();
    private final static String keyCrypt = "randomString1234@";
    public static String generateHash(String password){
        String encryptedPassword = password;
        try{
        byte[] salt = new String("12345678").getBytes();
        int iterationCount = 40000;
        int keyLength = 128;
        SecretKeySpec key = Encryption.createSecretKey(keyCrypt.toCharArray(), salt, iterationCount, keyLength);

        String originalPassword = password;
        encryptedPassword = encrypt(originalPassword, key);
        }
        catch (Exception e ){
            e.printStackTrace();
        }
    return encryptedPassword;
    }
    
    public static void main(String[] args){
        launch(args);
    }
    
    public static void showPages (String page ){

        try {
            Parent root = FXMLLoader.load(FacilitatorProviderLinkClient.class.getResource("" + page));
            stage.setScene(new Scene(root));
            stage.show();
        }catch (Exception e )
        {
            e.printStackTrace();
        }

    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        stage=primaryStage;
        showPages("login.fxml");
    }

    public static void startForTests(Stage primaryStage) throws Exception{
        stage=primaryStage;
        showPages("login.fxml");
    }
    


    public static ArrayList<JSONObject> extractConsommations(JSONArray consommationValue, long idSupplyPoint)
    {
        ArrayList<JSONObject> consommations = new ArrayList<>();

        for (int i =0; i< consommationValue.length(); i++){
            JSONObject obj = consommationValue.getJSONObject(i);
            if (obj.getJSONObject("supplyPoint").getLong("id")==idSupplyPoint){
                consommations.add(obj);
            }
        }

        return consommations;
    }



}
