package Gui.ModelTabs;

import org.json.JSONObject;

public class HistoriqueTable {
    private String name;
    private String type_energy;
    private String date;
    private String consommation;
    private String fournisseur;

    public HistoriqueTable (JSONObject consommationelt, String fournis){
        Object supply_point = consommationelt.get("supplyPoint");
        if (supply_point instanceof JSONObject) {
            name = consommationelt.getJSONObject("supplyPoint").getString("name");
            type_energy = consommationelt.getJSONObject("supplyPoint").getString("energy");
        } else {
            name = "non defini";
            type_energy = "non defini";
        }
        date = consommationelt.getString("date").split("T")[0];
        consommation = consommationelt.getLong("consommation")+"";
        fournisseur = fournis;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType_energy() {
        return type_energy;
    }

    public void setType_energy(String type_energy) {
        this.type_energy = type_energy;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getConsommation() {
        return consommation;
    }

    public void setConsommation(String consommation) {
        this.consommation = consommation;
    }

    public String getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(String fournisseur) {
        this.fournisseur = fournisseur;
    }
}
