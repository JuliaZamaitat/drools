package controller;

import models.Address;
import models.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClientController {

    // Create
    public static void addClient(Client client, Connection connection) throws SQLException {
        String insertClientSql = "INSERT INTO clients (id, first_name, last_name, email, billing_address_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertClientSql)) {
            preparedStatement.setString(1, client.getId());
            preparedStatement.setString(2, client.getFirstName());
            preparedStatement.setString(3, client.getLastName());
            preparedStatement.setString(4, client.getEmail());
            preparedStatement.setString(5, client.getBillingAddress().getId());
            preparedStatement.executeUpdate();
        }
    }

    // Read
    public Client getClient(String id, Connection connection) throws SQLException {
        String selectClientSql = "SELECT * FROM clients WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectClientSql)) {
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String billingAddressId = resultSet.getString("billing_address_id");

                AddressController addressController = new AddressController();
                Address billingAddress = addressController.getAddressById(billingAddressId, connection);

                return new Client(id, firstName, lastName, email, billingAddress);
            }
            return null;
        }
    }

    // Update
    public void updateClient(Client client, Connection connection) throws SQLException {
        String updateClientSql = "UPDATE clients SET first_name = ?, last_name = ?, email = ?, billing_address_id = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateClientSql)) {
            preparedStatement.setString(1, client.getFirstName());
            preparedStatement.setString(2, client.getLastName());
            preparedStatement.setString(3, client.getEmail());
            preparedStatement.setString(4, client.getBillingAddress().getId());
            preparedStatement.setString(5, client.getId());
            preparedStatement.executeUpdate();
        }
    }

    // Delete
    public void deleteClient(String id, Connection connection) throws SQLException {
        String deleteClientSql = "DELETE FROM clients WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteClientSql)) {
            preparedStatement.setString(1, id);
            preparedStatement.executeUpdate();
        }
    }

    // List all clients
    public List<Client> getAllClients(Connection connection) throws SQLException {
        List<Client> clients = new ArrayList<>();
        String selectAllClientsSql = "SELECT * FROM clients";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(selectAllClientsSql);
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String billingAddressId = resultSet.getString("billing_address_id");

                AddressController addressController = new AddressController();
                Address billingAddress = addressController.getAddressById(billingAddressId, connection);

                clients.add(new Client(id, firstName, lastName, email, billingAddress));
            }
        }
        return clients;
    }

    // ClientController.java
    public static void deleteAllClients(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM clients;");
        }
    }
}
