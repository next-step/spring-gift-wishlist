package gift.product.entity;


public class Item {

	private final Long id;

	private final Long userId;

	private String name;

	private Integer price;

	private String imageUrl;

	public Item(Long id, Long userId, String name, Integer price, String imageUrl) {
		validateKakaoKeyword(name);

		this.id = id;
		this.userId = userId;
		this.name = name;
		this.price = price;
		this.imageUrl = imageUrl;
	}

	public Item(Long userId, String name, Integer price, String imageUrl) {
		this(null, userId, name, price, imageUrl);
	}

	private static void validateKakaoKeyword(String name){
		if (name.contains("카카오")){
			throw new IllegalArgumentException("'카카오'는 담당자와 협의 후 사용가능한 키워드입니다.");
		}
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Long getUserId() {
		return userId;
	}


	public Integer getPrice() {
		return price;
	}


	public void setPrice(Integer price) {
		this.price = price;
	}


	public String getImageUrl() {
		return imageUrl;
	}


	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

}
