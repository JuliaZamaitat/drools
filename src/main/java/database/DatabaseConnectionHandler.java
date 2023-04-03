package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionHandler {
    private Connection connection;
    public Connection openConnection(){
        try {
            this.connection = DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
            System.out.println("Connected to MySQL database!");
            return this.connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void closeConnection(){
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Connection to db was closed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
