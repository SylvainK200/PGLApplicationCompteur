package Gui.ModelTabs;

import org.json.JSONObject;

public class PointFourniture {
    public Long id;
    public String nom;
    public String ean;
    public String energy;

    public PointFourniture(Long id, String nom, String ean, String energy) {
        this.id = id;
        this.nom = nom;
        this.ean = ean;
        this.energy = energy;
    }
    
    public PointFourniture(JSONObject json) {
        this.id = json.optLong("id");
        nom = json.optString("name");
        ean = json.optString("ean_18");
        energy = json.optString("energy");
    }

    public String getNom() {
        return nom;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getEan() {
        return ean;
    }
    public void setEan(String ean) {
        this.ean = ean;
    }
    public String getEnergy() {
        return energy;
    }
    public void setEnergy(String energy) {
        this.energy = energy;
    }
}
