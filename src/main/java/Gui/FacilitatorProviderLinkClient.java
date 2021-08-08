package Gui;

import Gui.ModelTabs.MenuPrincipalTable;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.util.*;


/**
 * This class implement the main window of the portfolio management application
 */
public class FacilitatorProviderLinkClient extends Application {
    public static BorderPane rootLayout;
    public static JSONObject currentClient ;
    public static JSONObject currentprovider;
    public static JSONObject current_supply_point;
    public static Stage primaryStage;
    public static Stage  stage = new Stage();
    public static void main(String[] args){
        launch(args);
    }
    public static void showPage (String file){
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(FacilitatorProviderLinkClient.class.getResource(""+file));
            AnchorPane journal = (AnchorPane) loader.load();
            // Set person overview into the center of root layout.
            rootLayout.setCenter(journal);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void  showEditDialog(){
        try{

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void showPages (String page ){

        try {
            Parent root = FXMLLoader.load(FacilitatorProviderLinkClient.class.getResource("" + page));
            //stage.setTitle(titre);
            stage.setScene(new Scene(root));
            stage.show();
        }catch (Exception e )
        {
            e.printStackTrace();
        }

    }

    @Override
    public void start(Stage primaryStage) throws Exception{
       stage=primaryStage;
       showPages("login.fxml");
    }
    
    public void iporterFileCSV(File file,String typeCompteur){
        //JSONObject compteur = findUnique("supplyPoint/ean_18/"+compteur_importer.getValue());
        try {
            JSONObject result = new JSONObject() ;
            Scanner sc = new Scanner(file);
            sc.useDelimiter("\n");
            int i = 0 ;
            int j = 0;
            while (sc.hasNext())
            {

                if (i > 0) {
                    String ligne = sc.next();
                    System.out.println(ligne);
                    String[] elts = ligne.split(";");
                    if (i==1){
                        JSONObject compteur = new JSONObject();
                        compteur.put("ean_18",elts[0]);
                        compteur.put("energy",typeCompteur);
                       // result = createObject(compteur,"supplyPoint");
                        i+=1;
                    }
                    String newElement = elts[1];
                    try
                    {
                        Date date =Date.valueOf(elts[1]);
                        System.out.println(date);
                    }catch (Exception e){
                        System.out.println("e");
                    }
                    //enregistrer (result,elts[1],Integer.parseInt(newElement));

                }
                else {
                    i++;
                    String ligne = sc.next();
                }

            }
            sc.close();
            JOptionPane.showMessageDialog(null,"Importation terminee");
        }catch (Exception e ) {
            e.printStackTrace();
        }

    }


    public static ArrayList<JSONObject> extractConsommations(JSONArray consommationValue, long idSupplyPoint)
    {
        ArrayList<JSONObject> consommations = new ArrayList<>();
        for (JSONObject obj : consommations){
            if (obj.getJSONObject("supplyPoint").getLong("id")==idSupplyPoint){
                consommations.add(obj);
            }
        }

        return consommations;
    }

    private JSONObject generateSupplyPoint(String ean, String supplierName){
        JSONObject supplyPoint = new JSONObject();
        supplyPoint.put("ean",ean);
        supplyPoint.put("supplier",supplierName);
        return supplyPoint;
    }
    public static void eportToCSV(File file, List<MenuPrincipalTable> elts) {
        final String DELIMITER = ";";
        final String SEPARATOR = "\n";

        //En-tête de fichier
        final String HEADER = "EAN;Consommation;cout;compteur;date_affectation;date_cloture;name_wallet";
        try{
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.append(HEADER);
            fileWriter.append(SEPARATOR);
            Iterator it = elts.iterator();

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
}
