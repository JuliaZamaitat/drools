package controller;

import models.Address;
import models.Client;
import models.Item;
import models.ProductOrder;

import java.sql.*;
import java.util.Date;
import java.util.List;

public class ProductOrderController {
    public static void addProductOrder(ProductOrder order, Connection connection) throws SQLException {
        AddressController addressController = new AddressController();
        Address deliveryAddress = order.getDeliveryAddress();

        // Check if the delivery address already exists
        String existingAddressId = addressController.findExistingAddressId(deliveryAddress, connection);

        // If the delivery address does not exist, insert it
        if (existingAddressId == null) {
            // Insert the delivery address
            // Insert the delivery address
            String insertDeliveryAddressSql = "INSERT INTO addresses (street, house_number, postal_code, city, country, additional_information) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertDeliveryAddressSql, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, deliveryAddress.getStreet());
                preparedStatement.setString(2, deliveryAddress.getHouseNumber());
                preparedStatement.setString(3, deliveryAddress.getPostalCode());
                preparedStatement.setString(4, deliveryAddress.getCity());
                preparedStatement.setString(5, deliveryAddress.getCountry());
                preparedStatement.setString(6, deliveryAddress.getAdditionalInformation());
                preparedStatement.executeUpdate();
                // Get the generated address ID
                ResultSet rs = preparedStatement.getGeneratedKeys();
                if (rs.next()) {
                    String deliveryAddressId = rs.getString(1);
                    deliveryAddress.setId(deliveryAddressId);
                }
            }
        } else {
            // Set the existing address ID to the delivery address
            deliveryAddress.setId(existingAddressId);
        }

        // Insert the product order
        String insertProductOrderSql = "INSERT INTO product_orders (id, client_id, delivery_address_id, order_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertProductOrderSql)) {
            preparedStatement.setString(1, order.getId());
            preparedStatement.setString(2, order.getClient().getId());
            preparedStatement.setString(3, order.getDeliveryAddress().getId());
            preparedStatement.setTimestamp(4, new Timestamp(order.getOrderDate().getTime()));
            preparedStatement.executeUpdate();
        }

        // Insert the order items
        String insertOrderItemsSql = "INSERT INTO order_items (order_id, item_id) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertOrderItemsSql)) {
            for (Item item : order.getItems()) {
                preparedStatement.setString(1, order.getId());
                preparedStatement.setString(2, item.getId());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }

    // Get a product order by ID
    public static ProductOrder getProductOrder(String orderId, Connection connection) throws SQLException {
        ProductOrder order = null;

        String selectProductOrderSql = "SELECT * FROM product_orders WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectProductOrderSql)) {
            preparedStatement.setString(1, orderId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    ClientController clientController = new ClientController();
                    AddressController addressController = new AddressController();
                    ItemController itemController = new ItemController();

                    String clientId = resultSet.getString("client_id");
                    String deliveryAddressId = resultSet.getString("delivery_address_id");
                    Date orderDate = resultSet.getTimestamp("order_date");

                    Client client = clientController.getClient(clientId, connection);
                    Address deliveryAddress = addressController.getAddressById(deliveryAddressId, connection);
                    List<Item> items = itemController.getItemsForOrder(orderId, connection);

                    order = new ProductOrder(orderId, client, deliveryAddress, items, orderDate);
                }
            }
        }

        return order;
    }
    // Update an existing product order
    public static void updateProductOrder(ProductOrder order, Connection connection) throws SQLException {
        String updateProductOrderSql = "UPDATE product_orders SET client_id = ?, delivery_address_id = ?, order_date = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateProductOrderSql)) {
            preparedStatement.setString(1, order.getClient().getId());
            preparedStatement.setString(2, order.getDeliveryAddress().getId());
            preparedStatement.setTimestamp(3, new Timestamp(order.getOrderDate().getTime()));
            preparedStatement.setString(4, order.getId());
            preparedStatement.executeUpdate();
        }
    }

    // Delete a product order by ID
    public static void deleteProductOrder(String orderId, Connection connection) throws SQLException {
        // Delete order items
        String deleteOrderItemsSql = "DELETE FROM order_items WHERE order_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteOrderItemsSql)) {
            preparedStatement.setString(1, orderId);
            preparedStatement.executeUpdate();
        }

        // Delete the product order
        String deleteProductOrderSql = "DELETE FROM product_orders WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteProductOrderSql)) {
            preparedStatement.setString(1, orderId);
            preparedStatement.executeUpdate();
        }
    }

    public static void deleteAllProductOrders(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM order_items;");
            statement.execute("DELETE FROM product_orders;");
        }
    }
}


