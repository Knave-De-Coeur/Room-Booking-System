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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.json.JSONObject;

/**
 *
 * @author alexa
 */
public class DeleteRoomController implements Initializable {
    
    @FXML
    private Button deleteRoom;
    
    @FXML
    private Button showRms;
    
    @FXML 
    private TableView<Room> roomsTable;
    
    @FXML
    public static ArrayList<Room> rooms;
    
    @FXML
    public static Stage newRoomStage;

    public static void setRooms(ArrayList<Room> rooms) {
        DeleteRoomController.rooms = rooms;
    }
    
    public static Gson gson = new Gson();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Client.endpoint.sendMessage("SendAllRooms");
        } catch (IOException ex) {
            Logger.getLogger(DeleteRoomController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    public void showRooms() {
        for (Room room : rooms) {
            System.out.println("Controller: " + room);
            roomsTable.getItems().add(room);
        }
    }
    
    @FXML
    private void removeRooms() throws Exception {
        // instatiates two different observable lists
        ObservableList<Room> roomsSelected, allItems;
        allItems = roomsTable.getItems(); // one holds all items in table
        roomsSelected = roomsTable.getSelectionModel().getSelectedItems(); // one holds user selected
        
        // list is created to loop and remove from in the database
        List<Room> showing = roomsSelected;
        
        System.out.println(showing.toString());
        
        for (Room room : showing) {
            JSONObject jsonRoom = new JSONObject();
            String roomToSrver = gson.toJson(room);
            System.out.println(roomToSrver);
            jsonRoom.put("DeleteRoom", roomToSrver);
            System.out.println(jsonRoom.toString());
            Client.endpoint.sendMessage(jsonRoom.toString());
        }
        
        roomsSelected.forEach(allItems::remove); // items removed from table
    } // end removeRooms()
    
    // Will simply output that a room was deleted
    @FXML
    public static void success(String success) {
        JFrame frame = Client.getFrame();
        if (frame == null) {
            frame = new JFrame();
        }
        frame.setAlwaysOnTop(true); // makes sure that the dialog pops up over all other windows
        JOptionPane.showMessageDialog(frame, success);
    } // end success
} // end DeleteRoomController class