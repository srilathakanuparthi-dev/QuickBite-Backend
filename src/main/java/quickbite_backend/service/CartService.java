
package quickbite_backend.service;

import quickbite_backend.model.CartItem;
import quickbite_backend.model.User;
import quickbite_backend.model.Food;
import quickbite_backend.repository.CartRepository;
import quickbite_backend.repository.UserRepository;
import quickbite_backend.repository.FoodRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final FoodRepository foodRepository;

    public CartService(
            CartRepository cartRepository,
            UserRepository userRepository,
            FoodRepository foodRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.foodRepository = foodRepository;
    }
    
    private void validateUser(Integer userId) {

        String loggedInUserId = org.springframework.security.core.context.SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        if (!loggedInUserId.equals(String.valueOf(userId))) {
            throw new RuntimeException("You are not allowed to access this cart");
        }
    }

    public List<CartItem> getCart(Integer userId) {

        validateUser(userId);

        return cartRepository.findByUser_UserId(userId);
    }

    @Transactional
    public CartItem addToCart(Integer userId, Integer foodId) {

        validateUser(userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        

        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new RuntimeException("Food not found"));

        CartItem item = cartRepository
                .findByUser_UserIdAndFood_FoodId(userId, foodId)
                .orElse(null);

        if (item != null) {
            item.setQuantity(item.getQuantity() + 1);
        } else {
            item = new CartItem();
            item.setUser(user);
            item.setFood(food);
            item.setQuantity(1);
        }

        return cartRepository.save(item);
    }

    @Transactional
    public void removeFromCart(Integer userId, Integer foodId) {

        validateUser(userId);

        cartRepository.deleteByUser_UserIdAndFood_FoodId(userId, foodId);
    }
    public Double getCartTotal(Integer userId) {

        validateUser(userId);

        List<CartItem> items = cartRepository.findByUser_UserId(userId);

        return items.stream()
                .mapToDouble(item ->
                    item.getFood().getPrice() * item.getQuantity()
                )
                .sum();
    }
    @Transactional
    public CartItem updateCartQuantity(Integer userId, Integer foodId, Integer quantity) {

        validateUser(userId);

        CartItem cartItem = cartRepository
                .findByUser_UserIdAndFood_FoodId(userId, foodId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (quantity <= 0) {
            cartRepository.delete(cartItem);
            return null;
        }

        cartItem.setQuantity(quantity);

        return cartRepository.save(cartItem);
    }
}
