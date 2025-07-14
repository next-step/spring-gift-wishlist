package gift.controller;


import gift.dto.request.WishAddRequestDto;
import gift.dto.request.WishDeleteRequestDto;
import gift.dto.request.WishUpdateRequestDto;
import gift.dto.response.WishIdResponseDto;
import gift.entity.WishProduct;
import gift.service.WishServiceImpl;
import gift.wishPreProcess.LoginMember;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishes")
public class WishController {

    private final WishServiceImpl wishServiceImpl;

    public WishController(WishServiceImpl wishServiceImpl) {
        this.wishServiceImpl = wishServiceImpl;
    }

    @PostMapping("")
    public ResponseEntity<WishIdResponseDto> addToWish(
        @RequestBody @Valid WishAddRequestDto wishAddRequestDto,
        @LoginMember String userEmail
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(wishServiceImpl.addProduct(wishAddRequestDto, userEmail));
    }

    @GetMapping("")
    public ResponseEntity<List<WishProduct>> getWishItem(
        @LoginMember String userEmail
    ) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(wishServiceImpl.getWishList(userEmail));
    }

    @DeleteMapping("/{wishId}")
    public ResponseEntity<Void> deleteWish(
        @RequestBody @Valid WishDeleteRequestDto productName,
        @LoginMember String userEmail,
        @PathVariable Long wishId) {

        wishServiceImpl.deleteProduct(userEmail, wishId, productName);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/{wishId}")
    public ResponseEntity<Void> updateWish(
        @RequestBody @Valid WishUpdateRequestDto wishUpdateRequestDto,
        @LoginMember String userEmail,
        @PathVariable Long wishId
    ) {
        wishServiceImpl.updateProduct(wishId, userEmail, wishUpdateRequestDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
