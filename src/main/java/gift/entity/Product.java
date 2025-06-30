package gift.entity;

import java.util.HashMap;
import java.util.Map;

public class Product {

    private final Map<String, Boolean> modifiedFlags = new HashMap<>(
            Map.of(
                "name", false,
                "price", false,
                "imageUrl", false
            ));

    private Long id;
    private String name;
    private Long price;
    private String imageUrl;

    public Product() { }

    public Product(Long id, String name, Long price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        modifiedFlags.put("name", true);
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }
    public void setPrice(Long price) {
        modifiedFlags.put("price", true);
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.modifiedFlags.put("imageUrl", true);
        this.imageUrl = imageUrl;
    }

    public Object getFieldValue(String fieldName) {
        return switch (fieldName) {
            case "id" -> id;
            case "name" -> name;
            case "price" -> price;
            case "imageUrl" -> imageUrl;
            default -> throw new IllegalArgumentException("찾을 수 없는 필드 값 입니다. :  " + fieldName);
        };
    }

    public Map<String, Boolean> getModifiedInfo() {
        return modifiedFlags;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Product objProd)) return false;

        return id != null && id.equals(objProd.id) &&
               name != null && name.equals(objProd.name) &&
               price != null && price.equals(objProd.price) &&
               imageUrl != null && imageUrl.equals(objProd.imageUrl);
    }
}
