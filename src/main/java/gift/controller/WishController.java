package gift.controller;

import gift.dto.ProductResponse;
import gift.dto.WishRequest;
import gift.service.WishService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
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

    @PostMapping
    public ResponseEntity<Void> addWish(
            @RequestAttribute("memberId") Long memberId,
            @Valid @RequestBody WishRequest request
    ) {
        wishService.addWish(memberId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getWishes(
            @RequestAttribute("memberId") Long memberId
    ) {
        List<ProductResponse> products = wishService.getWishes(memberId);
        return ResponseEntity.ok(products);
    }
}
