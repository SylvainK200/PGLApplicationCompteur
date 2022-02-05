package Gui.Controllers.Methods;

import org.json.JSONArray;
import org.json.JSONObject;

public interface GeneralMethods {

    public  JSONObject createObject(JSONObject contract, String url);
    public JSONObject updateObject(JSONObject contract,String url);
    public  JSONObject deleteObject (String url);
    public JSONObject findUnique(String url);
    public  JSONArray find(String url);

    public JSONArray findSupplyPointByUser();
    public JSONArray findSupplyPointByUserByEan_18();
    public JSONArray findContractByUserByEan_18();

}
