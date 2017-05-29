/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import com.google.gson.Gson;
import com.objects.User;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONObject;

/**
 *
 * @author alexa
 */
public class LogginController implements Initializable {
    
    @FXML
    private Button login;
    
    @FXML 
    private Label loginErr;
    
    @FXML
    private TextField username;
    
    @FXML 
    private PasswordField password;
    
    public static User user;
    
    public static JSONObject json = new JSONObject();
    
    public static Gson gson = new Gson();
    
    public static boolean onLogin = true;
    
    public static Stage stage;
    
    
    
    @FXML
    private void login() throws IOException {
        if (username.getText().isEmpty() || password.getText().isEmpty()) {
            loginErr.setText("Please Fill out all the fields");
            loginErr.setVisible(true);
        } else {
            System.out.println("Logging in");
            String usrname = username.getText();
            String pass = password.getText();
            user = new User(usrname, pass);
            json.put("UserLoggingin", gson.toJson(user));
            stage = (Stage) login.getScene().getWindow();
            Client.endpoint.sendMessage(json.toString());
        } // end if else
    } // end login()

    public static void setUser(User user) {
        LogginController.user = user;
        switchView();
    }
    
    /* Since this method needs to be accessed from another class and to avoid null pointer, and keep 
        variables non-static Platform.run later is used
        UI can only be updated from the application thread.
    */
    @FXML
    public static void switchView() {
        Platform.runLater(
            () -> {
                Parent root;
                try {
                    System.out.println("User is: " + user.toString());
                    // will set the root as the main menu
                    if (onLogin && user.isAdmin()) {
                        root = FXMLLoader.load(LogginController.class.getResource("AdminPage.fxml"));
                        onLogin = false;
                    } else if ( onLogin && !(user.isAdmin()) ){
                        // root gets set back to login fxml 
                        root = FXMLLoader.load(LogginController.class.getResource("UserPage.fxml"));
                        onLogin = false;
                    } else {
                        root = FXMLLoader.load(LogginController.class.getResource("Login.fxml"));
                        onLogin = true;
                    }// end else if
                    // sets scene depending on what root was 
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();// shows new scene
                } catch (IOException e) {
                    System.err.println(e);
                }// end try catch
            });
    }// end switchView()
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
}// end clientController class