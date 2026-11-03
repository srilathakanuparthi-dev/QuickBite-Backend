package quickbite_backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import quickbite_backend.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByUser_UserId(Integer userId);

}