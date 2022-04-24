package Gui.Controllers.Methods;

import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.scene.control.DatePicker;
import javafx.stage.FileChooser;

/**
 * Liste des interfaces des fonctions permettant d'interagir avec le backend.
 */
public interface GeneralMethods {

    /**
     * Interface de creation d'un objet.
     * Utiliser pour faire les requetes POST ( contenant object dans le corps ) à l'adresse specifiée.
     * @param url
     * @param object
     * @return
     */
    public  JSONObject createObject(JSONObject object, String url);

    /**
     * Interface de mise à jour d'un objet.
     * Utiliser pour faire les requetes PUT ( contenant object dans le corps ) à l'adresse specifiée.
     * @param url
     * @param object
     * @return
     */
    public JSONObject updateObject(JSONObject object,String url);

    /**
     * Interface de suppression d'un objet.
     * Utiliser pour faire les requetes DELETE à l'adresse specifiée.
     * @param url
     * @return
     */
    public  JSONObject deleteObject (String url);

     /**
     * Interface de selection d'un element.
     * Utiliser pour faire les requetes GET à l'adresse specifiée.
     * Retourne un element
     * @param url
     * @return
     */
    public JSONObject findUnique(String url);

    /**
     * Interface de selection multiple d'elements.
     * Utiliser pour faire les requetes GET à l'adresse specifiée.
     * Retourne une liste d'elements
     * @param url
     * @return
     */
    public JSONArray find(String url);

    /**
     * Interface de login d'un utilisateur.
     * @param username
     * @param password
     * @param isProvider
     * @return
     */
    public JSONObject signin(String username, String password, Boolean isProvider);

    /**
     * Interface de creation d'un nouvel utilisateur
     * @param name
     * @param identifiant
     * @param password
     * @param adresse_mail
     * @param question_secrete
     * @param reponse_question_secrete
     * @param street
     * @param number
     * @param city
     * @param postal_code
     * @param isProvider
     * @return
     */
    public boolean signup(String name, String identifiant, String password, String adresse_mail,
                          String question_secrete, String reponse_question_secrete, String street, 
                          String number, String city, String postal_code, Boolean isProvider);


    /**
     * Utiliser pour redefinir le format d'affichage des dates.
     * @param datePicker
     */
    public void redefineDatePickerDateFormat(DatePicker datePicker);

    /**
     * Chargement du dialog de selection de fichiers pour les importations/exportations.
     * @return
     */
    public FileChooser getFileChooser();

    /**
     * Check if the ean consists of 18 digits
     * @param ean the ean
     * @return True if the ean consists of 18 digits else, False

    */
    public boolean checkEanValue(String ean);

    /**
     * Utiliser pour faire les logs
     * @param logger
     * @param operationWarning
     * @param operationSevere
     */
    public void logOperation(Logger logger,String operationWarning, String operationSevere );
    public void log(String classname,String logs);

    /**
     * Utiliser pour afficher des notifications
     * @param contentText
     */
    public void afficherAlert(String contentText);
}
