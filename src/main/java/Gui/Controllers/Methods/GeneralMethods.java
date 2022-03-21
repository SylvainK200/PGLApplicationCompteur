package Gui.Controllers.Methods;

import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.scene.control.DatePicker;
import javafx.stage.FileChooser;

public interface GeneralMethods {

    public  JSONObject createObject(JSONObject contract, String url);
    public JSONObject updateObject(JSONObject contract,String url);
    public  JSONObject deleteObject (String url);
    public JSONObject findUnique(String url);
    public  JSONArray find(String url);

    public JSONObject signin(String username, String password, Boolean isProvider);

    public boolean signup(String name, String identifiant, String password, String adresse_mail,
                          String question_secrete, String reponse_question_secrete, String street, 
                          String number, String city, String postal_code, Boolean isProvider);


    public void redefineDatePickerDateFormat(DatePicker datePicker);
    public FileChooser getFileChooser();

    /**
     * Check if the ean consists of 18 digits
     * @param ean the ean
     * @return True if the ean consists of 18 digits else, False

    */
    public boolean checkEanValue(String ean);

    public void logOperation(Logger logger,String operationWarning, String operationSevere );

    public void afficherAlert(String contentText);
}
