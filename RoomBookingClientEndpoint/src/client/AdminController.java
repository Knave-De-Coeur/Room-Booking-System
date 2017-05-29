/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import static client.LogginController.stage;
import com.objects.RoomsBookedView;
import com.objects.User;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.json.JSONObject;

/**
 *
 * @author alexa
 */
public class AdminController implements Initializable {
    @FXML
    private Button opnNewRoom;
    
    @FXML
    private Button opnNewModule;
    
    @FXML
    private Button opnNewUser;
    
    @FXML
    private Button opnDltRoom;
    
    @FXML
    private Button opnDltModule;
    
    @FXML
    private Button opnDltUser;
    
    @FXML
    private Button printBookings;
    
    @FXML 
    private Button opnNewBooking; 
    
    public static JSONObject json = new JSONObject();
    
    public static User admin;

    public static void setAdmin(User admin) {
        AdminController.admin = admin;
    }
    
    @FXML
    public static ArrayList<RoomsBookedView> roomsBooked;

    public static void setRoomsBooked(ArrayList<RoomsBookedView> roomsBooked) {
        AdminController.roomsBooked = roomsBooked;
    }
    
    @FXML
    public TableView<RoomsBookedView> bookings; // the table to display all bookings for today for admin
    
    @FXML
    public void addRows() {
        bookings.getItems().clear(); // clears of any bookings before adding new ones
        bookings.getItems().addAll(roomsBooked);
    }
    
    @FXML
    public void openWindow(ActionEvent event) {
        String fxmlFile = "";
        Object source = event.getSource();
        if (source instanceof Button) { 
            Button clickedBtn = (Button) source; // that's the button that was clicked
            System.out.println(clickedBtn.getId()); // prints the id of the button
            Parent root;
            try {
                // New window with to create a new room
                stage = new Stage();
                if (null == clickedBtn.getId()) {
                    //root = null;
                    return;
                } else switch (clickedBtn.getId()) {
                    case "opnNewRoom":
                        root = FXMLLoader.load(getClass().getResource("NewRoom.fxml"));
                        break;
                    case "opnNewModule":
                        root = FXMLLoader.load(getClass().getResource("NewModule.fxml"));
                        break;
                    case "opnNewUser":
                        root = FXMLLoader.load(getClass().getResource("NewUser.fxml"));
                        break;
                        
                    case "opnDltRoom":
                        root = FXMLLoader.load(getClass().getResource("DeleteRoom.fxml"));
                        break;
                    case "opnDltModule":
                        root = FXMLLoader.load(getClass().getResource("DeleteModule.fxml"));
                        break;
                    case "opnDltUser":
                        root = FXMLLoader.load(getClass().getResource("DeleteUser.fxml"));
                        break;
                    case "opnNewBooking":
                        root = FXMLLoader.load(getClass().getResource("BookRoom.fxml"));
                        BookRoomController.setAdmin(this.admin);
                        break;
                    default:
                        root = null;
                        break;
                }

                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                System.err.println(e);
            } // end try catch
        } // end if
        
    }// end openWindow()
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Client.endpoint.sendMessage("RoomsBookedToday");
        } catch (IOException ex) {
            Logger.getLogger(DeleteRoomController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
} // end AdminController class