package gift.controller;

import gift.annotation.LoginMember;
import gift.domain.WishList;
import gift.dto.MemberResponse;
import gift.dto.WishListRequest;
import gift.dto.WishListResponse;
import gift.service.WishListService;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/wishlists")
public class WishListController {

    private final WishListService wishListService;

    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @GetMapping
    public ResponseEntity<List<WishListResponse>> getAllWithProduct(
            @LoginMember MemberResponse member) {
        List<WishListResponse> wishLists = wishListService.getAllWithProductByMemberId(member.id());
        return ResponseEntity.ok(wishLists);
    }

    @PostMapping
    public ResponseEntity<WishList> createOrUpdate(
            @LoginMember MemberResponse member,
            @RequestBody WishListRequest request
    ) {
        WishList created = wishListService.createOrUpdate(member.id(), request);
        URI location = URI.create("/api/products/" + created.id());

        return ResponseEntity.created(location)
                .body(created);
    }

    @DeleteMapping("/{wishlistId}")
    public ResponseEntity<Void> delete(
            @LoginMember MemberResponse member,
            @PathVariable Long wishlistId) {
        wishListService.delete(member.id(), wishlistId);
        return ResponseEntity.noContent().build();
    }
}
