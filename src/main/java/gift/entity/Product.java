package gift.entity;

public record Product(Long id, String name, Integer price, String imageUrl, Status status) {
    public enum Status {
        APPROVED,
        PENDING,
        REJECTED
    }

    public Product(Long id, String name, int price, String imageUrl) {
        this(
                id,
                name,
                price,
                imageUrl,
                inferStatus(name)
        );
    }

    public static Status inferStatus(String name) {
        if (name.contains("카카오")) {
            return Product.Status.PENDING;
        } else {
            return Product.Status.APPROVED;
        }
    }
}
