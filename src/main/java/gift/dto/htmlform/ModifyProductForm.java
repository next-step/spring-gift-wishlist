package gift.dto.htmlform;

public class ModifyProductForm {
    
    private String name;
    private Long price;
    private String imageUrl;
    private Boolean isMDOK;
    
    public ModifyProductForm() {
        this(null, null, null, false);
    }
    
    public ModifyProductForm(String name, Long price, String imageUrl, Boolean isMDOK) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isMDOK = isMDOK;
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
    
    public Boolean getMDOK() {
        return isMDOK;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setPrice(Long price) {
        this.price = price;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public void setMDOK(Boolean MDOK) {
        isMDOK = MDOK;
    }
}
