package gift.controller;

import gift.annotation.LoginMember;
import gift.dto.WishRequestDto;
import gift.dto.WishResponseDto;
import gift.entity.Member;
import gift.service.WishService;
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

    @PostMapping
    public ResponseEntity<WishResponseDto> createWish(
            @RequestBody WishRequestDto request,
            @LoginMember Member member
    ) {
        WishResponseDto created = wishService.createWish(member.getId(), request.productId());
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<WishResponseDto>> findAllWish(
            @LoginMember Member member
    ) {
        List<WishResponseDto> result = wishService.findAllWishesByMemberId(member.getId());
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{wishId}")
    public ResponseEntity<Void> deleteWish(
            @PathVariable Long wishId,
            @LoginMember Member member
    ) {
        wishService.deleteWish(member.getId(), wishId);
        return ResponseEntity.noContent().build();
    }
}
