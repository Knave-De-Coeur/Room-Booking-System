/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import com.google.gson.Gson;
import com.objects.Room;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.json.JSONObject;

/**
 *
 * @author alexa
 */
public class NewRoomController implements Initializable {
    
    @FXML
    private Button submit;
    
    @FXML
    private TextField roomName;
    
    @FXML
    private TextField roomType;
    
    @FXML
    private TextField numbSeats;
    
    private static Gson gson = new Gson();
    
    @FXML
    public static Stage newRoomStage;
    
    public static Room newRoom;
    
    @FXML 
    public void createRoom() throws IOException {
        String rmName = roomName.getText();
        String rmTyp = roomType.getText();
        int numOfSeats = Integer.parseInt(numbSeats.getText());
        newRoomStage = (Stage) submit.getScene().getWindow(); // will be used to refernce the stage to close
        newRoom = new Room(rmName, rmTyp, numOfSeats);
        JSONObject userMessage = new JSONObject();
        userMessage.put("NewRoom", gson.toJson(newRoom));
        Client.endpoint.sendMessage(userMessage.toString());
    }
    
    /* Upon getting a success message from the server the stage is captured so when
    the user presses ok on the dialog the application will close the current window
    */
    @FXML
    public static void success(String success) {
        JFrame frame = Client.getFrame();
        if (frame == null) {
            frame = new JFrame();
        }
        frame.setAlwaysOnTop(true); // makes sure that the dialog pops up over all other windows
        JOptionPane.showMessageDialog(frame, success);
        Platform.runLater(() -> {
            newRoomStage.close();
        });
    } // end success
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
}