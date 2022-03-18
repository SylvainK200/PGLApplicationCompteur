package Gui.ModelTabs;

import static Gui.FacilitatorProviderLinkClient.extractConsommations;

import java.util.ArrayList;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Gui.Controllers.Methods.GeneralMethods;
import Gui.Controllers.Methods.GeneralMethodsImpl;


public class MenuPrincipalTable {
    private String ean_18;
    private double consommation;
    private double cout ;
    private String type_compteur;
    private String date_affectation;
    private String date_cloture;
    private String nameWallet ;
    GeneralMethods generalMethods = new GeneralMethodsImpl();
    public String getNameWallet() {
        return nameWallet;
    }

    public MenuPrincipalTable (JSONObject contract_supply) {

        type_compteur = contract_supply.getJSONObject("supplyPoint").getString("energy");
        
        try {
            date_affectation = contract_supply.getString("date_begin").split("T")[0];
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            date_cloture = contract_supply.getString("date_end").split("T")[0];
        } catch (JSONException e) {
            e.printStackTrace();
        }
        cout = contract_supply.getDouble("meter_rate");
        nameWallet = "";
        Object json = contract_supply.get("wallet");
        System.out.println("Wallet is null ? " + Objects.isNull(json));
        if (!Objects.isNull(json)){
            nameWallet = contract_supply.getJSONObject("wallet").getString("name");
        }

        JSONArray consommationValues = generalMethods.find("supplyPoint");
        if (contract_supply.get("supplyPoint") instanceof  JSONObject)
        {
            ArrayList<JSONObject> consommations = extractConsommations(consommationValues,contract_supply.getJSONObject("supplyPoint").getLong("id")) ;
            if (consommations.size()>0){
                consommation = consommations.get(consommations.size()-1).getDouble("consommation");
            }
            ean_18 = contract_supply.getJSONObject("supplyPoint").getString("ean_18");

        }

    }

    public String getEan_18() {
        return ean_18;
    }

    public double getConsommation() {
        return consommation;
    }

    public double getCout() {
        return cout;
    }

    public String getType_compteur() {
        return type_compteur;
    }

    public String getDate_affectation() {
        return date_affectation;
    }

    public String getDate_cloture() {
        return date_cloture;
    }
}
