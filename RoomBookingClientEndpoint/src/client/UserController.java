/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import com.objects.User;
import com.objects.UsersBookedRooms;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.json.JSONObject;

/**
 *
 * @author alexa
 */
public class UserController implements Initializable {
    
    public static Stage userStage;
    
    public static User user;
    
    @FXML
    private Button showYourBookings;
    
    @FXML
    public static ArrayList<UsersBookedRooms> usersBookedRooms;
    
    @FXML
    public TableView<UsersBookedRooms> yourBookings; // the table to display all bookings for today
    
    public static void setUsersBookedRooms(ArrayList<UsersBookedRooms> usersBookedRooms) {
        UserController.usersBookedRooms = usersBookedRooms;
    }

    public static void setUser(User user) {
        UserController.user = user;
    }
    
    @FXML
    public void addYourRows() {
        yourBookings.getItems().clear(); // clears of any bookings before adding new ones
        yourBookings.getItems().addAll(usersBookedRooms);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            JSONObject rplyToServer = new JSONObject();
            rplyToServer.put("MyRoomsBookedForToday", user.getUsername()); // creates key valu pair to send to server
            System.out.println(rplyToServer);
            Client.endpoint.sendMessage(rplyToServer.toString()); // sends request to server for all the rooms booked today
            
        } catch (IOException ex) {
            Logger.getLogger(DeleteRoomController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }// end initialize  
    
}// end UserControllerClass