package Gui.Controllers.Methods;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.StringConverter;

import static Gui.Controllers.ApplicationProvider.CreerCompte.JSON;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GeneralMethodsImpl implements GeneralMethods{
    public  static String API_URL = "http://localhost:8085/energy-management";
    //public  static String API_URL = "https://energy-management-be.herokuapp.com/energy-management";

    @Override
    public JSONObject createObject(JSONObject contract, String url) {

        this.log(this.getClass().getName(), "Creating "+url + " : " + contract);

        JSONObject resp = new JSONObject();
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        RequestBody formBody = RequestBody.create(JSON, contract.toString());
        Request request = new Request.Builder()
                .url(API_URL +"/"+ url)
                .post(formBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                return new JSONObject(response.body().string());
            }
            response.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        this.log(this.getClass().getName(), "Responding : "+resp);

        return resp;
    }

    public JSONObject updateObject(JSONObject contract,String url) {
        JSONObject resp = new JSONObject();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        RequestBody formBody = RequestBody.create(JSON, contract.toString());
        Request request = new Request.Builder()
                .url(API_URL +"/"+ url)
                .put(formBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                this.log(this.getClass().getName(),"Operation d'enregistrement reussie.");
                return new JSONObject(response.body().string());
            }
            response.close();
        }
        catch (Exception e){
            e.printStackTrace();}
        return resp;
    }

    public JSONObject deleteObject (String url) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(API_URL+"/"+url)
                .method("DELETE", null)
                .build();

        try {
            client.newCall(request).execute();
        }catch (Exception e){
            e.printStackTrace();
        }
        return new JSONObject();

    }

    public JSONObject findUnique(String url){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(API_URL+"/"+url)
                .method("GET", null)
                .build();
        JSONObject result = null;
        try {
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            this.log(this.getClass().getName(), "this is the result for method find unique /"+url+" : "+res);

            if (response.isSuccessful())
            {
                result= new JSONObject(res);
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public JSONArray find(String url){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(API_URL+"/"+url)
                .method("GET", null)
                .build();
        JSONArray result = null;
        try {
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            this.log(this.getClass().getName(), "this is the result for method find /"+url+" : "+res);
            if (res !=null)
            {
                result= new JSONArray(res);
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public JSONObject signin(String identifiant, String password, Boolean isProvider) {
        password = password.replace("/", "-");
        String url = "" ;
        if (isProvider){
        url = API_URL+"/provider/identifiant/"+identifiant+"/"+password;
        }
        else {
            url = API_URL + "/user/identifiant/"+identifiant+"/"+password;
        }

        OkHttpClient client = new OkHttpClient().newBuilder().build();

        Request request = new Request.Builder()
            .url(url)
            .method("GET", null)
            .build();

        try {
            Response response = client.newCall(request).execute();
            JSONObject user = new JSONObject(response.body().string());
            response.close();
            return user;
        } catch (JSONException e) {
            this.afficherAlert("Vos identifiants semblent incorrets! Verifiez les et reessayer.");
            this.log(this.getClass().getName(), "GeneralMethodsImpl.java -> login()");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean signup(String name, String identifiant, String password, String adresse_mail,
                             String question_secrete, String reponse_question_secrete, String street, 
                             String number, String city, String postal_code, Boolean isProvider){
        
        JSONObject body = new JSONObject();
        body.put("address_mail", adresse_mail)
            .put("password", password)
            .put("question_secrete",question_secrete)
            .put("reponse_secrete",reponse_question_secrete)
            .put("identifiant",identifiant)
            .put("street",street)
            .put("number",number)
            .put("city",city)
            .put("postal_code",postal_code);

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        
        String url = "";

        if(isProvider){
            url = "/provider";
            body.put("company_name",name);
        }else{
            url = "/user";
            body.put("name",name);
        }

        RequestBody formBody = RequestBody.create(JSON, body.toString());
        Request request = new Request.Builder()
                .url(API_URL +url)
                .post(formBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                response.close();
                this.log(this.getClass().getName(), "Enregistrement termine");
                return true;

            }
            response.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public GeneralMethodsImpl(){}

    @Override
    public void redefineDatePickerDateFormat(DatePicker datePicker) {
        datePicker.setConverter(new StringConverter<LocalDate>() {
            String pattern = "yyyy-MM-dd";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
           
            {
                datePicker.setPromptText(pattern.toLowerCase());
            }
           
            @Override public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
           
            @Override public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
           });
    }
    
    @Override
    public FileChooser getFileChooser(){

        FileChooser js = new FileChooser();

        js.getExtensionFilters().add(new ExtensionFilter("Supported Files (*.csv|*.json|*.yml|*.Yaml)", "*.csv","*.csv","*.json", "*.yml","*.yaml"));

        js.getExtensionFilters().add(new ExtensionFilter("CSV Files (*.csv)", "*.csv"));

        js.getExtensionFilters().add(new ExtensionFilter("JSON Files (*.json)", "*.json"));

        js.getExtensionFilters().add(new ExtensionFilter("YAML Files (*.yml | *.yaml)", "*.yml","*.yaml"));

        return js;
    }

    @Override
    public boolean checkEanValue(String ean){
        if (ean == null) {
            return false;
        }
        try {
            if(ean.length()==18){
                Integer.parseInt(ean.substring(0, 6));
                Integer.parseInt(ean.substring(6, 12));
                Integer.parseInt(ean.substring(12, 18));
                return true;
            }else{
               return false ;
            }
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    @Override
    public void logOperation(Logger logger, String operationWarning, String operationSevere) {
        if(operationWarning.equals("")){
            logger.log(Level.SEVERE,operationSevere);
        }
        else if (operationSevere == ""){
            logger.log(Level.WARNING,operationWarning);
        }else if (!operationSevere.equals("") && !operationWarning.equals("")){
            logger.log(Level.SEVERE,operationSevere);
            logger.log(Level.WARNING,operationWarning);
        }
    }

    @Override
    public void log(String classname,String logs){
        Logger logger = Logger.getLogger(classname);
        logOperation(logger, logs, "");
    }

    @Override
    public void afficherAlert(String contentText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,contentText, ButtonType.OK);
        alert.showAndWait();
    }
}
