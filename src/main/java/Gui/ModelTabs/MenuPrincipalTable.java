package Gui.ModelTabs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class MenuPrincipalTable {
    private String ean_18;
    private double consommation;
    private double cout ;
    private String type_compteur;
    private String date_affectation;
    private String date_cloture;
    private String nameWallet ;

    public String getNameWallet() {
        return nameWallet;
    }

    public MenuPrincipalTable (JSONObject contract_supply) {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

        type_compteur = contract_supply.getString("meter_type");
        String deb = df.format(contract_supply.getJSONObject("contract").getLong("date_begin"));
        String fin = df.format(contract_supply.getJSONObject("contract").getLong("date_end"));
        cout = contract_supply.getDouble("meter_rate");
        nameWallet = "";
        Object json = contract_supply.get("wallet");
        System.out.println(Objects.isNull(json));
        if (Objects.isNull(json)){
            nameWallet = contract_supply.getJSONObject("wallet").getString("name");
        }
        JSONArray consommations = contract_supply.getJSONObject("supplyPoint").getJSONArray("consommationValues");
        if (consommations.length()>0){
            consommation = consommations.getJSONObject(consommations.length()-1).getDouble("value");
        }
        ean_18 = contract_supply.getJSONObject("supplyPoint").getString("ean_18");


            date_affectation = deb;
            date_cloture =fin;

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
