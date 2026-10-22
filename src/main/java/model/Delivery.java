package model;

public class Delivery {

    private int id;
    private Package packageItem;
    private Customer customer;
    private Courier courier;
    private DeliveryStatus status;

    public Delivery(int id, Package packageItem, Customer customer) {
        this.id = id;
        this.packageItem = packageItem;
        this.customer = customer;
        this.status = DeliveryStatus.CREATED;
    }

    public int getId() {
        return id;
    }

    public Package getPackageItem() {
        return packageItem;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Courier getCourier() {
        return courier;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }
}