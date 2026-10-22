package service;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import model.Delivery;
import model.DeliveryStatus;
import repository.DeliveryRepository;

import java.util.ArrayList;
import java.util.List;

public class DeliveryService {

    private final DeliveryRepository repository;
    private final List<String> statusHistory = new ArrayList<>();

    public DeliveryService(DeliveryRepository repository) {
        this.repository = Preconditions.checkNotNull(
                repository, "DeliveryRepository не может быть null");
    }

    public void createDelivery(Delivery delivery) {
        Preconditions.checkNotNull(delivery, "Delivery не может быть null");
        repository.save(delivery);
        statusHistory.add("CREATED (id=" + delivery.getId() + ")");
    }

    public Delivery getDelivery(int id) {
        return repository.findById(id);
    }

    public void changeStatus(int deliveryId, DeliveryStatus newStatus) {
        Delivery delivery = repository.findById(deliveryId);

        if (delivery == null) {
            throw new IllegalArgumentException("Доставка не найдена");
        }

        DeliveryStatus currentStatus = delivery.getStatus();

        if (!isValidTransition(currentStatus, newStatus)) {
            throw new IllegalStateException(
                    "Недопустимый переход статуса: "
                            + currentStatus + " -> " + newStatus);
        }

        delivery.setStatus(newStatus);
        statusHistory.add(currentStatus + " -> " + newStatus
                + " (id=" + deliveryId + ")");
    }

    public ImmutableList<String> getStatusHistory() {
        return ImmutableList.copyOf(statusHistory);
    }

    private boolean isValidTransition(
            DeliveryStatus currentStatus,
            DeliveryStatus newStatus) {

        if (currentStatus == DeliveryStatus.CREATED
                && newStatus == DeliveryStatus.ASSIGNED) {
            return true;
        }

        if (currentStatus == DeliveryStatus.ASSIGNED
                && newStatus == DeliveryStatus.IN_TRANSIT) {
            return true;
        }

        if (currentStatus == DeliveryStatus.IN_TRANSIT
                && newStatus == DeliveryStatus.DELIVERED) {
            return true;
        }

        if ((currentStatus == DeliveryStatus.CREATED
                || currentStatus == DeliveryStatus.ASSIGNED)
                && newStatus == DeliveryStatus.CANCELLED) {
            return true;
        }

        return false;
    }
}