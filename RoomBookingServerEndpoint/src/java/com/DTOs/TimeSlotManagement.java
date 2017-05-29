package com.DTOs;

import com.objects.TimeSlot;
import com.server.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;

public class TimeSlotManagement {
    // Gets the instance of the singlton and uses that already opened connection
    private static Connection conn = ConnectionManager.getInstance().getConnection();
    
    private static final String selectQuery = "SELECT * FROM TimeSlots WHERE name = ?;";
    
    private static final String insertQuery = "INSERT INTO TimeSlots(start_at, end_at, size) VALUES(?, ?, ?)";
    
    private static final String deleteQuery = "DELETE FROM TimeSlots WHERE id = ?";
    
    // Will get the row of the timeslot by querying the name nad returning that object
    public static TimeSlot getTimeSlot(String timeSlotName) throws SQLException {
        ResultSet rs = null;
        // uses try with resources to create a prepared statement and pass the String selectRow into it
        try(
                PreparedStatement pstmt = conn.prepareStatement(selectQuery);
            ){
            pstmt.setString(1, timeSlotName);
            rs = pstmt.executeQuery();
            
            /* Checks that there is indeed a row returned meaning that there was an employee returned from
                the database based on the number inputted by the user
            */
            if( rs.next() ) {
                int id = rs.getInt("id");
                Time start = rs.getTime("start_at");
                Time end = rs.getTime("end_at");
                String name = rs.getString("name");
                
                TimeSlot timeSlot = new TimeSlot(id, start, end, name);
                return timeSlot;
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
    public static boolean insertTimeSlot(TimeSlot timeSlot) throws Exception {
        ResultSet keys = null;
        // uses try with resources to create a prepared statement and pass the String inserRow into it
        try(
                PreparedStatement pstmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)
            ){
            
            // placeholders are set value of the attributes of the user 
            pstmt.setTime(1, timeSlot.getStart_at());
            pstmt.setTime(2, timeSlot.getEnd_at()); 
            pstmt.setString(3, timeSlot.getName());

            int affected = pstmt.executeUpdate();// returns rows affected

             // Checks that a row was indeed inserted grabs that row's id and sets it in the object's id
            if (affected == 1){
                keys = pstmt.getGeneratedKeys();
                keys.next();
                int newKey = keys.getInt(1);
                timeSlot.setId(newKey);
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
        return true;
    } // end insertRow()
    
    // will remove a row in the rooms table based on its id 
    public static boolean deleteTimeSlot(TimeSlot timeSlot) throws Exception {
        ResultSet keys = null;
        // uses try with resources to create a prepared statement and pass the String selectRow into it
        try(
                PreparedStatement pstmt = conn.prepareStatement(deleteQuery, Statement.RETURN_GENERATED_KEYS)
            ){
            pstmt.setInt(1, timeSlot.getId()); // sets first parameter as the id passed as args
            
            int affected = pstmt.executeUpdate(); // returns number of rows affected 
            
            // as long a number has been returned then the deletion was completed & transaction commited
            if (affected == 1){
                return true;
            } else {
                return false;
            } // end if else 
            
        } catch (SQLException e) {
            System.err.println("Error: " + e);
            return false;
        } finally {
            if(keys != null) keys.close(); // closes ResultSet
        } // end finally
    } // end deleteTimeSlot
}
