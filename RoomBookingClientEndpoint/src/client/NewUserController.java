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
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.json.JSONObject;

/**
 *
 * @author alexa
 */
public class NewUserController implements Initializable{
    
    @FXML
    private Button submit;
    
    @FXML
    private TextField name;
    
    @FXML
    private TextField surname;
    
    @FXML
    private TextField username;
    
    @FXML
    private TextField password;
    
    @FXML
    private RadioButton admin;
    
    private static Gson gson = new Gson();
    
    public static JFrame frame;
    
    @FXML
    public static Stage stage;
    
    public static User newUser;
    
    @FXML 
    public void createUser() throws IOException {
        String fname = name.getText();
        String lname = surname.getText();
        String usrnm = username.getText();
        String pass = password.getText();
        boolean ad = admin.isSelected();
        stage = (Stage) submit.getScene().getWindow(); // will be used to refernce the stage to close
        newUser = new User(fname, lname, usrnm, pass, ad);
        JSONObject userMessage = new JSONObject();
        userMessage.put("NewUser", gson.toJson(newUser));
        Client.endpoint.sendMessage(userMessage.toString());
    }
    
    /* Upon getting a success message from the server the stage is captured so when
    the user presses ok on the dialog the application will close the current window
    */
    @FXML
    public static void success(String success) {
        if (frame == null) {
            frame = new JFrame();
        }
        frame.setAlwaysOnTop(true); // makes sure that the dialog pops up over all other windows
        JOptionPane.showMessageDialog(frame, success);
        Platform.runLater(() -> {
            stage.close();
        });
    } // end success
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    } 
    
} // NewUserController class 