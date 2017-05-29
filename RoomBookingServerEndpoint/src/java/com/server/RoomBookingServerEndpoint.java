package com.server;

import com.DTOs.BookedRoomsViewMan;
import com.DTOs.ModuleManagement;
import com.DTOs.RoomsBookedManagement;
import com.DTOs.RoomsManagement;
import com.DTOs.TimeSlotManagement;
import com.DTOs.UserManagement;
import com.DTOs.UsersBookedRoomMan;
import com.google.gson.Gson;
import com.objects.AdminBookingInput;
import com.objects.Module;
import com.objects.Room;
import com.objects.RoomBooked;
import com.objects.RoomsBookedView;
import com.objects.TimeSlot;
import com.objects.User;
import com.objects.UsersBookedRooms;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.json.JSONException;
import org.json.JSONObject;

@ServerEndpoint("/endpoint")
public class RoomBookingServerEndpoint {
    
    static Set<Session> users = Collections.synchronizedSet(new HashSet<Session>());
    static Gson gson = new Gson();
    
    /**
     * @OnOpen allows us to intercept the creation of a new session.
     * The session class allows us to send data to the user.
     * In the method onOpen, we'll let the user know that the handshake was 
     * successful.
     */
    @OnOpen
    public void OnOpen(Session userSession) {
        users.add(userSession);
        System.out.println(userSession.getId() + "has connected");
    }
    
    // User closes connection
    @OnClose
    public void onClose(Session userSession){
        System.out.println("Session " +userSession.getId()+" has ended");
        if (users.contains(userSession)) {
            users.remove(userSession);
        }
    }
    
    /* When a user sends a message to the server, this method will intercept the message
    determine what the message contains and will execute a function that will repod to that
    client with a message containing data
    */
    
    @OnMessage
    public void onMessage(String userMessage, Session userSession) throws SQLException, IOException, Exception {
        System.out.println("Message recieved from client: " + userMessage);
        // First checks if the message is a json or a string
        JSONObject jObject  = isJSONValid(userMessage);
        /* if it is a json then the key value contains an object or array of objects and functions 
        will be met depending on what key is present
        */
        if (jObject != null) {
            // this condition means that the user object needs to be saved and check before returned as a whole
            if (jObject.has("UserLoggingin")) {
                checkUser(jObject, userSession); // check if data sentand will send back full user or error
                
            } else if (jObject.has("NewUser")) {
                storeUser(userSession, jObject); // Stores new user in database
                
            } else if (jObject.has("NewRoom")) {
                storeRoom(userSession, jObject); // Stores new room in database
                
            } else if (jObject.has("NewModule")) {
                storeModule(userSession, jObject); // Stores new module in database
                
            } else if (jObject.has("NewBooking")) {
                createBooking(userSession, jObject); // Stores new room in database
                
            } else if (jObject.has("MyRoomsBookedForToday")) {
                sendUserTheirBkRms(userSession, jObject); // Uses the object to filter the rooms sent to client
                
            } else if (jObject.has("DeleteRoom")) {
                dltRmsFrmDatabase(userSession, jObject); // Deletes the room based on room object sent
                
            } else if (jObject.has("DeleteModule")) {
                dltMdlsFrmDatabase(userSession, jObject); // Deletes the module based on module object sent
                
            } else if (jObject.has("DeleteUser")) {
                dltUsrsFrmDatabase(userSession, jObject); // Deletes the user based on the user object sent 
                
            } // end else if
        } 
        // if it is a string it will execute the function based on that string
         else if (userMessage.equals("RoomsBookedToday")) {
            sendAllRoomsBooked(userSession); // sends all the rooms booked today to admin
            
        } else if (userMessage.equals("SendAllRooms")) {
            sendRoomsToAdmin(userSession); // sends all the rooms stored in database
            
        } else if (userMessage.equals("SendAllModules")) {
            sendModulesToAdmin(userSession); // sends all the rooms stored in the database 
            
        } else if (userMessage.equals("SendAllUsers")) {
            sendUsersToAdmin(userSession); // sends all the users stored ub the database
            
        }// end else if
    } // end onMessage 
    
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
    } // end isJSONValid
    
    // Makes sure the user is in the database and will return to the client on that dependency 
    public void checkUser(JSONObject usrJSONMsg, Session userSession) throws SQLException, IOException {
        // gets user in json format without key then parse that into user object
        String usrJson = usrJSONMsg.getString("UserLoggingin"); 
        User sentUsr = gson.fromJson(usrJson, User.class);
        System.out.println(sentUsr);
        // Checks if there is a user by that username
        boolean valid = UserManagement.checkExists(sentUsr);
        JSONObject send2User = new JSONObject(); // required to transfer data to user
        if (valid) {
            // Gets the user from the database
            User cmpltUsr = UserManagement.getUser(sentUsr.getUsername(), sentUsr.getPassword());
            System.out.println(cmpltUsr);
            // Depending on weather or not the user logged in is admin will return the client endpoint a particular message
            if (cmpltUsr.isAdmin()) {
                send2User.put("Admin", gson.toJson(cmpltUsr));
            } else {
                send2User.put("User", gson.toJson(cmpltUsr));
            }
            userSession.getBasicRemote().sendText(send2User.toString());
        } else {
            send2User.put("Error", "The username or password is invalid please try again");
            userSession.getBasicRemote().sendText(send2User.toString());
        }// end if
    } // end checkUser
    
    public void storeUser(Session userSession, JSONObject newUser) throws Exception {
        String message = newUser.getString("NewUser");
        User userToStore = gson.fromJson(message, User.class);
        if (UserManagement.insertUser(userToStore)) {
            userSession.getBasicRemote().sendText("User Successfully Stored");
        } // end if
    } // end storeUser
    
    public void storeModule(Session userSession, JSONObject newModule) throws Exception {
        // First gets value of key pair and is a string in json format of the module object created by the admin
        String message = newModule.getString("NewModule"); 
        
        JSONObject moduleJson = new JSONObject(message); // JSON object is pure module object returned
        
        /* next we want to find a user based on the username passed and then remove the usernaem attribute 
        from the json before converting it to an object
        */
        String username = moduleJson.getString("username"); // gets username value from the moduleJson
        int userId = UserManagement.findUserbyusername(username); // finds user and stores it
        System.out.println("userId found and to be stored in object: " + userId);
        moduleJson.remove("username"); // gets rid of the string username since we grabbed the id
        
        // Finally we covert the moduleJson to module object then set its userid attribute
        Module modToStore = gson.fromJson(moduleJson.toString(), Module.class); 
        modToStore.setUserId(userId); // sets the module userid as the int stored earlier
        System.out.println(modToStore.toString());
        if (ModuleManagement.insertModule(modToStore)) {
            userSession.getBasicRemote().sendText("Module Successfully Stored!");
        } // end if
    } // end storeModule
    
    // will convert room json into object and store it in mysql
    public void storeRoom(Session userSession, JSONObject newModule) throws Exception {
        String message = newModule.getString("NewRoom");
        Room roomToStore = gson.fromJson(message, Room.class);
        if (RoomsManagement.insertRoom(roomToStore)) {
            userSession.getBasicRemote().sendText("Room Successfully Stored");
        } // end if
    } // end storeRoom
    
    public void sendUserTheirBkRms (Session userSession, JSONObject urnmJson) throws IOException, SQLException {
        String username = urnmJson.getString("MyRoomsBookedForToday"); // stores username in string
        System.out.println(username);
        // extracts from database and stores in arraylist depending on the username, the rooms booked for that user
        ArrayList<UsersBookedRooms> usersBookedRooms = UsersBookedRoomMan.getRoomsBookedForToday(username); 
        // simply outputs what was returned from databse
        for (UsersBookedRooms room : usersBookedRooms) {
            System.out.println(room);
        }
        //Converts to json with message
        JSONObject send2User = new JSONObject();
        // creates a json object with the json user and a message 'User' to implement a function on the client
        send2User.put("YourRoomsBookedToday", gson.toJson(usersBookedRooms));
        System.out.println(send2User.toString());
        userSession.getBasicRemote().sendText(send2User.toString()); // send userclient their rooms booked for today
    }
    
    // Simply grabs all the rooms in the database as sends it to the requester
    public void sendRoomsToAdmin (Session userSession) throws IOException, SQLException {
        System.out.println("Getting rooms");
        ArrayList<Room> rooms = RoomsManagement.getAllRooms();
        for (Room room : rooms) {
            System.out.println(room.toString());
        }
        JSONObject jsonRooms = new JSONObject();
        jsonRooms.put("AllRooms", gson.toJson(rooms)); // converts all the rooms to json with message to admin client
        System.out.println("Sending rooms to admin");
        userSession.getBasicRemote().sendText(jsonRooms.toString());
    } // end sendRoomsToAdmin
    
    // Deletes the room selected by the admin and its foreign key constraints
    public void dltRmsFrmDatabase (Session userSession, JSONObject jsonRoom) throws Exception {
        System.out.println("Deleting Room...");
        // First gets value of key pair and stores in a string in json format of the room object admin wants to delete
        String message = jsonRoom.getString("DeleteRoom"); 
        
        System.out.println(message);
        
        Room roomToDlt = gson.fromJson(message, Room.class);
        System.out.println(roomToDlt.getId());
        
        // Now we want to remove all the foerign key constraints of this room in roombookings
        RoomsBookedManagement.deleteByRoom(roomToDlt.getId());
        
        if (RoomsManagement.deleteRoom(roomToDlt.getId())) {
            userSession.getBasicRemote().sendText("Room Successfully Deleted!");
        } // end if
    } // end dltRmsFrmDataBase
    
    // Simply grabs all the Modules in the database as sends it to the requester
    public void sendModulesToAdmin (Session userSession) throws IOException, SQLException {
        ArrayList<Module> modules = ModuleManagement.getAllModules(); // stores all the modules from db to this array
        
        JSONObject jsonModules = new JSONObject();
        jsonModules.put("AllModules", gson.toJson(modules)); // converts all the rooms to json with message to admin client
        System.out.println("Sending modules to admin");
        
        userSession.getBasicRemote().sendText(jsonModules.toString());
    }
    
    // Deletes the room selected by the admin and its foreign key constraints
    public void dltMdlsFrmDatabase (Session userSession, JSONObject jsonRoom) throws Exception {
        System.out.println("Deleting Module...");
        // First gets value of key pair and stores in a string in json format of the room object admin wants to delete
        String message = jsonRoom.getString("DeleteModule"); 
        
        System.out.println(message);
        
        Module moduleToDlt = gson.fromJson(message, Module.class);
        System.out.println(moduleToDlt.getId());
        
        // Now we want to remove all the foerign key constraints of this module room in roombookings
        RoomsBookedManagement.deleteByModule(moduleToDlt.getId());
        
        if (ModuleManagement.deleteModule(moduleToDlt.getId())) {
            userSession.getBasicRemote().sendText("Module Successfully Deleted!");
        } // end if
    } // end dltRmsFrmDataBase
    
    // Simply grabs all the rooms in the database as sends it to the requester
    public void sendUsersToAdmin (Session userSession) throws IOException, SQLException {
        ArrayList<User> users = UserManagement.getAllUsers();
        
        JSONObject jsonUsers = new JSONObject();
        jsonUsers.put("AllUsers", gson.toJson(users)); // converts all the rooms to json with message to admin client
        System.out.println("Sending users to admin");
        
        userSession.getBasicRemote().sendText(jsonUsers.toString());
    } // sendUsersToAdmin
    
    // Deletes the user selected by the admin and its foreign key constraints
    public void dltUsrsFrmDatabase (Session userSession, JSONObject jsonRoom) throws Exception {
        System.out.println("Deleting User...");
        /* First gets value of key pair and stores in a string in json format of the room object 
            the admin, wants to delete.
        */
        String user = jsonRoom.getString("DeleteUser");
        
        User usrToDlt = gson.fromJson(user, User.class); // parses json string to user object
        int userId = usrToDlt.getId(); // extracts id
        
        // Fist get rid of the feorign key constrain in the modules table and null any rows linking to this user
        ModuleManagement.updateModule(userId);
        
        // Now we want to remove all the foerign key constraints of this room in roombookings
        RoomsBookedManagement.deleteByUser(userId);
        
    // Now we can delete the User
        if (UserManagement.deleteUser(usrToDlt)) {
            userSession.getBasicRemote().sendText("User Successfully Deleted!");
            
        } // end if
    } // end dltUsrsFrmDataBase

    private void createBooking(Session userSession, JSONObject sentBooking) throws SQLException, Exception {
        System.out.println("Creating Booking...");
        // First gets value of key pair and stores in a string in json format of the room object admin wants to delete
        String bookingRequest = sentBooking.getString("NewBooking"); 
        
        System.out.println(bookingRequest);
        
        AdminBookingInput request = gson.fromJson(bookingRequest, AdminBookingInput.class);
        // Now that we got the object from the client we need to find each attribute
        
        //First Find the Room and get its id
        String roomToFind = request.getRoomName();
        Room roomfound = RoomsManagement.getRoom(roomToFind);
        int roomId = roomfound.getId();
        
        // Next we get the TimeSlot and get its id
        String timslotToFind = request.getTimesoltName();
        TimeSlot timeslotFound = TimeSlotManagement.getTimeSlot(timslotToFind);
        int timeslotId = timeslotFound.getId();
        
        // We get the module and its id 
        String moduleToFind = request.getModuleName();
        Module moduleFound = ModuleManagement.getModule(moduleToFind);
        int moduleId = moduleFound.getId();
        
        //Finally we have the last variable required to insert a booking 
        int adminId = request.getAdminId();
        
        RoomBooked roomBooked = new RoomBooked(roomId, moduleId, timeslotId, adminId);
        
        
        // Now we can insert the booking
        if (RoomsBookedManagement.insertRoomBooked(roomBooked)) {
            userSession.getBasicRemote().sendText("Booking Successfully Made");
        } // end if
    }

    private void sendAllRoomsBooked(Session userSession) throws IOException, SQLException {
        ArrayList<RoomsBookedView> roomsBookedView = BookedRoomsViewMan.getRoomsBookedForToday(); // extracts from database and stores in arraylist
            // simply outputs what was returned from databse
            //Converts to json with message
            JSONObject send2User = new JSONObject();
            // creates a json object with the json user and a message 'User' to implement a function on the client
            send2User.put("RoomsBookedToday", gson.toJson(roomsBookedView));
            System.out.println(send2User.toString());
            userSession.getBasicRemote().sendText(send2User.toString());
    }
    
} // end RoomBookingServer
