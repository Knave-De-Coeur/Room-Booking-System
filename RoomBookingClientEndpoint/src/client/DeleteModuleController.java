/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import com.google.gson.Gson;
import com.objects.Module;
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
public class DeleteModuleController implements Initializable {
    @FXML
    private Button deleteModule;
    
    @FXML
    private Button showMdls;
    
    @FXML 
    private TableView<Module> modulesTable;
    
    @FXML
    public static ArrayList<Module> modules;

    public static void setModules(ArrayList<Module> modules) {
        DeleteModuleController.modules = modules;
    }
    
    public static Gson gson = new Gson();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Client.endpoint.sendMessage("SendAllModules");
        } catch (IOException ex) {
            Logger.getLogger(DeleteRoomController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    @FXML
    public void showModules() {
        for (Module module : modules) {
            System.out.println("Controller: " + module);
            modulesTable.getItems().add(module);
        }
    }
    
    @FXML
    private void removeModules() throws Exception {
        // instatiates two different observable lists
        ObservableList<Module> modulesSelected, allModules;
        allModules = modulesTable.getItems(); // one holds all items in table
        modulesSelected = modulesTable.getSelectionModel().getSelectedItems(); // one holds user selected
        
        // list is created to loop and remove from in the database
        List<Module> showing = modulesSelected;
        
        System.out.println(showing.toString());
        
        for (Module module : showing) {
            JSONObject jsonModule = new JSONObject();
            String mdlToSrver = gson.toJson(module);
            System.out.println(mdlToSrver);
            jsonModule.put("DeleteModule", mdlToSrver);
            System.out.println(jsonModule.toString());
            Client.endpoint.sendMessage(jsonModule.toString());
        }
        
        modulesSelected.forEach(allModules::remove); // items removed from table
    } // end 
    
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
}
