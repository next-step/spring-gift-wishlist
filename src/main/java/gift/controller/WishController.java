package gift.controller;

import gift.common.annotation.CurrentMember;
import gift.common.dto.request.AddWishRequest;
import gift.common.dto.response.WishDto;
import gift.domain.member.Member;
import gift.service.WishService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishes")
public class WishController {
    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @PostMapping("/add")
    public ResponseEntity<WishDto> addWish(@CurrentMember Member member,
                                        @RequestBody @Valid AddWishRequest request) {
        WishDto response = wishService.handleAddWishRequest(member, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<WishDto>> getMyWishList(@CurrentMember Member member) {
        List<WishDto> response = wishService.handleGetMyWishList(member);
        return ResponseEntity.ok(response);
    }
}
