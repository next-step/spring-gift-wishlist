package gift.product.controller;


import gift.product.commons.annotations.Authenticated;
import gift.product.dto.CreateWishListRequest;
import gift.product.entity.WishList;
import gift.product.service.WishListService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/wishlists")
public class WishListController {
	private final WishListService wishListService;
	public WishListController(WishListService wishListService) {
		this.wishListService = wishListService;
	}

	@GetMapping
	@Authenticated
	public List<WishList> getWishList(@RequestAttribute Long userId) {
		return wishListService.getWishList(userId);
	}

	@PostMapping
	@Authenticated
	public Long createWishList(@RequestAttribute Long userId, @RequestBody CreateWishListRequest request) {
		return wishListService.createWishList(userId, request);
	}

	@DeleteMapping("/{itemId}")
	@Authenticated
	public void deleteWishList(@RequestAttribute Long userId, @PathVariable Long itemId) {
		wishListService.deleteWishList(userId, itemId);
	}

	@PatchMapping("/{itemId}/increase")
	@Authenticated
	public void increaseWishList(@RequestAttribute Long userId, @PathVariable Long itemId) {
		wishListService.increaseAmount(userId, itemId);
	}

	@PatchMapping("/{itemId}/decrease")
	@Authenticated
	public void decreaseWishList(@RequestAttribute Long userId, @PathVariable Long itemId) {
		wishListService.decreaseAmount(userId, itemId);
	}
}
