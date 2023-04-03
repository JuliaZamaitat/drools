package models;

import java.util.Date;
import java.util.List;

public class ProductOrder {
    private String id;
    private Client client;
    private Address deliveryAddress;
    private List<Item> items;
    private Date orderDate;
    private Date orderProcessDate;


    public Date getOrderProcessDate() {
        return orderProcessDate;
    }

    public void setOrderProcessDate(Date orderProcessDate) {
        this.orderProcessDate = orderProcessDate;
    }


    public ProductOrder(String id, Client client, Address deliveryAddress, List<Item> items, Date orderDate) {
        this.id = id;
        this.client = client;
        this.deliveryAddress = deliveryAddress;
        this.items = items;
        this.orderDate = orderDate;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
}
