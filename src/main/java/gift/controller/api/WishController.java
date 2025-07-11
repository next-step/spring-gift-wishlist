package gift.controller.api;

import gift.dto.WishRequest;
import gift.dto.WishResponse;
import gift.entity.Member;
import gift.login.Authenticated;
import gift.login.Login;
import gift.service.WishService;
import jakarta.validation.Valid;
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
@RequestMapping("/api/wishes")
@Authenticated
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping
    public ResponseEntity<List<WishResponse>> getWishes(@Login Member member) {
        List<WishResponse> wishes = wishService.getWishes(member);
        return ResponseEntity.ok(wishes);
    }

    @PostMapping
    public ResponseEntity<Void> addWish(@Valid @RequestBody WishRequest request, @Login Member member) {
        wishService.addWish(request, member);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{wishId}")
    public ResponseEntity<Void> deleteWish(@PathVariable("wishId") Long wishId) {
        wishService.deleteWish(wishId);
        return ResponseEntity.noContent().build();
    }
}