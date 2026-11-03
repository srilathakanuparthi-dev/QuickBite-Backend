package quickbite_backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import quickbite_backend.model.Food;
import quickbite_backend.service.FoodService;

@CrossOrigin(origins = "http://localhost:5174")
@RestController
@RequestMapping("/api/foods")
public class FoodController {
    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping
    public List<Food> getAllFoods() {
        return foodService.getAllFoods();
    }
    @PostMapping
    public Food addFood(@RequestBody Food food) {
        return foodService.addFood(food);
    }
    @PutMapping("/{foodId}")
    public Food updateFood(
            @PathVariable Integer foodId,
            @RequestBody Food food) {

        return foodService.updateFood(foodId, food);
    }
    @DeleteMapping("/{foodId}")
    public ResponseEntity<Void> deleteFood(
            @PathVariable Integer foodId) {

        foodService.deleteFood(foodId);
        return ResponseEntity.noContent().build();
    }
}