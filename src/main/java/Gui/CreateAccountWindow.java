package Gui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This class implement the create account window
 */
public class CreateAccountWindow {

    private ResourceBundle resourceBundle;
    private Stage window = new Stage();

    private TextField mailField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private TextField firstnameField;
    private TextField nameField;

    public CreateAccountWindow(Locale languageInfo){
        resourceBundle = ResourceBundle.getBundle("CreateAccountBundle",languageInfo);
        display();
    }

    private void display(){
        GridPane layout = new GridPane();
        layout.setPadding(new Insets(20,20,20,20));
        layout.setHgap(10);
        layout.setVgap(10);

        Label mailLabel = new Label(resourceBundle.getString("mail"));
        GridPane.setConstraints(mailLabel,0,0);
        mailField = new TextField();
        GridPane.setConstraints(mailField,1,0);

        Label passwordLabel = new Label(resourceBundle.getString("password"));
        GridPane.setConstraints(passwordLabel,0,1);
        passwordField = new PasswordField();
        GridPane.setConstraints(passwordField,1,1);

        Label confirmPasswordLabel = new Label(resourceBundle.getString("confirmPassword"));
        GridPane.setConstraints(confirmPasswordLabel,0,2);
        confirmPasswordField = new PasswordField();
        GridPane.setConstraints(confirmPasswordField,1,2);

        Label firstnameLabel = new Label(resourceBundle.getString("firstname"));
        GridPane.setConstraints(firstnameLabel,0,3);
        firstnameField = new TextField();
        GridPane.setConstraints(firstnameField,1,3);

        Label nameLabel = new Label(resourceBundle.getString("name"));
        GridPane.setConstraints(nameLabel,0,4);
        nameField = new TextField();
        GridPane.setConstraints(nameField,1,4);


        Button createButton = new Button(resourceBundle.getString("create"));
        createButton.setOnAction(e-> {
            try {
                createAccount();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        GridPane.setConstraints(createButton,1,6);

        Button cancelButton = new Button(resourceBundle.getString("cancel"));
        cancelButton.setOnAction(e-> window.close());
        GridPane.setConstraints(cancelButton,0,6);

        layout.getChildren().addAll(
                mailLabel,mailField,
                passwordLabel,passwordField,
                confirmPasswordLabel,confirmPasswordField,
                firstnameLabel,firstnameField,
                nameLabel,nameField,
                createButton,cancelButton);
        Scene scene = new Scene(layout,400,300);

        window.initModality(Modality.APPLICATION_MODAL);
        window.setScene(scene);
        window.showAndWait();
    }

    private void createAccount() throws IOException {
        boolean canBeCreate = true;
        String mail = mailField.getText();
        if (! checkMail(mail)){
            canBeCreate = false;
            mailField.clear();
        }
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        if (! checkPassword(password,confirmPassword)){
            canBeCreate = false;
            passwordField.clear();
            confirmPasswordField.clear();
        }
        String firstname = firstnameField.getText();
        String name = nameField.getText();
        if (! checkOtherField(firstname) || ! checkOtherField(name)){
            canBeCreate = false;
        }

        if (canBeCreate){
            postAccount(mail,password,firstname,name);
        }
    }

    private boolean checkMail(String mail){
        Pattern mailPattern = Pattern.compile("[a-zA-Z0-9\\.]+@[a-z\\.]+");
        Matcher matcher = mailPattern.matcher(mail);
        return matcher.matches();
    }

    private boolean checkPassword(String password, String confirmPassword){
        if (password.equals("") || confirmPassword.equals(""))
            return false;
        return password.equals(confirmPassword);
    }

    private boolean checkOtherField(String value){
        return ! value.equals("");
    }

    private void postAccount(String mail, String password, String firstname, String name) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8080/users").openConnection();
        connection.setRequestMethod("POST");

        connection.setDoOutput(true);

        JSONObject data = new JSONObject();
        data.put("mail",mail);
        data.put("password",password);
        data.put("firstname",firstname);
        data.put("name",name);
        String dataString = data.toString();
        System.out.println(dataString);
        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
        writer.write(dataString);
        writer.flush();

        int respondCode = connection.getResponseCode();
        if (respondCode==200){
            InfoWindow.display("Account created");
            window.close();
        }
        else{
            InfoWindow.display("Error");
        }
    }
}
