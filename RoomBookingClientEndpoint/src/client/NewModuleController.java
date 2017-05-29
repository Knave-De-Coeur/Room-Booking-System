/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import static client.NewUserController.frame;
import com.google.gson.Gson;
import com.objects.Module;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.json.JSONObject;

/**
 *
 * @author alexa
 */
public class NewModuleController implements Initializable {
    
    @FXML
    private Button submit;
    
    @FXML
    private TextField moduleName;
    
    @FXML
    private TextArea moduleDesc;
    
    @FXML
    private TextField moduleLeader;
    
    @FXML
    private TextField Lecturer;
    
    @FXML
    public static Stage newmoduleStage;
    
    private static Gson gson = new Gson();
    
    public static Module newModule;
    
    @FXML 
    public void createModule() throws IOException {
        String name = moduleName.getText();
        String desc = moduleDesc.getText();
        String leader = moduleLeader.getText();
        String username = Lecturer.getText();
        String lectUsername = Lecturer.getText();
        newmoduleStage = (Stage) submit.getScene().getWindow(); // will be used to refernce the stage to close
        newModule = new Module(name, desc, leader, lectUsername);
        JSONObject userMessage = new JSONObject();
        System.out.println(newModule.toString());
        userMessage.put("NewModule", gson.toJson(newModule));
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
            newmoduleStage.close();
        });
    } // end success
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
} // end new Module Class
