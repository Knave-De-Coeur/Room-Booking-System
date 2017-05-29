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
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.json.JSONObject;

/**
 *
 * @author alexa
 */
public class DeleteUserController implements Initializable {
    @FXML
    private Button deleteUser;
    
    @FXML
    private Button shwUsrs;
    
    @FXML 
    private TableView<User> usersTable;
    
    @FXML
    public static ArrayList<User> users;

    public static void setUsers(ArrayList<User> users) {
        DeleteUserController.users = users;
    }
    
    public static Gson gson = new Gson();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Client.endpoint.sendMessage("SendAllUsers");
        } catch (IOException ex) {
            Logger.getLogger(DeleteRoomController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    public void showUsers() {
        for (User user : users) {
            System.out.println("Controller: " + user);
            usersTable.getItems().add(user);
        }
    }
    
    @FXML
    private void removeUsers() throws Exception {
        // instatiates two different observable lists
        ObservableList<User> usersSelected, allUsers;
        allUsers = usersTable.getItems(); // one holds all items in table
        usersSelected = usersTable.getSelectionModel().getSelectedItems(); // one holds user selected
        
        // list is created to loop and remove from in the database
        List<User> showing = usersSelected;
        
        System.out.println(showing.toString());
        
        for (User user : showing) {
            JSONObject jsonUser = new JSONObject();
            String usrToSrver = gson.toJson(user);
            System.out.println(usrToSrver);
            jsonUser.put("DeleteUser", usrToSrver);
            System.out.println(jsonUser.toString());
            Client.endpoint.sendMessage(jsonUser.toString());
        }
        
        usersSelected.forEach(allUsers::remove); // items removed from table
    } // end removeModules
    
    // Will simply output that a room was deleted
    @FXML
    public static void success(String success) {
        JFrame frame = Client.getFrame(); // uses the main class's frame instance
        if (frame == null) {
            frame = new JFrame(); // creates new one when null
        }
        frame.setAlwaysOnTop(true); // makes sure that the dialog pops up over all other windows
        JOptionPane.showMessageDialog(frame, success); // outputs message passed in parameters
    } // end success
    
} // end DeletUserController class
