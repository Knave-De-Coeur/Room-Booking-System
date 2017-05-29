package com.DTOs;

import com.objects.RoomBooked;
import com.server.ConnectionManager;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class RoomsBookedManagement {
    // Gets the instance of the singlton and uses that already opened connection
    private static Connection conn = ConnectionManager.getInstance().getConnection();
    
    private static final String selectQuery = "SELECT * FROM RoomsBooked Where DayBooked = CURDATE()";
    
    private static final String insertQuery = "INSERT INTO RoomsBooked (Room_id, Module_Id, TimeSlot_id, DayBooked, User_id) VALUES (?, ?, ?, CURDATE(), ?)";
    
    private static final String dltByRoomQuery = "DELETE FROM RoomsBooked WHERE Room_id = ?";
    
    private static final String dltByModuleQuery = "DELETE FROM RoomsBooked WHERE Module_Id = ?;";
    
    private static final String dltByUserQuery = "DELETE FROM RoomsBooked WHERE User_Id = ?;";
    
    // Will get the row of the room by querying the name nad returning that object
    public static ArrayList<RoomBooked> getRoomsBooked() throws SQLException {
        ResultSet rs = null;
        // uses try with resources to create a prepared statement and pass the String selectRow into it
        try(
                PreparedStatement pstmt = conn.prepareStatement(selectQuery);
            ){
            rs = pstmt.executeQuery();
            
            ArrayList<RoomBooked> roomsBooked = new ArrayList<RoomBooked>();
           
            while (rs.next()) {
                int id = rs.getInt("id");
                int Room_id = rs.getInt("Room_id");
                int Module_id = rs.getInt("Module_id");
                int TimeSlot_id = rs.getInt("TimeSlot_id");
                Date DayBooked = rs.getDate("DayBooked");
                int User_id = rs.getInt("User_id");
                RoomBooked roomBooked = new RoomBooked(Room_id, Module_id, TimeSlot_id, User_id);
                roomsBooked.add(roomBooked);
            }
            
            return roomsBooked;
        } catch (SQLException e) {
            System.err.println(e);
            return null;
        } finally {
            if (rs != null) rs.close(); // closes resultset
        } // end try catch finally
    } // end getRow()
    
    // Inserts new room into databse 
    public static boolean insertRoomBooked(RoomBooked roomBooked) throws Exception {
        ResultSet keys = null;
        // uses try with resources to create a prepared statement and pass the String inserRow into it
        try(
                PreparedStatement pstmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)
            ){
            
            // placeholders are set value of the attributes of the user 
            pstmt.setInt(1, roomBooked.getRoom_id());
            pstmt.setInt(2, roomBooked.getModule_id()); 
            pstmt.setInt(3, roomBooked.getTimeSlot_id());
            pstmt.setInt(4, roomBooked.getUser_id());

            int affected = pstmt.executeUpdate();// returns rows affected

             // Checks that a row was indeed inserted grabs that row's id and sets it in the object's id
            if (affected == 1){
                keys = pstmt.getGeneratedKeys();
                keys.next();
                int newKey = keys.getInt(1);
                roomBooked.setId(newKey);
                conn.commit();
            } else {
                System.err.println("No rows affected");
                conn.rollback();
                return false;
            } // end else if

        } catch (SQLException e) {
            System.err.println("Error: " + e);
            return false;
        } finally {
            if(keys != null) keys.close(); // closes reulstet
        }
        return true;
    } // end insertRow()
    
    // Might or might not remove rows in the database, therefore nothing needs to be returned
    public static void deleteByRoom (int Room_id) throws Exception {
        ResultSet keys = null;
        // uses try with resources to create a prepared statement and pass the String selectRow into it
        try(
                PreparedStatement pstmt = conn.prepareStatement(dltByRoomQuery, Statement.RETURN_GENERATED_KEYS)
            ){
            pstmt.setInt(1, Room_id); // sets first parameter as the id passed as args
            System.out.println("Trying to delete roombooking");
            int affected = pstmt.executeUpdate(); // returns number of rows affected
            
            if (affected == 1){
                System.out.println("Committing the deletion of one or more roombookings");
                conn.commit();
            } else {
                System.out.println("No bookings were related to the room so none need to be deleted");
            }
            
        } catch (SQLException e) {
            System.err.println("Error: " + e);
        } finally {
            if(keys != null) keys.close(); // closes ResultSet
            
        } // end finally
    } // end deleteByRoom
    
    // Might or might not remove rows in the database, therefore nothing needs to be returned
    public static void deleteByModule (int moduleId) throws Exception {
        ResultSet keys = null;
        // uses try with resources to create a prepared statement and pass the String selectRow into it
        try(
                PreparedStatement pstmt = conn.prepareStatement(dltByModuleQuery, Statement.RETURN_GENERATED_KEYS)
            ){
            pstmt.setInt(1, moduleId); // sets first parameter as the id passed as args
            System.out.println("Trying to delete roombooking");
            int affected = pstmt.executeUpdate(); // returns number of rows affected
            
            if (affected == 1){
                System.out.println("Committing the deletion of one or more roombookings");
                conn.commit();
            } else {
                System.out.println("No bookings were related to the room so none need to be deleted");
            }
            
        } catch (SQLException e) {
            System.err.println("Error: " + e);
        } finally {
            if(keys != null) keys.close(); // closes ResultSet
            
        } // end finally
    } // end deleteByRoom
    
    // Might or might not remove rows in the database, therefore nothing needs to be returned
    public static boolean deleteByUser (int userId) throws Exception {
        ResultSet rs = null;
        // uses try with resources to create a prepared statement and pass the String selectRow into it
        try(
                PreparedStatement pstmt = conn.prepareStatement(dltByUserQuery, Statement.RETURN_GENERATED_KEYS)
            ){
            pstmt.setInt(1, userId); // sets first parameter as the id passed as args
            System.out.println("Trying to delete roombooking");
            int affected = pstmt.executeUpdate(); // returns number of rows affected
            
            if (affected == 1){
                System.out.println("Committing the deletion of one or more roombookings");
                conn.commit();
                return true;
            } else {
                System.out.println("No bookings were related to the room so none need to be deleted");
                return false;
            }
            
        } catch (SQLException e) {
            System.err.println("Error: " + e);
            return false;
        } finally {
            if(rs != null) rs.close(); // closes ResultSet
        } // end finally
    } // end deleteByRoom
    
} // end deleteByModule