package gift.controller;

import gift.dto.CreateWishRequest;
import gift.dto.LoginMember;
import gift.entity.Wish;
import gift.service.WishService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishes")
public class WishRestController {

    private final WishService wishService;

    public WishRestController(WishService wishService) {
        this.wishService = wishService;
    }

    @PostMapping
    public ResponseEntity<Object> addWish(LoginMember member,
            @RequestBody CreateWishRequest request) {
        wishService.addWish(member.getId(), request.productId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteWish(LoginMember member,
            @RequestBody CreateWishRequest request) {
        wishService.removeWish(member.getId(), request.productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<List<Wish>> getMyWishes(LoginMember loginMember) {
        List<Wish> wishes = wishService.getAllWish(loginMember.getId());
        return ResponseEntity.ok(wishes);
    }

}
