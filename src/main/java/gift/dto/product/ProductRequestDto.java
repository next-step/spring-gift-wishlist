package gift.dto.product;

import jakarta.validation.constraints.*;


public class ProductRequestDto {
    private Long id;
    private boolean usableKakao;

    @NotBlank(message = "상품명을 입력해주세요.")
    @Size(max=15, message = "상품명의 길이는 15이하로 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣\\s()\\[\\]+\\-&/_]*$",
            message = "허용되지 않는 입력 방식을 사용하셨습니다. (가능한 특수 기호: ( ), [ ], +, -, &, /, _)")
    private String name;

    @NotNull(message = "상품의 가격을 입력해주세요.")
    @Min(value = 100, message = "상품의 가격은 최소 100원입니다.")
    private int price;

    private String imageUrl;

    @AssertTrue(message = "'카카오'가 포함된 상품명은 담당 MD와 협의가 필요합니다.")
    private boolean isKakaoPolicyCompliant() {
        if (name == null || !name.contains("카카오")) {
            return true;
        }
        return usableKakao;
    }

    public ProductRequestDto() {}
    public ProductRequestDto(Long id, String name, int price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public int getPrice() {return price;}
    public void setPrice(int price) {this.price = price;}
    public String getImageUrl() {return imageUrl;}
    public void setImageUrl(String imageUrl) {this.imageUrl = imageUrl;}
    public boolean getUsableKakao() {return usableKakao;}
    public void setUsableKakao(boolean usableKakao) {this.usableKakao = usableKakao;}
}