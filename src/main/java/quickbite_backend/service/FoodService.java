
package quickbite_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import quickbite_backend.model.Food;
import quickbite_backend.repository.FoodRepository;

@Service
public class FoodService {

    private final FoodRepository foodRepository;

    public FoodService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    public List<Food> getAllFoods() {
        return foodRepository.findAll();
    }

    public Food addFood(Food food) {
        return foodRepository.save(food);
    }

    public Food updateFood(Integer foodId, Food updatedFood) {

        Food existingFood = foodRepository.findById(foodId)
                .orElseThrow(() -> new RuntimeException("Food not found"));

        existingFood.setName(updatedFood.getName());
        existingFood.setPrice(updatedFood.getPrice());
        existingFood.setCategory(updatedFood.getCategory());
        existingFood.setImageUrl(updatedFood.getImageUrl());
        existingFood.setRating(updatedFood.getRating());
        existingFood.setPrepTime(updatedFood.getPrepTime());

        return foodRepository.save(existingFood);
    }

    public void deleteFood(Integer foodId) {

        Food existingFood = foodRepository.findById(foodId)
                .orElseThrow(() -> new RuntimeException("Food not found"));

        foodRepository.delete(existingFood);
    }
}
