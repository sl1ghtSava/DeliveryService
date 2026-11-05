package service;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import model.Delivery;
import model.DeliveryStatus;
import repository.DeliveryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DeliveryService {

    private static final Logger logger = LoggerFactory.getLogger(DeliveryService.class);

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
        logger.info("Создана доставка id={}", delivery.getId());
    }

    public Delivery getDelivery(int id) {
        return repository.findById(id);
    }

    public void changeStatus(int deliveryId, DeliveryStatus newStatus) {
        Delivery delivery = repository.findById(deliveryId);

        if (delivery == null) {
            logger.error("Доставка с id={} не найдена", deliveryId);
            throw new IllegalArgumentException("Доставка не найдена");
        }

        DeliveryStatus currentStatus = delivery.getStatus();

        if (!isValidTransition(currentStatus, newStatus)) {
            logger.warn("Некорректный переход статуса доставки id={}: {} -> {}",
                    deliveryId, currentStatus, newStatus);
            throw new IllegalStateException(
                    "Недопустимый переход статуса: " + currentStatus + " -> " + newStatus);
        }

        delivery.setStatus(newStatus);
        statusHistory.add(currentStatus + " -> " + newStatus + " (id=" + deliveryId + ")");
        logger.info("Статус доставки id={} успешно изменён: {} -> {}",
                deliveryId, currentStatus, newStatus);
    }

    public ImmutableList<String> getStatusHistory() {
        return ImmutableList.copyOf(statusHistory);
    }

    private boolean isValidTransition(DeliveryStatus currentStatus, DeliveryStatus newStatus) {
        if (currentStatus == DeliveryStatus.CREATED && newStatus == DeliveryStatus.ASSIGNED) {
            return true;
        }
        if (currentStatus == DeliveryStatus.ASSIGNED && newStatus == DeliveryStatus.IN_TRANSIT) {
            return true;
        }
        if (currentStatus == DeliveryStatus.IN_TRANSIT && newStatus == DeliveryStatus.DELIVERED) {
            return true;
        }
        if ((currentStatus == DeliveryStatus.CREATED || currentStatus == DeliveryStatus.ASSIGNED)
                && newStatus == DeliveryStatus.CANCELLED) {
            return true;
        }
        return false;
    }

    public String processDelivery(String orderId, String status) {
        if (status == null || status.isEmpty()) {
            logger.warn("Попытка обработать доставку {} с пустым статусом", orderId);
            return "Invalid status";
        }
        if ("DELIVERED".equalsIgnoreCase(status)) {
            logger.info("Доставка {} успешно завершена", orderId);
            return "Order " + orderId + " successfully delivered";
        }
        logger.info("Доставка {} получила статус {}", orderId, status);
        return "Order " + orderId + " status: " + status;
    }
}