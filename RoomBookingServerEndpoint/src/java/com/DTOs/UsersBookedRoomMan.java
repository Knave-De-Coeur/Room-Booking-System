/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.DTOs;

import com.objects.RoomsBookedView;
import com.objects.UsersBookedRooms;
import com.server.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

/**
 *
 * @author alexa
 */
public class UsersBookedRoomMan {
    // Gets the instance of the singlton and uses that already opened connection
    private static Connection conn = ConnectionManager.getInstance().getConnection();
    
    private static final String selectQuery = "SELECT rooms.RoomName, modules.moduleName, timeslots.start_at, timeslots.end_at "
            + "FROM roomsbooked INNER JOIN rooms ON roomsbooked.room_id=rooms.Id INNER JOIN modules ON roomsbooked.Module_Id=modules.id INNER JOIN timeslots ON roomsbooked.TimeSlot_id=timeslots.id INNER JOIN users ON modules.users_id=users.id "
            + "WHERE DayBooked = CURDATE() AND users.username = ? ORDER BY timeslots.start_at ASC ";
    
    public static ArrayList<UsersBookedRooms> getRoomsBookedForToday(String username) throws SQLException {
        ResultSet rs = null;
        // uses try with resources to create a prepared statement and pass the String selectRow into it
        try(
                PreparedStatement pstmt = conn.prepareStatement(selectQuery);
            ){
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            
            ArrayList<UsersBookedRooms> usersBookedRooms = new ArrayList<UsersBookedRooms>();
           
            if (rs.next()) {
                rs.beforeFirst();
                while (rs.next()) {
                    String roomName = rs.getString("RoomName");
                    String moduleName = rs.getString("moduleName");
                    Time Start = rs.getTime("start_at");
                    Time End = rs.getTime("end_at");
                    
                    UsersBookedRooms urb = new UsersBookedRooms(roomName, moduleName, Start, End);
                    usersBookedRooms.add(urb);
                }
                return usersBookedRooms;
            } else {
                return null;
            }
            
        } catch (SQLException e) {
            System.err.println(e);
            return null;
        } finally {
            if (rs != null) rs.close(); // closes resultset
        } // end try catch finally
    } // end getRoomsBookedForToday()
} // end UsersBookedRoomMan
