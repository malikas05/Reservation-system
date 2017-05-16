package util;

import com.sun.rowset.CachedRowSetImpl;
import controller.Malik_Main;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class Malik_Database {
    private static Connection conn;
    
    //Connect to DB
    public static void dbConnect() throws SQLException, ClassNotFoundException {
        //Setting Mysql JDBC Driver
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your Mysql JDBC Driver?");
            e.printStackTrace();
            throw e;
        }

//        System.out.println("Mysql JDBC Driver Registered!");

        //Establish the Mysql Connection using Connection String
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/travel_system", "root", "");
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console" + e);
            e.printStackTrace();
            throw e;
        }
    }

    //Close Connection
    public static void dbDisconnect() throws SQLException {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception e){
           throw e;
        }
    }

    //DB Execute Query Operation
    public static ResultSet dbExecuteQuery(String queryStmt) throws SQLException, ClassNotFoundException {
        //Declare statement, resultSet and CachedResultSet as null
        Statement stmt = null;
        ResultSet resultSet = null;
        CachedRowSetImpl crs = null;
        try {
            //Connect to DB (Establish Mysql Connection)
            dbConnect();
//            System.out.println("Select statement: " + queryStmt + "\n");
            //Create statement
            stmt = conn.createStatement();
            //Execute select (query) operation
            resultSet = stmt.executeQuery(queryStmt);

            //CachedRowSet Implementation
            //In order to prevent "java.sql.SQLRecoverableException: Closed Connection: next" error
            //We are using CachedRowSet
            crs = new CachedRowSetImpl();
            crs.populate(resultSet);
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeQuery operation : " + e);
            throw e;
        } finally {
            if (resultSet != null) {
                //Close resultSet
                resultSet.close();
            }
            if (stmt != null) {
                //Close Statement
                stmt.close();
            }
            //Close connection
            dbDisconnect();
        }
        //Return CachedRowSet
        return crs;
    }
    
    //DB Execute Update (For Update/Insert/Delete) Operation
    public static void dbExecuteUpdate(String sqlStmt) throws SQLException, ClassNotFoundException {
        //Declare statement as null
        Statement stmt = null;
        try {
            //Connect to DB (Establish Mysql Connection)
            dbConnect();
            
            //Create Statement
            stmt = conn.createStatement();
            //Run executeUpdate operation with given sql statement
            stmt.executeUpdate(sqlStmt);
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeUpdate operation : " + e);
            throw e;
        } finally {
            if (stmt != null) {
                //Close statement
                stmt.close();
            }
            //Close connection
            dbDisconnect();
        }
    }
    
    public static void updateUserSettings(String password, String firstname, String lastname, InputStream image, String username) throws SQLException, ClassNotFoundException{
        PreparedStatement preparedStatement = null;
        String updateTableWithImage = "UPDATE user SET password = ?, firstname = ?, lastname = ?, " 
                        + "image = ?, hasImage = ? WHERE username = ?";
        
        String updateTableSQL = "UPDATE user SET password = ?, firstname = ?, lastname = ?" 
                        + " WHERE username = ?";
        try {
            dbConnect();
            if (image != null){
                preparedStatement = conn.prepareStatement(updateTableWithImage);
                preparedStatement.setBinaryStream(4, image);
                preparedStatement.setByte(5, (byte)1);
                preparedStatement.setString(6, username);
            }
            else{
                preparedStatement = conn.prepareStatement(updateTableSQL);
                preparedStatement.setString(4, username);
            }
            preparedStatement.setString(1, password);
            preparedStatement.setString(2, firstname);
            preparedStatement.setString(3, lastname);
            
            // execute update SQL stetement
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            dbDisconnect();
        }
    }
    
    public static void updateFlightDate(Date timeDate, int id) throws SQLException, ClassNotFoundException{
        PreparedStatement preparedStatement = null;
        String updateTable = "UPDATE flight SET timeDate = ? WHERE id = ?";
        
        try {
            dbConnect();
            System.out.println(timeDate);
            preparedStatement = conn.prepareStatement(updateTable);
            preparedStatement.setTimestamp(1, new java.sql.Timestamp(timeDate.getTime()));
            preparedStatement.setInt(2, id);
            
            // execute update SQL stetement
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            dbDisconnect();
        }
    }
}
