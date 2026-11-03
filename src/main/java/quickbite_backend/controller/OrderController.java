
package quickbite_backend.controller;
import java.util.List;
import java.util.Map;
import quickbite_backend.dto.OrderResponse;

import org.springframework.web.bind.annotation.*;

import quickbite_backend.model.Order;
import quickbite_backend.service.OrderService;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:5174")
public class OrderController {

    private final OrderService orderService;
    @GetMapping("/user/{userId}")
    public List<OrderResponse> getUserOrders(@PathVariable Integer userId) {

        return orderService.getUserOrders(userId)
                .stream()
                .map(order -> new OrderResponse(
                        order.getOrderId(),
                        order.getCustomerName(),
                        order.getPhone(),
                        order.getDeliveryAddress(),
                        order.getPaymentMethod(),
                        order.getTotalAmount(),
                        order.getOrderStatus(),
                        order.getOrderDate()
                ))
                .toList();
    }

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/{userId}")
    public Map<String, Object> placeOrder(
            @PathVariable Integer userId,
            @RequestBody Map<String, String> request) {

        Order order = orderService.placeOrder(
                userId,
                request.get("customerName"),
                request.get("phone"),
                request.get("address"),
                request.get("paymentMethod")
        );

        return Map.of(
                "message", "Order placed successfully!",
                "orderId", order.getOrderId(),
                "totalAmount", order.getTotalAmount(),
                "paymentMethod", order.getPaymentMethod(),
                "orderStatus", order.getOrderStatus()
        );
    }
    
}
