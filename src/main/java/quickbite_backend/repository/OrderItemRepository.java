package quickbite_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import quickbite_backend.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

}