package com.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private static ConnectionManager instance = null;
    
    private final static String MYSQL_JDBCDRIVER = "com.mysql.jdbc.Driver"; // driver
    private final String MYSQL_CONN = "jdbc:mysql://localhost/RoomBooking"; //connection string to mysql
    private final String USERNAME = "Alex"; // Username of mysql
    private final String PASSWORD = "knave"; // Password of mysql
    
    private Connection conn = null; // connection object to be returned throughout application
    
    //prevents any other class from instantiating.
    private ConnectionManager(){
    } // end constructor
    
    // When this function is called for the first time it will create an instance of this class and assigns it to the variable 
    public static ConnectionManager getInstance(){
        if (instance == null) {
            instance = new ConnectionManager();
            System.out.println("Connection instance been made");
        }
        // if it isn't null it will return the contents of the variable
        return instance;
    }// end ConnectionManager getInstance()
    
    // call if connection isn't already open
    private boolean openConnection(){
        try{
            /* the connection interface  is being set a communication context via the
                driver manager class thus initializing its connectivity to the database
            */
            conn = DriverManager.getConnection(MYSQL_CONN, USERNAME, PASSWORD);
            System.out.println("Setting the connection");
            conn.setAutoCommit(false);// simply turning off auto commit for better control of what gets sent to mysql
            return true;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        }
    }// end openConnection()
    
    // will be called in the one and only insatnce of the connection manager
    public Connection getConnection(){
        if(conn == null){
            if (openConnection()){
                System.out.println("Coonectoion has been established with mysql");
                return conn;
            } else{
                System.out.println("Connection has already been established, returning that connection");
                return conn;
            } // end elseif
        }// end if
        return conn;
    }// end Connection getConnection
    
    // simply returns the driver 
    public static String getMYSQLJDBCDRIVER(){
        return MYSQL_JDBCDRIVER;
    } // end getMYSQLJDBCDRIVER
    
    // closes connection and sets it to null
    public void close(){
        System.out.println("Closing Connection");
        try{
            conn.close(); 
            conn = null;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }// end try catch
    }// end close()
    
}// end ConnectionManager Class
