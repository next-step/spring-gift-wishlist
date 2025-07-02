package gift.dto;

public class CreateProductRequestDto extends ProductRequestBase {
    public CreateProductRequestDto() {
        super();
    }

    public CreateProductRequestDto(String name, Long price, String imageUrl) {
        super(name, price, imageUrl);
    }

    public CreateProductRequestDto(String name, Long price, String imageUrl, boolean isMdApproved) {
        super(name, price, imageUrl, isMdApproved);
    }
}
