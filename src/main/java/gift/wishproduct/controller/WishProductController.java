package gift.wishproduct.controller;


import gift.domain.WishProduct;
import gift.member.annotation.MyAuthenticalPrincipal;
import gift.member.dto.AuthMember;
import gift.util.LocationGenerator;
import gift.wishproduct.dto.WishProductCreateReq;
import gift.wishproduct.service.WishProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/wish-products")
public class WishProductController {

    private final WishProductService wishProductService;

    public WishProductController(WishProductService wishProductService) {
        this.wishProductService = wishProductService;
    }

    @PostMapping()
    public ResponseEntity<Void> addWishProduct(@MyAuthenticalPrincipal AuthMember authMember,
            @RequestBody WishProductCreateReq wishProductCreateReq) {

        UUID savedId = wishProductService.save(wishProductCreateReq, authMember.getEmail());


        return ResponseEntity.status(HttpStatus.CREATED).
                location(LocationGenerator.generate(savedId))
                .build();

    }
}
