package Gui.ModelTabs;

import Gui.Controllers.client.HistoriqueCompteur;
import org.json.JSONObject;

public class HistoriqueTable {
    private String ean;
    private String type_energy;
    private String date;
    private String consommation;
    private String fournisseur;

    public HistoriqueTable (JSONObject consommation, String fournisseur){

    }
    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
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
