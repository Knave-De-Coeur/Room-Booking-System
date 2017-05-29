package com.DTOs;

import com.objects.Module;
import com.objects.TimeSlot;
import com.server.ConnectionManager;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;

public class ModuleManagement {
    // Gets the instance of the singlton and uses that already opened connection
    private static Connection conn = ConnectionManager.getInstance().getConnection();
    
    private static final String selectQuery = "SELECT * FROM modules WHERE moduleName = ?";
    
    private static final String updateQuery = "UPDATE modules SET users_id = NULL WHERE users_id = ?";
    
    private static final String getAllMdlsQuery = "SELECT * FROM modules;";
    
    private static final String insertQuery = "INSERT INTO modules(moduleName, moduleDesc, moduleLeader, users_id) VALUES(?, ?, ?, ?)";
    
    private static final String deleteQuery = "DELETE FROM modules WHERE id = ?";
    
    // Will get rows of modules
    public static ArrayList<Module> getAllModules() throws SQLException {
        ResultSet rs = null;
        // uses try with resources to create a prepared statement and pass the String selectRow into it
        try(
                PreparedStatement pstmt = conn.prepareStatement(getAllMdlsQuery);
            ){
            rs = pstmt.executeQuery();
            
            ArrayList<Module> modules = new ArrayList<Module>();
           
            while (rs.next()) {
                int id = rs.getInt("id");
                String modName = rs.getString("moduleName");
                String modDesc = rs.getString("moduleDesc");
                String modLeader = rs.getString("moduleLeader");
                int userId = rs.getInt("users_id");
                Module module = new Module(id, modName, modDesc, modLeader, userId);
                modules.add(module);
            }
            
            return modules;
        } catch (SQLException e) {
            System.err.println(e);
            return null;
        } finally {
            if (rs != null) rs.close(); // closes resultset
        } // end try catch finally
    } // end getRow()
    
    // Will get the row of the module by querying the name nad returning that object
    public static Module getModule(String moduleName) throws SQLException {
        ResultSet rs = null;
        // uses try with resources to create a prepared statement and pass the String selectRow into it
        try(
                PreparedStatement pstmt = conn.prepareStatement(selectQuery);
            ){
            pstmt.setString(1, moduleName);
            rs = pstmt.executeQuery();
            
            /* Checks that there is indeed a row returned meaning that there was an employee returned from
                the database based on the number inputted by the user
            */
            if( rs.next() ) {
                int id = rs.getInt("id");
                String modName = rs.getString("moduleName");
                String modDesc = rs.getString("moduleDesc");
                String modLeader = rs.getString("moduleLeader");
                int userId = rs.getInt("users_id");
                Module module = new Module(id, modName, modDesc, modLeader, userId);
                return module;
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
    public static boolean insertModule(Module module) throws Exception {
        ResultSet keys = null;
        // uses try with resources to create a prepared statement and pass the String inserRow into it
        try(
                PreparedStatement pstmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)
            ){
            
            // placeholders are set value of the attributes of the user 
            pstmt.setString(1, module.getModuleName()); 
            pstmt.setString(2, module.getModuleDesc()); 
            pstmt.setString(3, module.getModuleLeader());
            pstmt.setInt(4, module.getUserId());

            int affected = pstmt.executeUpdate();// returns rows affected

             // Checks that a row was indeed inserted grabs that row's id and sets it in the object's id
            if (affected == 1){
                keys = pstmt.getGeneratedKeys();
                keys.next();
                int newKey = keys.getInt(1);
                module.setId(newKey);
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
    public static boolean deleteModule(int moduleId) throws Exception {
        ResultSet keys = null;
        // uses try with resources to create a prepared statement and pass the String selectRow into it
        try(
                PreparedStatement pstmt = conn.prepareStatement(deleteQuery, Statement.RETURN_GENERATED_KEYS)
            ){
            pstmt.setInt(1, moduleId);
            System.out.println("Trying to delete Module");
            
            int affected = pstmt.executeUpdate(); // returns number of rows affected 
            
            // as long a number has been returned then the deletion was completed & transaction commited
            if (affected == 1){
                System.out.println("Committing the deletion of one or more roombookings");
                conn.commit();
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
    }
    
    // will find the rows and change their user id to null
    public static boolean updateModule(int userId) throws Exception {
        ResultSet keys = null;
        // uses try with resources to create a prepared statement and pass the String deleteRow into it
        try(
                PreparedStatement pstmt = conn.prepareStatement(updateQuery, Statement.RETURN_GENERATED_KEYS)
            ){
            pstmt.setInt(1, userId); // first placeholder sets the user id to find
            
            int affected = pstmt.executeUpdate(); // returns rows affected
            
            // Checks that a row was indeed updated 
            if (affected >= 1) {
                conn.commit(); // commits the transaction when there 
                return true;
            } else {
                conn.rollback(); // rolls back transaction in case of errors
                return false;
            } // end if else
            
        } catch (SQLException e) {
            System.err.println("Error: " + e);
            conn.rollback(); // rolls back transaction in case of errors
            return false;
        } finally {
            if(keys != null) keys.close(); // clsoes resultset
        } // end try catch finally
    } // end updateModule()
    
}// end ModuleManagementClass