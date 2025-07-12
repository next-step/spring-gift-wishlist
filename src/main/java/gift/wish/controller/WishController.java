package gift.wish.controller;

import gift.security.annotation.LoginMember;
import gift.member.dto.AuthenticatedMemberDto;
import gift.wish.dto.WishRequestDto;
import gift.wish.dto.WishResponseDto;
import gift.wish.service.WishService;
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
            @LoginMember AuthenticatedMemberDto authenticatedMemberDto
    ) {
        WishResponseDto created = wishService.createWish(authenticatedMemberDto.id(), request.productId());
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<WishResponseDto>> findAllWish(
            @LoginMember AuthenticatedMemberDto authenticatedMemberDto
    ) {
        List<WishResponseDto> result = wishService.findAllWishesByMemberId(authenticatedMemberDto.id());
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{wishId}")
    public ResponseEntity<Void> deleteWish(
            @PathVariable Long wishId,
            @LoginMember AuthenticatedMemberDto authenticatedMemberDto
            ) {
        wishService.deleteWish(authenticatedMemberDto.id(), wishId);
        return ResponseEntity.noContent().build();
    }
}
