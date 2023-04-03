package controller;

import models.Address;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AddressController {
    public static String findExistingAddressId(Address address, Connection connection) throws SQLException {
        String existingAddressId = null;

        String selectAddressSql = "SELECT id FROM addresses WHERE street = ? AND house_number = ? AND postal_code = ? AND city = ? AND country = ? AND additional_information = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectAddressSql)) {
            preparedStatement.setString(1, address.getStreet());
            preparedStatement.setString(2, address.getHouseNumber());
            preparedStatement.setString(3, address.getPostalCode());
            preparedStatement.setString(4, address.getCity());
            preparedStatement.setString(5, address.getCountry());
            preparedStatement.setString(6, address.getAdditionalInformation());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    existingAddressId = resultSet.getString("id");
                }
            }
        }

        return existingAddressId;
    }

    public static String addAddress(Address address, Connection connection) throws SQLException {
        String insertAddressSql = "INSERT INTO addresses (street, house_number, postal_code, city, country, additional_information) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertAddressSql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, address.getStreet());
            preparedStatement.setString(2, address.getHouseNumber());
            preparedStatement.setString(3, address.getPostalCode());
            preparedStatement.setString(4, address.getCity());
            preparedStatement.setString(5, address.getCountry());
            preparedStatement.setString(6, address.getAdditionalInformation());
            preparedStatement.executeUpdate();

            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                String addressId = rs.getString(1);
                address.setId(addressId);
                return addressId;
            }
        }
        return null;
    }

    public static Address getAddressById(String id, Connection connection) throws SQLException {
        Address address = null;
        String selectAddressSql = "SELECT * FROM addresses WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectAddressSql)) {
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                address = new Address(
                        resultSet.getString("id"),
                        resultSet.getString("street"),
                        resultSet.getString("house_number"),
                        resultSet.getString("postal_code"),
                        resultSet.getString("city"),
                        resultSet.getString("country"),
                        resultSet.getString("additional_information")
                );
            }
        }
        return address;
    }

    public static void updateAddress(Address address, Connection connection) throws SQLException {
        String updateAddressSql = "UPDATE addresses SET street = ?, house_number = ?, postal_code = ?, city = ?, country = ?, additional_information = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateAddressSql)) {
            preparedStatement.setString(1, address.getStreet());
            preparedStatement.setString(2, address.getHouseNumber());
            preparedStatement.setString(3, address.getPostalCode());
            preparedStatement.setString(4, address.getCity());
            preparedStatement.setString(5, address.getCountry());
            preparedStatement.setString(6, address.getAdditionalInformation());
            preparedStatement.setString(7, address.getId());
            preparedStatement.executeUpdate();
        }
    }

    public static void deleteAddress(String id, Connection connection) throws SQLException {
        String deleteAddressSql = "DELETE FROM addresses WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteAddressSql)) {
            preparedStatement.setString(1, id);
            preparedStatement.executeUpdate();
        }
    }

    public static void deleteAllAddresses(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM addresses;");
        }
    }
}
