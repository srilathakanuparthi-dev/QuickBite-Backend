package quickbite_backend.dto;

public class OrderItemResponse {

    private String foodName;
    private String imageUrl;
    private Integer quantity;
    private Double price;

    public OrderItemResponse(String foodName, String imageUrl,
                             Integer quantity, Double price) {
        this.foodName = foodName;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.price = price;
    }

    public String getFoodName() {
        return foodName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getPrice() {
        return price;
    }
}