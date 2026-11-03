package quickbite_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import quickbite_backend.model.Food;

public interface FoodRepository extends JpaRepository<Food, Integer> {

}