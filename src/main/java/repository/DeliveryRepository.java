package repository;

import model.Delivery;

public interface DeliveryRepository {

    void save(Delivery delivery);

    Delivery findById(int id);
}