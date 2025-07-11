package gift.controller;

import gift.annotation.LoginMember;
import gift.dto.WishRequestDto;
import gift.entity.Member;
import gift.service.WishService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/wishes")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @PostMapping
    public ResponseEntity<Void> addWish(
            @Valid @RequestBody WishRequestDto request,
            @LoginMember Member member) {
        wishService.addWish(member, request.getProductId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}