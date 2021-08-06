package Gui.ModelTabs;

import org.json.JSONObject;

public class MenuPrincipalConsommateurTable {
    public String ean_18;
    public String type_energie;
    public String type_compteur;
    public String Fournisseur;
    public String date_affectation;
    public String date_cloture;
    public String wallet;

    public MenuPrincipalConsommateurTable(JSONObject contract_supply_point ) {

    }
    public String getWallet(){
        return wallet;
    }
    public String getEan_18() {
        return ean_18;
    }

    public void setEan_18(String ean_18) {
        this.ean_18 = ean_18;
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
