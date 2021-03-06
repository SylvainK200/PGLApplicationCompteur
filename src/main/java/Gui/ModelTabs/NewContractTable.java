package Gui.ModelTabs;

import okhttp3.MediaType;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class NewContractTable {
    public String nom_client;
    public String num_contrat;
    public String type_contrat;
    public  String debut_contrat;
    public String fin_contrat;
    public String compteur;
    public String typeEnergie;
    public String etat_compteur;
    public DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public NewContractTable(JSONObject client,JSONObject contract_supply_point){
        nom_client = client.getString("identifiant");
        num_contrat = contract_supply_point.getString("numero_contract");

        try {
            debut_contrat = df.format( contract_supply_point.get("date_begin"));
            fin_contrat = df.format( contract_supply_point.get("date_end"));
        }catch(Exception e){
            debut_contrat = contract_supply_point.getString("date_begin");
            fin_contrat = contract_supply_point.getString("date_end");
            
        }

        Object json  = contract_supply_point.get("supplyPoint");
        if (json instanceof  JSONObject){
            
            compteur = ((((JSONObject) json).getString("name")));
            typeEnergie = ((JSONObject)json).getString("energy");
            etat_compteur = getEtat_compteur(contract_supply_point);
        }

    }
    public static String getEtat_compteur(JSONObject contract_supply_point){

        Object json = null;
        if (contract_supply_point.has("wallet"))
        {
            json = contract_supply_point.get("wallet");
            if (json instanceof  JSONObject){
                if (((JSONObject) json).isEmpty()){
                    return "Non Alloue";
                }
                else
                {
                    return "Alloue";
                }
            }
        }
        return "Non Alloue";
    }

    public String getNom_client() {
        return nom_client;
    }

    public void setNom_client(String nom_client) {
        this.nom_client = nom_client;
    }

    public String getNum_contrat() {
        return num_contrat;
    }

    public void setNum_contrat(String num_contrat) {
        this.num_contrat = num_contrat;
    }

    public String getType_contrat() {
        return type_contrat;
    }

    public void setType_contrat(String type_contrat) {
        this.type_contrat = type_contrat;
    }

    public String getDebut_contrat() {
        return debut_contrat;
    }

    public void setDebut_contrat(String debut_contrat) {
        this.debut_contrat = debut_contrat;
    }

    public String getFin_contrat() {
        return fin_contrat;
    }

    public void setFin_contrat(String fin_contrat) {
        this.fin_contrat = fin_contrat;
    }

    public String getCompteur() {
        return compteur;
    }

    public void setCompteur(String compteur) {
        this.compteur = compteur;
    }

    public String getTypeEnergie() {
        return typeEnergie;
    }

    public void setTypeEnergie(String typeEnergie) {
        this.typeEnergie = typeEnergie;
    }

    public String getEtat_compteur() {
        return etat_compteur;
    }

    public void setEtat_compteur(String etat_compteur) {
        this.etat_compteur = etat_compteur;
    }


}
