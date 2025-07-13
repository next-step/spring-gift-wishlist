package gift.controller;

import gift.dto.CreateWishRequest;
import gift.dto.CreateWishResponse;
import gift.entity.Member;
import gift.entity.Wish;
import gift.service.WishService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/wishes")
public class WishRestController {
    private WishService wishService;

    WishRestController(WishService wishService) {
        this.wishService = wishService;
    }

    @PostMapping
    public ResponseEntity<CreateWishResponse> addWishes(
        @RequestBody CreateWishRequest request,
        @LoginMember Member member
    ) {
        return new ResponseEntity<>(wishService.create(member.getId(), request), HttpStatus.CREATED);
    }
}
