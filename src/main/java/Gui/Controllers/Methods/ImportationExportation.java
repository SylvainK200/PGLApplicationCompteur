package Gui.Controllers.Methods;

import Gui.ModelTabs.MenuPrincipalTable;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

/**
 * Interface de gestion des importations/exportations des données.
 */
public interface ImportationExportation {
    /**
     * Importation depuis un fichier CSV
     * @param file
     * @param typeCompteur
     */
    public void importerFileCSV(File file, String typeCompteur);

    /**
     * Enregistrement des données.
     * @param result
     * @param date
     * @param index
     */
    public  void enregistrer(JSONObject result, String date, long index);

    /**
     * Enregistre les elements contenus dans la liste elts dans le fichier file.
     * @param file
     * @param elts
     */
    public  void exportToCSV(File file, List<MenuPrincipalTable> elts);
}
