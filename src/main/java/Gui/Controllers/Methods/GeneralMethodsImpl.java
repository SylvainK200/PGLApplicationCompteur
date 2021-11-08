package Gui.Controllers.Methods;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;

import static Gui.Controllers.CreerCompte.JSON;
import static Gui.Controllers.RetrouverCompte.API_URL;

public class GeneralMethodsImpl implements GeneralMethods{
    @Override
    public JSONObject createObject(JSONObject contract, String url) {
        JSONObject resp = new JSONObject();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
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
            e.printStackTrace();}
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
                JOptionPane.showMessageDialog(null,"Operation d'enregistrement reussie");
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
            Response response = client.newCall(request).execute();
        }catch (Exception e){
            e.printStackTrace();
        }
        return  new JSONObject();

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
            System.out.println(res);
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

}
