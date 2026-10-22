import model.Customer;
import model.Package;
import model.Courier;
import model.Delivery;
import model.DeliveryStatus;
import repository.DeliveryRepository;
import repository.InMemoryDeliveryRepository;
import service.DeliveryService;

import com.google.common.collect.ImmutableList;

public class Main {

        public static void main(String[] args) {

                Customer customer = new Customer(
                                1,
                                "Иван Иванов",
                                "ул. Ленина, 10",
                                "+79991234567");

                Package packageItem = new Package(
                                1,
                                "Документы",
                                0.5);

                Courier courier = new Courier(
                                1,
                                "Алексей Петров");

                Delivery delivery = new Delivery(
                                1,
                                packageItem,
                                customer);

                DeliveryRepository repository = new InMemoryDeliveryRepository();
                DeliveryService service = new DeliveryService(repository);

                service.createDelivery(delivery);
                delivery.setCourier(courier);

                System.out.println("Текущий статус: " + delivery.getStatus());

                service.changeStatus(1, DeliveryStatus.ASSIGNED);
                System.out.println("Текущий статус: " + delivery.getStatus());

                service.changeStatus(1, DeliveryStatus.IN_TRANSIT);
                System.out.println("Текущий статус: " + delivery.getStatus());

                service.changeStatus(1, DeliveryStatus.DELIVERED);
                System.out.println("Текущий статус: " + delivery.getStatus());

                System.out.println("Проверка недопустимого перехода:");
                try {
                        service.changeStatus(1, DeliveryStatus.CREATED);
                } catch (IllegalStateException e) {
                        System.out.println("Ошибка: " + e.getMessage());
                }

                ImmutableList<String> history = service.getStatusHistory();
                System.out.println("\nИстория статусов через Guava):");
                history.forEach(System.out::println);
                System.out.println("Размер истории: " + history.size());
        }
}