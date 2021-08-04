package Gui;

import Gui.ModelTabs.MenuPrincipalTable;
import com.sun.tools.javac.Main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import portfolioManagement.Portfolio;
import portfolioManagement.SupplyPoint;
import portfolioManagement.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


/**
 * This class implement the main window of the portfolio management application
 */
public class PortfolioManagementClient extends Application {
    public static BorderPane rootLayout;
    protected Stage window;
    private Locale languageInfo;
    private ResourceBundle resourceBundle;
    private ArrayList<Portfolio> portfolios = new ArrayList<>();
    private Portfolio currentPortfolio;
    private BorderPane layout = new BorderPane();
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
            loader.setLocation(PortfolioManagementClient.class.getResource(""+file));
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
            Parent root = FXMLLoader.load(PortfolioManagementClient.class.getResource("" + page));
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


    public void setLanguage(Locale languageInfo){
        this.languageInfo = languageInfo;
        resourceBundle = ResourceBundle.getBundle("MainBundle",languageInfo);
    }



    private JSONObject generateSupplyPoint(String ean, String supplierName){
        JSONObject supplyPoint = new JSONObject();
        supplyPoint.put("ean",ean);
        supplyPoint.put("supplier",supplierName);
        return supplyPoint;
    }

    private JSONObject generatePortfolio(String id,int nbSupp, int ean,String name, String admin){
        JSONObject port = new JSONObject();
        JSONArray portfolio = new JSONArray();
        for (int i=0;i < nbSupp;i++){
            portfolio.put(generateSupplyPoint(Integer.toString(ean), "Test"));
            ean = ean+5201;
        }
        port.put("name",name);
        port.put("id",id);
        port.put("admin",admin);
        port.put("portfolio",portfolio);
        return port;

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
    /**
     * Display the window which contain the list of all the portfolio that the user has access
     */
    private void display(){
        BorderPane portfoliosListLayout = new BorderPane();
        portfoliosListLayout.setPadding(new Insets(10,10,10,10));

        // Label
        Label portfolioListLabel = new Label(resourceBundle.getString("ListLabel"));
        portfolioListLabel.setFont(new Font("Arial",18));
        portfoliosListLayout.setTop(portfolioListLabel);

        // List of portfolio
        ListView<String> portfoliosList = new ListView<>();
        for (Portfolio p : portfolios){
            portfoliosList.getItems().add(p.getName());
        }
        portfoliosList.setMaxSize(200,400);
        portfoliosListLayout.setCenter(portfoliosList);

        // Button below the list
        HBox portfolioListButton = new HBox();
        portfolioListButton.setSpacing(20);

        // Create a portfolio button
        Button createButton = new Button(resourceBundle.getString("createPortfolioButton"));
        createButton.setMinSize(100,20);

        // Select portfolio button
        Button selectButton = new Button(resourceBundle.getString("selectButton"));
        selectButton.setMinSize(100,20);
        selectButton.setOnAction(e->displayPortfolio(portfoliosList.getSelectionModel().getSelectedItem()));

        portfolioListButton.getChildren().addAll(createButton,selectButton);
        portfoliosListLayout.setBottom(portfolioListButton);

        layout.setLeft(portfoliosListLayout);

        Scene scene = new Scene(layout,900,500);
        window.setScene(scene);
        window.setMinHeight(500);
        window.setMinWidth(900);
        window.setResizable(false);
        window.show();
    }


    /**
     * Display the information of the selected portfolio
     * @param name The name of the portfolio
     */
    private void displayPortfolio(String name){
        // Search the portfolio
        for (Portfolio p : portfolios){
            if (p.getName().equals(name)){
                currentPortfolio = p;
            }
        }

        // Portfolio layout
        BorderPane portfolioDisplay = new BorderPane();
        portfolioDisplay.setPadding(new Insets(10,10,10,10));
        portfolioDisplay.setStyle("-fx-border-width: 1;" +
                "-fx-border-insets: 2;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: black;" );


        // Information of the portfolio (name, id, admin)
        VBox infoPortfolio = new VBox();

        // Portfolio name
        String portfolioName = resourceBundle.getString("portfolioName") +" : " + currentPortfolio.getName();
        Label nameLabel = new Label(portfolioName);
        nameLabel.setFont(new Font("Arial",14));

        // Portfolio id
        String portfolioId = "Id : " + currentPortfolio.getId();
        Label portfolioIdLabel = new Label(portfolioId);
        portfolioIdLabel.setFont(new Font("Arial",14));

        // Portfolio admin
        String admin = resourceBundle.getString("admin") + " : " + currentPortfolio.getAdmin();
        Label adminLabel = new Label(admin);
        adminLabel.setFont(new Font("Arial",14));

        infoPortfolio.getChildren().addAll(nameLabel,portfolioIdLabel,adminLabel);
        portfolioDisplay.setTop(infoPortfolio);

        // List of the supply point of the portfolio
        BorderPane supplyList = new BorderPane();
        supplyList.setPadding(new Insets(30,5,5,5));

        Label supplyListLabel = new Label(resourceBundle.getString("ListSupplyLabel"));
        supplyListLabel.setFont(new Font("Arial",16));
        supplyList.setTop(supplyListLabel);

        // List of supply point
        ListView<String> supplyPointList = new ListView<>();
        for (SupplyPoint s : currentPortfolio.getSupplyPoints()){
            supplyPointList.getItems().add(s.getEan());
        }
        supplyPointList.setMaxSize(200,300);
        supplyList.setCenter(supplyPointList);

        // Button under the list
        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);
        // Add supply point button
        Button addSupplyPointButton = new Button(resourceBundle.getString("addButton"));
        addSupplyPointButton.setMinSize(80,20);

        // Select supply point button
        Button selectSupplyPointButton = new Button(resourceBundle.getString("selectButton"));
        selectSupplyPointButton.setMinSize(80,20);
        selectSupplyPointButton.setOnAction(e-> {
            String eanSelected = supplyPointList.getSelectionModel().getSelectedItem();
            for (SupplyPoint s : currentPortfolio.getSupplyPoints()){
                if (s.getEan().equals(eanSelected))
                    displaySupplyPoint(portfolioDisplay,s);
            }

        });
        buttonBox.getChildren().addAll(addSupplyPointButton,selectSupplyPointButton);
        supplyList.setBottom(buttonBox);

        portfolioDisplay.setLeft(supplyList);

        // Admin button
        VBox adminButtonBox = new VBox();
        adminButtonBox.setPadding(new Insets(50,10,10,10));
        adminButtonBox.setSpacing(20);

        // Delete portfolio button
        Button deletePortfolioButton = new Button(resourceBundle.getString("deleteButton"));
        deletePortfolioButton.setMinSize(100,20);

        // Invite button
        Button inviteButton = new Button(resourceBundle.getString("inviteButton"));
        inviteButton.setMinSize(100,20);


        adminButtonBox.getChildren().addAll(inviteButton,deletePortfolioButton);
        portfolioDisplay.setRight(adminButtonBox);


        layout.setCenter(portfolioDisplay);
    }


    /**
     * Display the information of the selected supply point
     * @param borderPane The layout of the portfolio
     * @param supplyPoint The supply point
     */
    private void displaySupplyPoint(BorderPane borderPane, SupplyPoint supplyPoint){
        // Supply point layout
        BorderPane supplyPointLayout = new BorderPane();
        supplyPointLayout.setPadding(new Insets(20,20,20,20));
        supplyPointLayout.setStyle("-fx-border-width: 1;" +
                "-fx-border-insets: 2;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: black;" );

        // Information of the supply point (ean, energy, supplier)
        VBox infoSupplyPoint = new VBox();

        // EAN label
        Label eanLabel = new Label("EAN : " + supplyPoint.getEan());
        eanLabel.setFont(new Font("Arial",14));

        // Energy Label
        Label energyLabel = new Label(resourceBundle.getString("energyLabel") + " : " + supplyPoint.getEnergy());
        energyLabel.setFont(new Font("Arial",14));

        // Supplier Label
        Label supplierLabel = new Label(resourceBundle.getString("supplierLabel")+" : " + supplyPoint.getSupplierName());
        supplierLabel.setFont(new Font("Arial",14));


        infoSupplyPoint.getChildren().addAll(eanLabel,energyLabel,supplierLabel);
        supplyPointLayout.setTop(infoSupplyPoint);
        borderPane.setCenter(supplyPointLayout);
    }


}
