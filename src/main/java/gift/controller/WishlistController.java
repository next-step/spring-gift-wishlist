package gift.controller;

import gift.annotation.LoginMember;
import gift.dto.LoginMemberDto;
import gift.dto.WishRequest;
import gift.dto.WishResponse;
import gift.model.Product;
import gift.service.WishlistService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<WishResponse> create(@Valid @RequestBody WishRequest request,
        @LoginMember LoginMemberDto memberDto) {
        WishResponse response = wishlistService.create(request, memberDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping()
    public ResponseEntity<List<WishResponse>> findAll(@LoginMember LoginMemberDto memberDto) {
        List<WishResponse> wishResponses = wishlistService.findAllByMemberId(memberDto);
        return ResponseEntity.status(HttpStatus.OK).body(wishResponses);
    }

}
