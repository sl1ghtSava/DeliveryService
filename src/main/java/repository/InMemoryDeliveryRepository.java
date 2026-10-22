package repository;

import model.Delivery;

import java.util.ArrayList;
import java.util.List;

public class InMemoryDeliveryRepository implements DeliveryRepository {

    private final List<Delivery> deliveries = new ArrayList<>();

    @Override
    public void save(Delivery delivery) {
        deliveries.add(delivery);
    }

    @Override
    public Delivery findById(int id) {
        for (Delivery delivery : deliveries) {
            if (delivery.getId() == id) {
                return delivery;
            }
        }

        return null;
    }
}