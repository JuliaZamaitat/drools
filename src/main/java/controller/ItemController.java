package controller;

import models.Item;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemController {

    public static String addItem(Item item, Connection connection) throws SQLException {
        String insertItemSql = "INSERT INTO items (id, name, price, quantity) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertItemSql)) {
            preparedStatement.setString(1, item.getId());
            preparedStatement.setString(2, item.getName());
            preparedStatement.setDouble(3, item.getPrice());
            preparedStatement.setInt(4, item.getQuantity());
            preparedStatement.executeUpdate();
            return item.getId();
        }
    }

    public static Item getItemById(String id, Connection connection) throws SQLException {
        Item item = null;
        String selectItemSql = "SELECT * FROM items WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectItemSql)) {
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                item = new Item(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getFloat("price"),
                        resultSet.getInt("quantity")
                );
            }
        }
        return item;
    }

    public static void updateItem(Item item, Connection connection) throws SQLException {
        String updateItemSql = "UPDATE items SET name = ?, price = ?, quantity = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateItemSql)) {
            preparedStatement.setString(1, item.getName());
            preparedStatement.setDouble(2, item.getPrice());
            preparedStatement.setInt(3, item.getQuantity());
            preparedStatement.setString(4, item.getId());
            preparedStatement.executeUpdate();
        }
    }

    public static void deleteItem(String id, Connection connection) throws SQLException {
        String deleteItemSql = "DELETE FROM items WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteItemSql)) {
            preparedStatement.setString(1, id);
            preparedStatement.executeUpdate();
        }
    }

    public static List<Item> getItemsForOrder(String orderId, Connection connection) throws SQLException {
        List<Item> items = new ArrayList<>();

        String selectItemsForOrderSql = "SELECT i.* FROM items i " +
                "JOIN order_items oi ON i.id = oi.item_id " +
                "WHERE oi.order_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectItemsForOrderSql)) {
            preparedStatement.setString(1, orderId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String itemId = resultSet.getString("id");
                    String name = resultSet.getString("name");
                    float price = resultSet.getFloat("price");
                    int quantity = resultSet.getInt("quantity");

                    Item item = new Item(itemId, name, price, quantity);
                    items.add(item);
                }
            }
        }

        return items;
    }

    public static void deleteAllItems(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM items;");
        }
    }

}
