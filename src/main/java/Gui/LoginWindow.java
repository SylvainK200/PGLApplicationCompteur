package Gui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;


/**
 * This class implement the login window
 */
public class LoginWindow {

    private Stage window;
    private final String serverAddress = "http://localhost:8080/";
    private Locale languageInfo;
    private ResourceBundle resourceBundle;
    private PortfolioManagementClient client;
    private boolean isLogin = false;

    private TextField mailField;
    private PasswordField passwordField;
    private Label errorLabel = new Label();


    public LoginWindow(PortfolioManagementClient client){
        this.client = client;
    }


    /**
     * Display the window
     * The application start in french
     */
    public void display(){
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        languageInfo = new Locale("fr","FR");
        resourceBundle = ResourceBundle.getBundle("LoginBundle",languageInfo);
        window.setScene(makeScene());
        window.setResizable(false);
        window.showAndWait();

    }


    /**
     * Create the scene for the login window
     * @return The scene of the login window
     */
    public Scene makeScene(){
        GridPane layout = new GridPane();
        layout.setPadding(new Insets(20,20,20,20));
        layout.setHgap(10);
        layout.setVgap(10);
        // Mail
        Label mailLabel = new Label(resourceBundle.getString("mail"));
        GridPane.setConstraints(mailLabel,0,0);
        mailField = new TextField();
        GridPane.setConstraints(mailField,1,0);

        // Password
        Label passwordLabel = new Label(resourceBundle.getString("password"));
        GridPane.setConstraints(passwordLabel,0,1);
        passwordField = new PasswordField();
        GridPane.setConstraints(passwordField,1,1);

        // Language
        Label languageLabel = new Label(resourceBundle.getString("language"));
        GridPane.setConstraints(languageLabel,0,2);
        ChoiceBox<String> languageBox = new ChoiceBox<>();
        languageBox.getItems().addAll("Français","English");
        languageBox.setValue(resourceBundle.getString("currentLanguage"));
        languageBox.setOnAction(e-> changeLanguage(languageBox.getValue()));
        GridPane.setConstraints(languageBox,1,2);

        // Login Button
        Button loginButton = new Button(resourceBundle.getString("login"));
        loginButton.setOnAction(e-> {
            try {
                login(mailField.getText(), passwordField.getText());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        GridPane.setConstraints(loginButton,1,4);

        // Create an account button
        Button createAccountButton = new Button(resourceBundle.getString("createAccount"));
        createAccountButton.setOnAction(e-> new CreateAccountWindow(languageInfo));
        GridPane.setConstraints(createAccountButton,0,4);

        // error label
        GridPane.setConstraints(errorLabel,0,3);

        layout.getChildren().addAll(mailLabel,mailField,passwordLabel,passwordField,loginButton,createAccountButton,languageLabel,languageBox,errorLabel);

        return new Scene(layout,400,200);
    }


    /**
     * Send the mail and the password to the server
     * @param mail The mail address
     * @param password  The password
     * @throws IOException an exception
     */
    private void login(String mail, String password) throws IOException {
        String url = serverAddress+"user/" + mail+"?password="+password;
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == 200){
            String response = "";
            Scanner scanner = new Scanner(connection.getInputStream());
            while(scanner.hasNextLine()){
                response += scanner.nextLine();
            }
            scanner.close();
            switch (response){
                case "mailException":
                    mailField.clear();
                    passwordField.clear();
                    errorLabel.setText(resourceBundle.getString("mailException"));
                    break;
                case "passwordException":
                    passwordField.clear();
                    errorLabel.setText(resourceBundle.getString("passwordException"));
                    break;
                default:
                    isLogin = true;
                    client.setLanguage(languageInfo);
                    window.close();
            }
        }
    }


    /**
     * Change the language
     * @param language The language to apply (Français or English)
     */
    private void changeLanguage(String language){
        if (language.equals("English")){
            languageInfo = new Locale("en","US");
        }
        else{
            languageInfo = new Locale("fr","FR");
        }
        resourceBundle = ResourceBundle.getBundle("LoginBundle",languageInfo);
        window.setScene(makeScene());
    }

    public boolean isLogin() {
        return isLogin;
    }
}
