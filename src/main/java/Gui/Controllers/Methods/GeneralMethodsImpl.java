package Gui.Controllers.Methods;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;

import static Gui.Controllers.ApplicationProvider.CreerCompte.JSON;

public class GeneralMethodsImpl implements GeneralMethods{
    public  static String API_URL = "http://localhost:8085/energy-management";

    private static GeneralMethodsImpl INSTANCE;

    @Override
    public JSONObject createObject(JSONObject contract, String url) {
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
                JOptionPane.showMessageDialog(null,"Operation d'enregistrement reussie", "Message", JOptionPane.INFORMATION_MESSAGE);
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
            System.out.println("resultat "+res);
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
            System.out.println("this is the result for method : "+res);
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
            JOptionPane.showMessageDialog(null,"Vos identifiants semblent incorrets! Verifiez les et reessayer.", "Message", JOptionPane.INFORMATION_MESSAGE);
            System.out.println("GeneralMethodsImpl.java -> login()");

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
                System.out.println("Enregistrement termine");
                return true;

            }
            response.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static GeneralMethodsImpl getInstance(){
        if( INSTANCE == null){
            INSTANCE = new GeneralMethodsImpl();
        }
        return INSTANCE;
    }

    public GeneralMethodsImpl(){}
}
