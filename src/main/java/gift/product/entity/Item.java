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

	public static ItemBuilder builder() {
		return new ItemBuilder();
	}

	public static class ItemBuilder {
		private Long userId;
		private String name;
		private Integer price;
		private String imageUrl;

		public ItemBuilder userId(Long userId){
			this.userId = userId;
			return this;
		}
		public ItemBuilder name(String name){
			this.name = name;
			return this;
		}
		public ItemBuilder price(Integer price){
			this.price = price;
			return this;
		}
		public ItemBuilder imageUrl(String imageUrl){
			this.imageUrl = imageUrl;
			return this;
		}
		public Item build() {
			return new Item(null, userId, name, price, imageUrl);
		}
	}

	private void validateKakaoKeyword(String name){
		if (name.contains("카카오")){
			throw new IllegalArgumentException("'카카오'는 담당자와 협의 후 사용가능한 키워드입니다.");
		}
	}

	public void isItemAuthor(Long userId) {
		if(this.userId != userId){
			throw new IllegalArgumentException("작성자만 수정,삭제 가능합니다.");
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
