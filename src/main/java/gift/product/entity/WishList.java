package gift.product.entity;


public class WishList {
	private final Long id;
	private final Long userId;
	private final Long itemId;
	private final int amount;

	public WishList(Long id, Long userId, Long itemId, int amount) {
		this.id = id;
		this.userId = userId;
		this.itemId = itemId;
		this.amount = amount;
	}

	public WishList(Long userId, Long itemId, int amount) {
		this(null, userId, itemId, amount);
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

	public int getAmount() { return amount; }

}
