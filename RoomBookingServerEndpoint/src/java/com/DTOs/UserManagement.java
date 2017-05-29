
package com.DTOs;

import com.objects.Module;
import com.objects.User;
import com.server.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class UserManagement {
    
    // Gets the instance of the singlton and uses that already opened connection
    private static Connection conn = ConnectionManager.getInstance().getConnection();
    
    private static final String existsQuery = "SELECT EXISTS( SELECT * FROM users WHERE username = ? AND PASSWORD = ? )";
    
    private static final String selectQuery = "SELECT * FROM users WHERE username = ? AND PASSWORD = ?";
    
    private static final String usernameQuery = "SELECT * FROM users WHERE username = ?";
    
    private static final String insertQuery = "INSERT INTO users(firstname, surname, "
            + "username, password, admin) VALUES(?, ?, ?, ?, ?)";
    
    private static final String deleteQuery = "DELETE FROM users WHERE id = ?;";
    
    private static final String slctAllQuery = "SELECT * FROM users;";
    
     // Will check that a row exists with the credentials entered
    public static boolean checkExists(User usr) throws SQLException {
        ResultSet rs = null;
        try(
                PreparedStatement pstmt = conn.prepareStatement(existsQuery);
            ){
            pstmt.setString(1, usr.getUsername());
            pstmt.setString(2, usr.getPassword());
            rs = pstmt.executeQuery();
            
            if( rs.next() ) {
                int exists = rs.getInt(1);
                if ( exists != 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                // nothing returned from database
                return false;
            } // end else if
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        } finally {
            if (rs != null) rs.close(); // closes resultset
        } // end try catch finally
    }// end check exists
    
    // Will get rows of modules
    public static ArrayList<User> getAllUsers() throws SQLException {
        ResultSet rs = null;
        // uses try with resources to create a prepared statement and pass the String selectRow into it
        try(
                PreparedStatement pstmt = conn.prepareStatement(slctAllQuery);
            ){
            rs = pstmt.executeQuery();
            
            ArrayList<User> users = new ArrayList<User>();
           
            while (rs.next()) {
                int id = rs.getInt("id");
                String fname = rs.getString("firstname");
                String surname = rs.getString("surname");
                boolean admin = rs.getBoolean("admin");
                String username = rs.getString("username");
                String password = rs.getString("Password");
                User user = new User(id, username, password, fname, surname, admin);
                users.add(user);
            }
            
            return users;
        } catch (SQLException e) {
            System.err.println(e);
            return null;
        } finally {
            if (rs != null) rs.close(); // closes resultset
        } // end try catch finally
    } // end getRow()
    
     // will return the user based on the credenials of the row
    public static User getUser(String usrnm, String pass) throws SQLException {
        ResultSet rs = null;
        // uses try with resources to create a prepared statement and pass the String selectRow into it
        try(
                PreparedStatement pstmt = conn.prepareStatement(selectQuery);
            ){
            pstmt.setString(1, usrnm);
            pstmt.setString(2, pass);
            rs = pstmt.executeQuery();
            
            /* Checks that there is indeed a row returned meaning that there was an employee returned from
                the database based on the number inputted by the user
            */
            if( rs.next() ) {
                int id = rs.getInt("id");
                String username = usrnm;
                String password = pass;
                String name = rs.getString("firstname");
                String surname = rs.getString("surname");
                boolean admin = rs.getBoolean("admin");
                
                User usr = new User(id, username, password, name, surname, admin);
                return usr; // row is saved in object and returned
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
    
    // will only return the user selected's id
    public static int findUserbyusername (String username) throws SQLException {
        ResultSet rs = null;
        // uses try with resources to create a prepared statement and pass the String selectRow into it
        try(
                PreparedStatement pstmt = conn.prepareStatement(usernameQuery);
            ){
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            
            /* Checks that there is indeed a row returned meaning that there was an employee returned from
                the database based on the number inputted by the user
            */
            if( rs.next() ) {
                int id = rs.getInt("id");
                System.out.println("Found user id: " + id);
                return id; // id of row is found and returned
            } else {
                // nothing returned from database
                return 0;
            } // end else if
            
        } catch (SQLException e) {
            System.err.println(e);
            return 0;
        } finally {
            if (rs != null) rs.close(); // closes resultset
        } // end try catch finally
    }
    
    // Inserts new Transaction header into databse with only current time and employee number
    public static boolean insertUser(User usr) throws Exception {
        ResultSet keys = null;
        // uses try with resources to create a prepared statement and pass the String inserRow into it
        try(
                PreparedStatement pstmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)
            ){
            
            // placeholders are set value of the attributes of the user 
            pstmt.setString(1, usr.getName());
            pstmt.setString(2, usr.getSurname());
            pstmt.setString(3, usr.getUsername());
            pstmt.setString(4, usr.getPassword());
            pstmt.setBoolean(5, usr.isAdmin());

            int affected = pstmt.executeUpdate();// returns rows affected

             // Checks that a row was indeed inserted grabs that row's id and sets it in the object's id
            if (affected == 1){
                keys = pstmt.getGeneratedKeys();
                keys.next();
                int newKey = keys.getInt(1);
                usr.setId(newKey);
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
    
    // will remove a row in the user table based on its id 
    public static boolean deleteUser(User user) throws Exception {
        ResultSet keys = null;
        // uses try with resources to create a prepared statement and pass the String selectRow into it
        try(
                PreparedStatement pstmt = conn.prepareStatement(deleteQuery, Statement.RETURN_GENERATED_KEYS)
            ){
            pstmt.setInt(1, user.getId()); // sets first parameter as the id passed as args
            
            int affected = pstmt.executeUpdate(); // returns number of rows affected 
            
            // as long a number has been returned then the deletion was completed & transaction commited
            if (affected == 1){
                conn.commit();
                System.out.println("Removed User from database");
                return true;
            } else {
                conn.rollback();
                return false;
            } // end if else 
            
        } catch (SQLException e) {
            System.err.println("Error: " + e);
            conn.rollback();
            return false;
        } finally {
            if(keys != null) keys.close(); // closes ResultSet
        } // end finally
    }// end deleteRow()
}// end UserManagement
