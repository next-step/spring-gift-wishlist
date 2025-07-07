package gift.entity;

public class Product {

    private Long id;
    private String name;
    private Long price;
    private String imageUrl;
    private MdApprovalStatus mdApproval;

    public Product(String name, Long price, String imageUrl) {
        validateName(name);
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Product(long id, String name, long price, String imageUrl, MdApprovalStatus mdApproved) {
        validateName(name);
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.mdApproval = mdApproved;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setId(Long productId) {
        this.id = productId;
    }

    public boolean isApproved() { return mdApproval.isApproved(); }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("상품명은 비어 있을 수 없습니다.");
        }
        if (name.length() > 15) {
            throw new IllegalArgumentException("상품명은 15자를 초과할 수 없습니다.");
        }
        String allowedPattern = "^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ()\\[\\]+\\-&/_\s]*$";
        if (!name.matches(allowedPattern)) {
            throw new IllegalArgumentException("상품명에 허용되지 않은 특수문자가 포함되어 있습니다.");
        }
    }
}
