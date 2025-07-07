package gift.entity;

import gift.dto.ProductRequestDto;

public record Product(Long id, String name, Integer price, String imageUrl, Status status) {
    public enum Status {
        APPROVED,
        PENDING,
        REJECTED
    }

    public  Product(Long id, ProductRequestDto productRequestDto) {
        this(
                id,
                productRequestDto.name(),
                productRequestDto.price(),
                productRequestDto.imageUrl(),
                inferStatus(productRequestDto)
        );
    }

    public static Status inferStatus(ProductRequestDto productRequestDto) {
        if (productRequestDto.name().contains("카카오")) {
            return Product.Status.PENDING;
        } else {
            return Product.Status.APPROVED;
        }
    }
}
