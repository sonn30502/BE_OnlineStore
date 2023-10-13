package com.example.online_farm.DTO.Cart;

public enum PurchaseStatus {
    IN_CART(-1, "Sản phẩm đang trong giỏ hàng"),
    ALL_PRODUCTS(0, "Tất cả sản phẩm"),
    WAITING_FOR_CONFIRMATION(1, "Sản phẩm đang đợi xác nhận từ chủ shop"),
    BEING_PREPARED(2, "Sản phẩm đang được lấy hàng"),
    BEING_SHIPPED(3, "Sản phẩm đang vận chuyển"),
    DELIVERED(4, "Sản phẩm đã được giao"),
    CANCELLED(5, "Sản phẩm đã bị hủy");

    private final int code;
    private final String description;

    PurchaseStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static PurchaseStatus fromCode(int code) {
        for (PurchaseStatus status : PurchaseStatus.values()) {
            if (status.code == code) {
                return status;
            }
        }
        return null; // Hoặc ném một exception nếu không tìm thấy
    }
}
