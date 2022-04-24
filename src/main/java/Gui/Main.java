package Gui;

import javafx.application.Application;

/**
 * Wrapper de la classe FacilitatorProviderLinkClient. Utile pour la generation d'executables.
 * En effet la classe principale ne doit pas étendre la classe Application de JavaFX.
 */
public class Main {
    public static void main(String[] args){
        Application.launch(FacilitatorProviderLinkClient.class, args);
    }
}
