package gift.entity;


import jakarta.validation.constraints.Size;

public class Gift {
    private Long id;

    private Long giftId;

    @Size(min = 1, max = 15, message = "상품 명은 공백 포함 최대 15자까지 가능합니다.")
    private String giftName;

    private Integer giftPrice;

    private String giftPhotoUrl;

    private boolean isKakaoMDAccepted = true;

    public Gift(Long giftId, String giftName, Integer giftPrice, String giftPhotoUrl) {
        this.giftId = giftId;
        this.giftName = giftName;
        this.giftPrice = giftPrice;
        this.giftPhotoUrl = giftPhotoUrl;
    }

    public Gift() {}

    public Long getId() {
        return id;
    }

    public Long getGiftId() {
        return giftId;
    }

    public String getGiftName() {
        return giftName;
    }

    public Integer getGiftPrice() {
        return giftPrice;
    }

    public String getGiftPhotoUrl() {
        return giftPhotoUrl;
    }

    public boolean getIsKakaoMDAccepted() {
        return isKakaoMDAccepted;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setGiftId(Long giftId) {
        this.giftId = giftId;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public void setGiftPrice(Integer giftPrice) {
        this.giftPrice = giftPrice;
    }

    public void setGiftPhotoUrl(String giftPhotoUrl) {
        this.giftPhotoUrl = giftPhotoUrl;
    }

    public void setKakaoMDAccepted(boolean kakaoMDAccepted) {
        isKakaoMDAccepted = kakaoMDAccepted;
    }

    public boolean isGiftNameValid(){
        String pattern = "^[a-zA-Z0-9가-힣 ()\\[\\]\\+\\-\\&\\/\\_]*$";
        return giftName.matches(pattern);
    }

    public void isKakaoMessageInclude(){
        String censorshipWord = "카카오";
        if(giftName.contains(censorshipWord)){
            this.isKakaoMDAccepted = false;
        }
    }
}
