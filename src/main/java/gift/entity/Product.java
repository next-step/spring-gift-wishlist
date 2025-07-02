package gift.entity;

public record Product(Long id, String name, Integer price, String imageUrl, Status status) {
    public enum Status {
        APPROVED,
        PENDING,
        REJECTED
    }
}
