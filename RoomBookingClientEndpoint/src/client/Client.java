/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.IOException;
import java.net.URISyntaxException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.swing.JFrame;
import javax.websocket.DeploymentException;

/**
 *
 * @author alexa
 */
public class Client extends Application {
    
    public static ClientEnd endpoint;
    
    public static JFrame frame;

    public static ClientEnd getEndpoint() {
        return endpoint;
    }

    public static void setEndpoint(ClientEnd endpoint) {
        Client.endpoint = endpoint;
    }

    public static JFrame getFrame() {
        return frame;
    }

    public static void setFrame(JFrame frame) {
        Client.frame = frame;
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Room Booking System");// Chooses what FXML file to load up forst
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        
        Scene scene = new Scene(root); // sets the FXML file as the scene
        
        stage.setScene(scene);
        stage.show();
    }
    
    @Override
    public void stop() {
        System.exit(0);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws URISyntaxException, DeploymentException, IOException {
        endpoint = new ClientEnd();
        launch(args);
    }
    
}
