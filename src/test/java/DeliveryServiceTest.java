import model.Customer;
import model.Package;
import model.Delivery;
import model.DeliveryStatus;
import repository.DeliveryRepository;
import repository.InMemoryDeliveryRepository;
import service.DeliveryService;

import com.google.common.collect.ImmutableList;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DeliveryServiceTest {

        private DeliveryService createService() {
                DeliveryRepository repository = new InMemoryDeliveryRepository();
                return new DeliveryService(repository);
        }

        private Delivery createDelivery() {
                Customer customer = new Customer(
                                1,
                                "Иван Иванов",
                                "ул. Ленина, 10",
                                "+79991234567");

                Package packageItem = new Package(
                                1,
                                "Документы",
                                0.5);

                return new Delivery(
                                1,
                                packageItem,
                                customer);
        }

        @Test
        void shouldChangeStatusSuccessfully() {
                DeliveryService service = createService();
                Delivery delivery = createDelivery();

                service.createDelivery(delivery);
                service.changeStatus(1, DeliveryStatus.ASSIGNED);

                assertEquals(DeliveryStatus.ASSIGNED, delivery.getStatus());
        }

        @Test
        void shouldRejectInvalidStatusChange() {
                DeliveryService service = createService();
                Delivery delivery = createDelivery();

                service.createDelivery(delivery);

                assertThrows(
                                IllegalStateException.class,
                                () -> service.changeStatus(1, DeliveryStatus.DELIVERED));
        }

        @Test
        void shouldRejectReturningToCreatedStatus() {
                DeliveryService service = createService();
                Delivery delivery = createDelivery();

                service.createDelivery(delivery);
                service.changeStatus(1, DeliveryStatus.ASSIGNED);
                service.changeStatus(1, DeliveryStatus.IN_TRANSIT);
                service.changeStatus(1, DeliveryStatus.DELIVERED);

                assertThrows(
                                IllegalStateException.class,
                                () -> service.changeStatus(1, DeliveryStatus.CREATED));
        }

        @Test
        void shouldReturnImmutableStatusHistory() {
                DeliveryService service = createService();
                Delivery delivery = createDelivery();

                service.createDelivery(delivery);
                service.changeStatus(1, DeliveryStatus.ASSIGNED);

                ImmutableList<String> history = service.getStatusHistory();

                assertNotNull(history);
                assertEquals(2, history.size());
                assertTrue(history.get(0).contains("CREATED"));
                assertTrue(history.get(1).contains("ASSIGNED"));

                assertThrows(
                                UnsupportedOperationException.class,
                                () -> history.add("попытка изменить"));
        }
}