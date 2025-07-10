package gift.product.entity;


public class WishList {
	private final Long id;
	private final Long userId;
	private final Long itemId;

	public WishList(Long id, Long userId, Long itemId) {
		this.id = id;
		this.userId = userId;
		this.itemId = itemId;
	}

	public WishList(Long userId, Long itemId) {
		this(null, userId, itemId);
	}

	public Long getId() {
		return id;
	}

	public Long getUserId() {
		return userId;
	}

	public Long getItemId() {
		return itemId;
	}
}
