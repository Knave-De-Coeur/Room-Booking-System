/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import static client.NewUserController.frame;
import com.google.gson.Gson;
import com.objects.AdminBookingInput;
import com.objects.User;
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
public class BookRoomController implements Initializable {
    
    @FXML
    private Button submitBooking;
    
    @FXML
    private TextField roomName;
    
    @FXML
    private TextField timeSlot;
    
    @FXML
    private TextField moduleName;
    
    @FXML
    public static Stage bookroomStage;
    
    private static Gson gson = new Gson();
    
    public static User admin;
    
    public static AdminBookingInput newBooking;

    public static void setAdmin(User admin) {
        BookRoomController.admin = admin;
    }
    
    @FXML 
    public void createBooking() throws IOException {
        String room = roomName.getText();
        String timeslot = timeSlot.getText();
        String mdlname = moduleName.getText();
        bookroomStage = (Stage) submitBooking.getScene().getWindow(); // will be used to refernce the stage to close
        newBooking = new AdminBookingInput(room, timeslot, mdlname, admin.getId());
        JSONObject userMessage = new JSONObject();
        System.out.println(newBooking.toString());
        userMessage.put("NewBooking", gson.toJson(newBooking));
        Client.endpoint.sendMessage(userMessage.toString());
    }
    
    /* Upon getting a success message from the server the stage is captured so when
    the user presses ok on the dialog the application will close the current window
    */
    @FXML
    public static void success(String success) throws IOException {
        if (frame == null) {
            frame = new JFrame();
        }
        frame.setAlwaysOnTop(true); // makes sure that the dialog pops up over all other windows
        JOptionPane.showMessageDialog(frame, success);
        Platform.runLater(() -> {
            bookroomStage.close();
        });
        Client.endpoint.sendMessage("RoomsBookedToday");
    } // end success
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
}// end BookRoomController

