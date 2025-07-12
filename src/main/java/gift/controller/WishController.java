package gift.controller;

import gift.common.annotation.CurrentMember;
import gift.common.dto.request.AddWishRequest;
import gift.domain.member.Member;
import gift.domain.wish.Wish;
import gift.service.WishService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishes")
public class WishController {
    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @PostMapping("/add")
    public ResponseEntity<Wish> addWish(@CurrentMember Member member,
                                        @RequestBody @Valid AddWishRequest request) {
        Wish response = wishService.handleAddWishRequest(member, request);
        return ResponseEntity.ok(response);
    }
}
