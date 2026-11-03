
package quickbite_backend.controller;

import quickbite_backend.model.CartItem;
import quickbite_backend.service.CartService;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:5174")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // Get user's cart
    @GetMapping("/{userId}")
    public List<CartItem> getCart(@PathVariable Integer userId) {
        return cartService.getCart(userId);
    }

    // Add food to cart
    @PostMapping("/{userId}/add/{foodId}")
    public CartItem addToCart(
            @PathVariable Integer userId,
            @PathVariable Integer foodId) {

        return cartService.addToCart(userId, foodId);
    }
 // Update cart item quantity
    @PutMapping("/{userId}/update/{foodId}/{quantity}")
    public CartItem updateCartQuantity(
            @PathVariable Integer userId,
            @PathVariable Integer foodId,
            @PathVariable Integer quantity) {

        return cartService.updateCartQuantity(userId, foodId, quantity);
    }

    // Remove food from cart
    @DeleteMapping("/{userId}/remove/{foodId}")
    public String removeFromCart(
            @PathVariable Integer userId,
            @PathVariable Integer foodId) {

        cartService.removeFromCart(userId, foodId);
        return "Food removed from cart successfully";
    }
    @GetMapping("/{userId}/total")
    public Double getCartTotal(@PathVariable Integer userId) {
        return cartService.getCartTotal(userId);
    }
}
