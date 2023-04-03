import controller.AddressController;
import controller.ClientController;
import controller.ItemController;
import controller.ProductOrderController;
import database.DatabaseConfig;
import database.DatabaseConnectionHandler;
import drools.DroolsHelper;
import models.Address;
import models.Client;
import models.Item;
import models.ProductOrder;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductOrderRulesTest {
    private static DatabaseConnectionHandler dch;
    private static Connection databaseConnection;

    public static void main(String[] args) throws SQLException {
        try {
            databaseSetup();

            cleanDatabase();

            insertTestData();

            //do tests

            ProductOrder productOrder = ProductOrderController.getProductOrder("a1", databaseConnection);
            System.out.println("Fetched productOrder: " + productOrder.getId());

            //Apply Drools rule
            DroolsHelper droolsHelper = new DroolsHelper();
            droolsHelper.applyRules(productOrder);
            System.out.println("ProductOrder Order Date: " + productOrder.getOrderDate());

            System.out.println("ProductOrder Process Date after applying Drools rule: " + productOrder.getOrderProcessDate());

            ProductOrderController.updateProductOrder(productOrder, databaseConnection);
            System.out.println("ProductOrder updated in database");


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //close DB
            databaseTeardown();
        }
    }
    private static void databaseSetup() throws SQLException {
        //Database Connection & Table Setup
        dch = new DatabaseConnectionHandler();
        databaseConnection = dch.openConnection();
        if (databaseConnection == null) throw new SQLException();
        DatabaseConfig.createTables(databaseConnection);
    }
    private static void databaseTeardown() throws SQLException {
        dch.closeConnection();
    }

    private static void insertTestData() throws SQLException {
        Address address = new Address("1", "Marienstraße", "2", "12459", "Berlin", "Deutschland", "");
        AddressController.addAddress(address, databaseConnection);
        Client client = new Client("1", "Marie", "Müller", "marie.müller@mail.de", address);
        ClientController.addClient(client, databaseConnection);
        Item item = new Item("1", "Shampoo", 2.50, 100);
        ItemController.addItem(item, databaseConnection);
        Item item2 = new Item("2", "Hairdryer", 25, 30);
        ItemController.addItem(item2, databaseConnection);
        List<Item> items = new ArrayList<>();
        items.add(item);
        items.add(item2);
        Date orderDate = new Date();
        ProductOrder po = new ProductOrder("a1", client, address, items, orderDate);
        ProductOrderController.addProductOrder(po, databaseConnection);
    }

    private static void cleanDatabase() throws SQLException {
        ProductOrderController.deleteAllProductOrders(databaseConnection);
        ItemController.deleteAllItems(databaseConnection);
        ClientController.deleteAllClients(databaseConnection);
        AddressController.deleteAllAddresses(databaseConnection);

    }
}
