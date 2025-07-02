package gift.dto;

public class UpdateProductRequestDto extends ProductRequestBase {
    private Long id;

    public UpdateProductRequestDto() {
        super();
    }

    public UpdateProductRequestDto(Long id, String name, Long price, String imageUrl) {
        super(name, price, imageUrl);
        this.id = id;
    }

    public UpdateProductRequestDto(Long id, String name, Long price, String imageUrl, boolean isMdApproved) {
        super(name, price, imageUrl, isMdApproved);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
