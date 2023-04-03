import database.DatabaseConfig;
import drools.DroolsHelper;

import java.sql.SQLException;

public class UserRulesTest {
    public static void main(String[] args) {

//        try {
//            DatabaseHelper dbHelper = new DatabaseHelper(DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
//            dbHelper.createTable();
//            int userId = dbHelper.addUser("Marie Müller", "marie.müller@example.com");
//
//            // Fetch user from database
//            User userFromDb = dbHelper.getUser(userId);
//            System.out.println("Fetched user: " + userFromDb.getName());
//
//            // Apply Drools rule
//            DroolsHelper droolsHelper = new DroolsHelper();
//            droolsHelper.applyRules(userFromDb);
//            System.out.println("User after applying Drools rule: " + userFromDb.getName());
//
//            // Update user in database
//            dbHelper.updateUser(userFromDb);
//            System.out.println("User updated in database");
//
//            dbHelper.deleteUser(userId);
//            dbHelper.close();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }
}
