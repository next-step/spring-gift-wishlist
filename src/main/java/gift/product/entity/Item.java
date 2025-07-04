package gift.product.entity;


public class Item {

	private Long id;

	private String name;

	private Integer price;

	private String imageUrl;

	public Item(Long id, String name, Integer price, String imageUrl) {
		validateKakaoKeyword(name);

		this.id = id;
		this.name = name;
		this.price = price;
		this.imageUrl = imageUrl;
	}

	public Item(String name, Integer price, String imageUrl) {
		this(null, name, price, imageUrl);
	}

	public static void validateKakaoKeyword(String name){
		if (name.contains("카카오"))
			throw new IllegalArgumentException("'카카오'는 담당자와 협의 후 사용가능한 키워드입니다.");
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
