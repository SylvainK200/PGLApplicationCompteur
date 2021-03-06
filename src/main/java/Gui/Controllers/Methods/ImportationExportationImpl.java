package Gui.Controllers.Methods;

import Gui.ModelTabs.MenuPrincipalTable;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.sql.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * Implementation des interfaces de gestion des importations/exportations des données.
 */
public class ImportationExportationImpl implements ImportationExportation{
    GeneralMethods generalMethods = new GeneralMethodsImpl();


    public void exportToCSV(File file, List<MenuPrincipalTable> elts) {
        final String DELIMITER = ";";
        final String SEPARATOR = "\n";

        //En-tête de fichier
        final String HEADER = "EAN;Consommation;cout;compteur;date_affectation;date_cloture;name_wallet";
        try{
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.append(HEADER);
            fileWriter.append(SEPARATOR);
            Iterator<MenuPrincipalTable> it = elts.iterator();

            while (it.hasNext()){
                MenuPrincipalTable elt =(MenuPrincipalTable) it.next();

                fileWriter.append(elt.getEan_18());
                fileWriter.append(DELIMITER);
                fileWriter.append(""+elt.getConsommation());
                fileWriter.append(DELIMITER);
                fileWriter.append(""+elt.getCout());
                fileWriter.append(DELIMITER);
                fileWriter.append(""+elt.getType_compteur());
                fileWriter.append(DELIMITER);
                fileWriter.append(""+elt.getDate_affectation());
                fileWriter.append(DELIMITER);
                fileWriter.append(""+elt.getDate_cloture());
                fileWriter.append(DELIMITER);
                fileWriter.append(""+elt.getNameWallet());
                fileWriter.append(SEPARATOR);

            }
            fileWriter.close();
        }catch (Exception e ){
            e.printStackTrace();
        }

    }
    public void importerFileCSV(File file,String typeCompteur){
        //JSONObject compteur = findUnique("supplyPoint/ean_18/"+compteur_importer.getValue());
        try {
            JSONObject result = new JSONObject() ;
            Scanner sc = new Scanner(file);
            sc.useDelimiter("\n");
            int i = 1;

            sc.next();

            while (sc.hasNext())
            {
                String ligne = sc.next();
                String[] elts = ligne.split(";");
                if (i==1){
                    JSONObject compteur = new JSONObject();
                    compteur.put("ean_18",elts[0]);
                    compteur.put("energy",typeCompteur);
                    result = generalMethods.createObject(compteur,"supplyPoint");
                    i+=1;
                }
                String newElement = elts[2].substring(0,5);
                enregistrer (result,elts[1],Integer.parseInt(newElement));

            }
            sc.close();
            generalMethods.afficherAlert("L'importation est terminée.");
        }catch (Exception e ) {
            e.printStackTrace();
            generalMethods.afficherAlert("L'importation a échoué.");
        }
    }
    public void enregistrer(JSONObject result,String date,long index){

        JSONObject consommationValue = new JSONObject();


        consommationValue.put("value",index);
        try{
            consommationValue.put("date", Date.valueOf(date));
        }catch (Exception e) {
            e.printStackTrace();
        }
        
        consommationValue.put("supplyPoint",result);
        JSONObject result2 = generalMethods.createObject(consommationValue,"consommationValue");

        if (!result2.isEmpty()){
            generalMethods.log(this.getClass().getName(), result2.toString());
        }
    }

}