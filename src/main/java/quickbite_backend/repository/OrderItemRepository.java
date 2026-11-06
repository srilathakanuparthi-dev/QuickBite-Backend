package quickbite_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import quickbite_backend.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    List<OrderItem> findByOrder_OrderId(Integer orderId);

}