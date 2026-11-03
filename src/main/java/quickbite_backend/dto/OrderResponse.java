package quickbite_backend.dto;

import java.time.LocalDateTime;

public class OrderResponse {

    private Integer orderId;
    private String customerName;
    private String phone;
    private String deliveryAddress;
    private String paymentMethod;
    private Double totalAmount;
    private String orderStatus;
    private LocalDateTime orderDate;

    public OrderResponse(Integer orderId, String customerName,
            String phone, String deliveryAddress,
            String paymentMethod, Double totalAmount,
            String orderStatus, LocalDateTime orderDate) {

        this.orderId = orderId;
        this.customerName = customerName;
        this.phone = phone;
        this.deliveryAddress = deliveryAddress;
        this.paymentMethod = paymentMethod;
        this.totalAmount = totalAmount;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
    }

    public Integer getOrderId() { return orderId; }
    public String getCustomerName() { return customerName; }
    public String getPhone() { return phone; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public String getPaymentMethod() { return paymentMethod; }
    public Double getTotalAmount() { return totalAmount; }
    public String getOrderStatus() { return orderStatus; }
    public LocalDateTime getOrderDate() { return orderDate; }
}