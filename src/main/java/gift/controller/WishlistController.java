package gift.controller;

import gift.annotation.LoginMember;
import gift.dto.LoginMemberDto;
import gift.dto.WishRequest;
import gift.dto.WishResponse;
import gift.service.WishlistService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishes")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping()
    public ResponseEntity<WishResponse> create(@Valid @RequestBody WishRequest request, @LoginMember LoginMemberDto loginMemberDto) {
        WishResponse response = wishlistService.create(request, loginMemberDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
