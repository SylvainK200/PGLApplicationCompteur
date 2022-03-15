package Gui.Controllers.Methods;

import org.json.JSONArray;
import org.json.JSONObject;

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
}
