/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.objects.Module;
import com.objects.Room;
import com.objects.RoomsBookedView;
import com.objects.User;
import com.objects.UsersBookedRooms;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import org.json.JSONObject;
import javax.websocket.ClientEndpoint;
import javax.websocket.OnClose;
import org.json.JSONException;

@ClientEndpoint
public class ClientEnd {
    public JFrame frame;
    
    public Session session = null;
    
    public Gson gson = new Gson();
    
    public static JSONObject json2Server = new JSONObject();
    
    public ClientEnd() throws URISyntaxException, DeploymentException, IOException {
        URI uri = URI.create("ws://localhost:8080/RoomBookingServerEndpoint/endpoint");
        ContainerProvider.getWebSocketContainer().connectToServer(this, uri);
        System.out.println("Connection Opened");
    }
    
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
    }
    
    @OnClose
    public void onClose() {
        error("The server has shut down");
    }
    
    /* recieves messages from server and depending on what message type, or what the message
    contains will carry out a specific function for the client
    */
    @OnMessage
    public void onMessage(String Message) throws IOException, Exception {
        System.out.println("Message Recieved from server: " + Message);
        JSONObject jObject  = isJSONValid(Message);
        // if it is a json then onMesage will handle one of the possibilities it will function with
        if (jObject != null) {
            // Checks if there is a certain string command and performs the appropriate function
            if (jObject.has("User") || jObject.has("Admin")) {
                returnClientFromDatabase(jObject); // return comleteclient and ovrrites the client instance in loggin
                
            } 
            // When the server responds with all the rooms Booked today
            else if (jObject.has("RoomsBookedToday")) {
                transfRoomsbkd(jObject); // sends todays rooms booked from the database to the admin controller to display
                
            } else if (jObject.has("YourRoomsBookedToday")) {
                transfUsrsRoomsBooked(jObject); // sends user-specific booked rooms so that user can view their booked rooms
                
            } else if (jObject.has("AllRooms")) {
                transferRooms(jObject); // sends all the rooms from the database to the controller
                        
            } else if (jObject.has("AllModules")) {
                transferModules(jObject); // sends all modules from the database to the controller
                
            } else if (jObject.has("AllUsers")) {
                transferUsers(jObject); // sends all the users from the database to the controller
                
            } else if (jObject.has("Error")) {
                String errorMsg = jObject.getString("Error"); // gets the value of the error 
                error(errorMsg); // displays the error
                //Client.showError("Error", errorMsg);
            }
        } // displays the success messages sent from the server endpoint
        else if (Message.equals("User Successfully Stored")) {
            NewUserController.success(Message); 
            
        } else if (Message.equals("User Successfully Deleted!")) {
            DeleteUserController.success(Message);
            
        } else if (Message.equals("Room Successfully Stored")) {
            NewRoomController.success(Message);
            
        } else if (Message.equals("Room Successfully Deleted!")) {
            DeleteRoomController.success(Message);
            
        } else if (Message.equals("Module Successfully Stored!")) {
            NewModuleController.success(Message);
            
        } else if (Message.equals("Module Successfully Deleted!")) {
            DeleteModuleController.success(Message);
            
        } else if (Message.equals("Booking Successfully Made")) {
            BookRoomController.success(Message);
            
        }
        else {
            System.out.println(Message); // simply prints out message contenet
        } // end else if
    } // end onMessage
    
    // This will send whatever string is passed into args to server
    public void sendMessage(String msg) throws IOException {
        session.getBasicRemote().sendText(msg);
    } // end sendMessage
    
    // Checks weather admin or user was returned, stores them and requests server
    public void returnClientFromDatabase(JSONObject jObject) throws IOException {
        String msgFromServer = "";
        User myUser = null;
        /* If a regular user is returned then the user is stored and send a message to the server for their
            rooms booked. A JSON message containing the order to carry out as key and the username of the user returned 
        */
        if (jObject.has("User")) {
            msgFromServer = jObject.getString("User"); // gets the value of the key "User"
            myUser = gson.fromJson(msgFromServer, User.class);// converts it to user object
            UserController.setUser(myUser); // sets the static instance in UserController
            
        } 
        // If its and admin simply stores admin as object and requests all the bookings with a single string 
        else if (jObject.has("Admin")) {
            msgFromServer = jObject.getString("Admin"); // gets the value of the key "Admin"
            myUser = gson.fromJson(msgFromServer, User.class); // converts it to user object
            AdminController.setAdmin(myUser);// set the static instance in AdminController
            
        } // end else if 
        /* sets the object in the client controller which will also switch the scene of the stage
        depending on the value of their admin attribute
        */
        LogginController.setUser(myUser); 
    } // end returnClientFromDatabase
    
    // Will be executed after user has been set and the scene is switched
    public void transfRoomsbkd(JSONObject jObject) {
        String message = jObject.getString("RoomsBookedToday");
        Type type = new TypeToken<ArrayList<RoomsBookedView>>() {}.getType();
        ArrayList<RoomsBookedView> bookingsRecieved = gson.fromJson(message, type);
        AdminController.setRoomsBooked(bookingsRecieved);
        //LogginController.setRoomsBooked(bookingsRecieved);
        //LogginController.switchView();
    }
    
    // will simply store the users rooms booked in the controller object
    public void transfUsrsRoomsBooked(JSONObject usersRooms) {
        String message = usersRooms.getString("YourRoomsBookedToday");
        Type type = new TypeToken<ArrayList<UsersBookedRooms>>() {}.getType();
        ArrayList<UsersBookedRooms> allBookedRooms = gson.fromJson(message, type);
        for (UsersBookedRooms room : allBookedRooms) {
            System.out.println(room);
            //ClientController.bookings.getItems().add(room);
        }
        UserController.setUsersBookedRooms(allBookedRooms);
        //LogginController.usersBookedRooms = allBookedRooms;
        //LogginController.switchView();
    }
    
    // will get the json version of the rooms and send it to the DeleteRoomsController object to display
    public void transferRooms(JSONObject allRooms) {
        System.out.println("Recieved from server, rooms: " + allRooms);
        String rooms = allRooms.getString("AllRooms");
        Type type = new TypeToken<ArrayList<Room>>() {}.getType();
        ArrayList<Room> roomsFromServer = gson.fromJson(rooms, type);
        
        DeleteRoomController.setRooms(roomsFromServer);
        //DeleteRoomController.displayRooms();
        //ClientController.usersBookedRooms = usersBookedRooms;
        //ClientController.switchView();
    } // end transferRooms
    
    // Return weather or not the variable passed is json in its json object
    public JSONObject isJSONValid(String msg) {
        System.out.println("Checking if it is valida json");
        JSONObject Object;
        try {
             Object = new JSONObject(msg);
        } catch (JSONException ex) {
            return null;
        }
        return Object;
    } // end isJSONvalid

    // Will send Modules to Delete Modules controller
    private void transferModules(JSONObject allModules) {
        System.out.println("Recieved from server, rooms: " + allModules);
        String modules = allModules.getString("AllModules");
        Type type = new TypeToken<ArrayList<Module>>() {}.getType();
        ArrayList<Module> modulesFromServer = gson.fromJson(modules, type);
        
        DeleteModuleController.setModules(modulesFromServer);
    } // end transferModules()
    
    // Will send the users recieved to the delet users controller
    private void transferUsers(JSONObject jObject) {
        String message = jObject.getString("AllUsers");
        Type type = new TypeToken<ArrayList<User>>() {}.getType();
        ArrayList<User> usersRecieved = gson.fromJson(message, type);
        for (User user : usersRecieved) {
            System.out.println(user);
            //ClientController.bookings.getItems().add(room);
        }
        DeleteUserController.setUsers(usersRecieved);
    } // end transfer students
    
    private void error(String err) {
        if (frame == null) {
            frame = new JFrame();
        }
        frame.setAlwaysOnTop(true);
        JOptionPane.showMessageDialog(frame, err, "Error", JOptionPane.ERROR_MESSAGE);
    } // end error
    
} // end ClientEnd class