package Gui.ModelTabs;


import com.sun.tools.javac.Main;
import org.json.JSONObject;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PortefeuilleTable {

    public String nom;
    public String addresse;
    public double nombreCompteur;

    public  PortefeuilleTable(JSONObject wallet){
        nom = wallet.getString("name");
        try{
            JSONObject home = wallet.getJSONObject("home");
            if (!home.isEmpty()){
            addresse = home.getString("city")+"-"+ home.getString("street");
            }
        }catch (Exception e ){
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE,
                    "Proble avec l'objet home du JSONObject dans le fichier PortefeuilleTable.java\n\n"
                            +e.getMessage());
        }

        // Creer route pour avoir le nombre de contrat pour un porte feuille
        nombreCompteur = 4;
    }
}
