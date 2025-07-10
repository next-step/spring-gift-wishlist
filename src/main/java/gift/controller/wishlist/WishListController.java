package gift.controller.wishlist;

import gift.dto.product.ProductResponseDto;
import gift.entity.LoginMember;
import gift.entity.Member;
import gift.service.wishlist.WishListService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishes")
public class WishListController {
    private final WishListService wishListService;

    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @PostMapping("/{productId}")
    public ResponseEntity<Void> create(
        @PathVariable Long productId,
        @LoginMember Member member
    ) {
        wishListService.create(productId, member.getId());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> findAll(
        @LoginMember Member member
    ) {
        return new ResponseEntity<>(wishListService.findAll(member.getId()), HttpStatus.OK);
    }
}