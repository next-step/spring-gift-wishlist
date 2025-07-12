package gift.controller;


import gift.dto.request.WishAddRequestDto;
import gift.dto.request.WishDeleteRequestDto;
import gift.dto.request.WishUpdateRequestDto;
import gift.dto.response.WishIdResponseDto;
import gift.entity.User;
import gift.entity.WishProduct;
import gift.service.WishService;
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

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @PostMapping("")
    public ResponseEntity<WishIdResponseDto> addToWish(
        @RequestBody @Valid WishAddRequestDto wishAddRequestDto,
        @LoginMember User currentUser
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(wishService.addProduct(wishAddRequestDto, currentUser.email()));
    }

    @GetMapping("")
    public ResponseEntity<List<WishProduct>> getWishItem(
        @LoginMember User currentUser
    ) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(wishService.getWishList(currentUser.email()));
    }

    @DeleteMapping("/{wishId}")
    public ResponseEntity<Void> deleteWish(
        @RequestBody @Valid WishDeleteRequestDto productName,
        @LoginMember User currentUser,
        @PathVariable Long wishId) {

        wishService.deleteProduct(currentUser.email(), wishId, productName);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/{wishId}")
    public ResponseEntity<Void> updateWish(
        @RequestBody @Valid WishUpdateRequestDto wishUpdateRequestDto,
        @LoginMember User currentUser,
        @PathVariable Long wishId
    ) {
        wishService.updateProduct(wishId, currentUser.email(), wishUpdateRequestDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
