package database;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConfig {
    public static final String URL = "jdbc:mysql://localhost:3306/mysql?useSSL=false&allowPublicKeyRetrieval=true";
    public static final String USER = "root";
    public static final String PASSWORD = "my-secret-pw";

    public static void createTables(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String createAddressesTableSql = "CREATE TABLE IF NOT EXISTS addresses ( " +
                    "id VARCHAR(255) PRIMARY KEY," +
                    "street VARCHAR(255) NOT NULL," +
                    "postal_code VARCHAR(10) NOT NULL," +
                    "house_number VARCHAR(255) NOT NULL," +
                    "city VARCHAR(255) NOT NULL," +
                    "country VARCHAR(255) NOT NULL," +
                    "additional_information VARCHAR(255));";
            statement.execute(createAddressesTableSql);

            String createClientsTableSql = "CREATE TABLE IF NOT EXISTS clients (" +
                    "id VARCHAR(255) PRIMARY KEY," +
                    "first_name VARCHAR(255) NOT NULL," +
                    "last_name VARCHAR(255) NOT NULL," +
                    "email VARCHAR(255) NOT NULL," +
                    "billing_address_id VARCHAR(255) NOT NULL," +
                    "FOREIGN KEY (billing_address_id) REFERENCES addresses (id));";
            statement.execute(createClientsTableSql);

            String createItemsTableSql = "CREATE TABLE IF NOT EXISTS items (" +
                    "id VARCHAR(255) PRIMARY KEY," +
                    "name VARCHAR(255) NOT NULL," +
                    "price DOUBLE NOT NULL," +
                    "quantity INT NOT NULL );";
            statement.execute(createItemsTableSql);

            String createProductOrdersTableSql = "CREATE TABLE IF NOT EXISTS product_orders (" +
                    "id VARCHAR(255) PRIMARY KEY," +
                    "client_id VARCHAR(255) NOT NULL," +
                    "delivery_address_id VARCHAR(255) NOT NULL," +
                    "order_date DATETIME NOT NULL," +
                    "order_process_date DATETIME," +
                    "FOREIGN KEY (client_id) REFERENCES clients (id)," +
                    "FOREIGN KEY (delivery_address_id) REFERENCES addresses (id));";
            statement.execute(createProductOrdersTableSql);

            String createOrderItemsTableSql = "CREATE TABLE IF NOT EXISTS order_items (" +
                    "order_id VARCHAR(255) NOT NULL," +
                    "item_id VARCHAR(255) NOT NULL," +
                    "PRIMARY KEY (order_id, item_id)," +
                    "FOREIGN KEY (order_id) REFERENCES product_orders (id)," +
                    "FOREIGN KEY (item_id) REFERENCES items (id));";
            statement.execute(createOrderItemsTableSql);
        }
    }
}


