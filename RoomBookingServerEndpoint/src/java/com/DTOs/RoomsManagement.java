package com.DTOs;

import com.objects.Room;
import com.objects.RoomsBookedView;
import com.server.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;

public class RoomsManagement {
    // Gets the instance of the singlton and uses that already opened connection
    private static Connection conn = ConnectionManager.getInstance().getConnection();
    
    private static final String selectQuery = "SELECT * FROM Rooms WHERE RoomName = ?";
    
    private static final String allRoomsQuery = "SELECT * FROM Rooms; ";
    
    private static final String insertQuery = "INSERT INTO Rooms(RoomName, type, size) VALUES(?, ?, ?)";
    
    private static final String deleteQuery = "DELETE FROM Rooms WHERE id = ?";
    
    public static ArrayList<Room> getAllRooms() throws SQLException {
        ResultSet rs = null;
        // uses try with resources to create a prepared statement and pass the String selectRow into it
        try(
                PreparedStatement pstmt = conn.prepareStatement(allRoomsQuery);
            ){
            rs = pstmt.executeQuery();
            
            ArrayList<Room> roomsFromServer = new ArrayList<Room>();
           
            if (rs.next()) {
                rs.beforeFirst();
                while (rs.next()) {
                    int roomId = rs.getInt("id");
                    String roomName = rs.getString("RoomName");
                    String roomType = rs.getString("type");
                    int roomSize = rs.getInt("size");
                    
                    Room serverRoom = new Room(roomId, roomName, roomType, roomSize);
                    roomsFromServer.add(serverRoom);
                }
                return roomsFromServer;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.err.println(e);
            return null;
        } finally {
            if (rs != null) rs.close(); // closes resultset
        } // end try catch finally
    }// end getAllRooms()
    
    // Will get the row of the room by querying the name nad returning that object
    public static Room getRoom(String name) throws SQLException {
        ResultSet rs = null;
        // uses try with resources to create a prepared statement and pass the String selectRow into it
        try(
                PreparedStatement pstmt = conn.prepareStatement(selectQuery);
            ){
            pstmt.setString(1, name);
            rs = pstmt.executeQuery();
            
            /* Checks that there is indeed a row returned meaning that there was an employee returned from
                the database based on the number inputted by the user
            */
            if( rs.next() ) {
                int id = rs.getInt("id");
                String type = rs.getString("type");
                int size = rs.getInt("size");
                String roomName = name;
                
                Room room = new Room(id, roomName, type, size);
                return room; // row is saved in object and returned
            } else {
                // nothing returned from database
                return null;
            } // end else if
            
        } catch (SQLException e) {
            System.err.println(e);
            return null;
        } finally {
            if (rs != null) rs.close(); // closes resultset
        } // end try catch finally
    } // end getRow()
    
    // Inserts new room into databse 
    public static boolean insertRoom(Room room) throws Exception {
        ResultSet keys = null;
        // uses try with resources to create a prepared statement and pass the String inserRow into it
        try(
                PreparedStatement pstmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)
            ){
            
            // placeholders are set value of the attributes of the user 
            pstmt.setString(1, room.getRoomName()); 
            pstmt.setString(2, room.getType()); 
            pstmt.setInt(3, room.getSize());

            int affected = pstmt.executeUpdate();// returns rows affected

             // Checks that a row was indeed inserted grabs that row's id and sets it in the object's id
            if (affected == 1){
                keys = pstmt.getGeneratedKeys();
                keys.next();
                int newKey = keys.getInt(1);
                room.setId(newKey);
            } else {
                System.err.println("No rows affected");
                return false;
            } // end else if

        } catch (SQLException e) {
            System.err.println("Error: " + e);
            return false;
        } finally {
            if(keys != null) keys.close(); // closes reulstet
        }
        conn.commit();
        return true;
    } // end insertRow()
    
    // will remove a row in the rooms table based on its id 
    public static boolean deleteRoom(int RoomId) throws Exception {
        ResultSet keys = null;
        // uses try with resources to create a prepared statement and pass the String selectRow into it
        try(
                PreparedStatement pstmt = conn.prepareStatement(deleteQuery, Statement.RETURN_GENERATED_KEYS)
            ){
            pstmt.setInt(1, RoomId); // sets first parameter as the id passed as args
            System.out.println("Truing to delete rooms");
            int affected = pstmt.executeUpdate(); // returns number of rows affected 
            
            // as long a number has been returned then the deletion was completed & transaction commited
            if (affected >= 1){
                System.out.println("There was at least one room deleted, therefore committing...");
                conn.commit();
                return true;
            } else {
                System.out.println("No Rooms were deleted");
                return false;
            } // end if else 
            
        } catch (SQLException e) {
            System.err.println("Error: " + e);
            return false;
        } finally {
            if(keys != null) keys.close(); // closes ResultSet
        } // end finally
    }
}
