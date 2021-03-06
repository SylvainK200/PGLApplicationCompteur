package Gui.ModelTabs;

import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONObject;

import Gui.Controllers.Methods.GeneralMethods;
import Gui.Controllers.Methods.GeneralMethodsImpl;

public class MenuPrincipalConsommateurTable {
    public String name;
    public String type_energie;
    public String type_compteur;
    public String Fournisseur;
    public String date_affectation;
    public String date_cloture;
    public String wallet;
    public String consommation;
    public boolean allocated;
    public String cloture;
    GeneralMethods generalMethods = new GeneralMethodsImpl();
    public MenuPrincipalConsommateurTable(JSONObject contract_supply_point ) {
        
        if (!Objects.nonNull(contract_supply_point.get("dateCloture"))){
            cloture  = contract_supply_point.getString("dateCloture").split("T")[0];

        }else {
            cloture = "non defini";
        }
        name = contract_supply_point.getJSONObject("supplyPoint").getString("name");
        type_energie = contract_supply_point.getJSONObject("supplyPoint").getString("energy");
        type_compteur = contract_supply_point.getString("meter_type");
        Fournisseur = contract_supply_point.getJSONObject("provider").getString("company_name");
        date_affectation = contract_supply_point.getString("date_begin").split("T")[0];
        date_cloture = contract_supply_point.getString("date_end").split("T")[0];
        if (!Objects.nonNull(contract_supply_point.get("wallet"))){
            wallet = contract_supply_point.getJSONObject("wallet").getString("name");
        }
        else{
            wallet = "non defini";
        }
        long id = contract_supply_point.getJSONObject("supplyPoint").getLong("id");

        JSONArray consommations = generalMethods.find("historicalValue/historiqueRecent/"+id);
        if (Objects.nonNull(consommations) && consommations.length() > 0){
            int consommationTotal = 0;
            for(int i=0; i< consommations.length();i++){
                consommationTotal += consommations.getJSONObject(i).getDouble("consommation");
            }
            consommation = ""+consommationTotal;
        }else {
            consommation = "0";
        }
    }

    public String getCloture(){
        return cloture;
    }
    public boolean isAllocated() {
        return allocated;
    }

    public String getConsommation(){
        return consommation;
    }
    public String getWallet(){
        return wallet;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType_energie() {
        return type_energie;
    }

    public void setType_energie(String type_energie) {
        this.type_energie = type_energie;
    }

    public String getType_compteur() {
        return type_compteur;
    }

    public void setType_compteur(String type_compteur) {
        this.type_compteur = type_compteur;
    }

    public String getFournisseur() {
        return Fournisseur;
    }

    public void setFournisseur(String fournisseur) {
        Fournisseur = fournisseur;
    }

    public String getDate_affectation() {
        return date_affectation;
    }

    public void setDate_affectation(String date_affectation) {
        this.date_affectation = date_affectation;
    }

    public String getDate_cloture() {
        return date_cloture;
    }

    public void setDate_cloture(String date_cloture) {
        this.date_cloture = date_cloture;
    }
}
