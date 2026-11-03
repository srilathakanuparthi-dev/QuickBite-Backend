
package quickbite_backend.repository;

import quickbite_backend.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<CartItem, Integer> {

    List<CartItem> findByUser_UserId(Integer userId);

    Optional<CartItem> findByUser_UserIdAndFood_FoodId(
        Integer userId,
        Integer foodId
    );

    void deleteByUser_UserIdAndFood_FoodId(
        Integer userId,
        Integer foodId
    );
}
