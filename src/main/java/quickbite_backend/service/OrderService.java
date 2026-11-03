
package quickbite_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import quickbite_backend.model.*;
import quickbite_backend.repository.*;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    public OrderService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            CartRepository cartRepository,
            UserRepository userRepository) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Order placeOrder(
            Integer userId,
            String customerName,
            String phone,
            String address,
            String paymentMethod) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<CartItem> cartItems = cartRepository.findByUser_UserId(userId);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setCustomerName(customerName);
        order.setPhone(phone);
        order.setDeliveryAddress(address);
        order.setPaymentMethod(paymentMethod);

        double total = cartItems.stream()
                .mapToDouble(item ->
                    item.getFood().getPrice() * item.getQuantity())
                .sum();

        order.setTotalAmount(total);

        Order savedOrder = orderRepository.save(order);

        for (CartItem cartItem : cartItems) {

            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(savedOrder);
            orderItem.setFood(cartItem.getFood());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getFood().getPrice());

            orderItemRepository.save(orderItem);
        }

        cartRepository.deleteAll(cartItems);

        return savedOrder;
    }
    public List<Order> getUserOrders(Integer userId) {
        return orderRepository.findByUser_UserId(userId);
    }
}
