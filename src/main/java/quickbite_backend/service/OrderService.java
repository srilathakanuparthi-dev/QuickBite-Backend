
package quickbite_backend.service;

import java.util.List;
import java.time.Duration;
import java.time.LocalDateTime;

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
    private void validateUser(Integer userId) {

        String loggedInUserId = org.springframework.security.core.context.SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        if (!loggedInUserId.equals(String.valueOf(userId))) {
            throw new RuntimeException("You are not allowed to access these orders");
        }
    }

    @Transactional
    public Order placeOrder(
            Integer userId,
            String customerName,
            String phone,
            String address,
            String paymentMethod) {

        validateUser(userId);

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
    @Transactional(readOnly = true)
    public List<quickbite_backend.dto.OrderResponse> getUserOrders(Integer userId) {

        validateUser(userId);

        return orderRepository.findByUser_UserId(userId)
                .stream()
                .map(order -> {

                    List<quickbite_backend.dto.OrderItemResponse> items =
                            orderItemRepository.findByOrder_OrderId(order.getOrderId())
                            .stream()
                            .map(item -> new quickbite_backend.dto.OrderItemResponse(
                                    item.getFood().getName(),
                                    item.getFood().getImageUrl(),
                                    item.getQuantity(),
                                    item.getPrice()
                            ))
                            .toList();

                    return new quickbite_backend.dto.OrderResponse(
                            order.getOrderId(),
                            order.getCustomerName(),
                            order.getPhone(),
                            order.getDeliveryAddress(),
                            order.getPaymentMethod(),
                            order.getTotalAmount(),
                            getCurrentOrderStatus(order),
                            order.getOrderDate(),
                            items
                    );
                })
                .toList();
    }
    private String getCurrentOrderStatus(Order order) {

        // Admin manually changed status
        if (Boolean.TRUE.equals(order.getManualStatus())) {
            return order.getOrderStatus();
        }

        if (order.getOrderDate() == null) {
            return "PLACED";
        }

        long minutes = Duration.between(
                order.getOrderDate(),
                LocalDateTime.now()
        ).toMinutes();

        if (minutes >= 5) {
            return "DELIVERED";
        } else if (minutes >= 3) {
            return "OUT_FOR_DELIVERY";
        } else if (minutes >= 1) {
            return "PREPARING";
        }

        return "PLACED";
    }
    @Transactional
    public Order updateOrderStatus(Integer orderId, String status) {

        if (!List.of(
                "PLACED",
                "PREPARING",
                "OUT_FOR_DELIVERY",
                "DELIVERED"
        ).contains(status)) {
            throw new IllegalArgumentException("Invalid order status");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));

        order.setOrderStatus(status);

        // Admin manually updated the order
        order.setManualStatus(true);

        return orderRepository.save(order);
    }
}
