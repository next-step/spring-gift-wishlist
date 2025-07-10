package gift.controller;

import gift.auth.JwtAuth;
import gift.dto.*;
import gift.exception.MemberExceptions;
import gift.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;
    private final JwtAuth jwtAuth;

    public MemberController(MemberService memberService, JwtAuth jwtAuth) {
        this.memberService = memberService;
        this.jwtAuth = jwtAuth;
    }

    @PostMapping("/membership")
    public ResponseEntity<MemberResponseDto> register(@Valid @RequestBody MemberRequestDto requestDto) {
        MemberResponseDto responseDto = memberService.register(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<MemberResponseDto> login(@Valid @RequestBody MemberRequestDto requestDto) {
        MemberResponseDto responseDto = memberService.login(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/wishlist")
    public ResponseEntity<List<ProductResponseDto>> getWishlist(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new MemberExceptions.InvalidAuthorizationHeaderException();
        }

        String token = authHeader.substring(7);
        if (!jwtAuth.validateToken(token)) {
            throw new MemberExceptions.InvalidTokenException();
        }

        List<ProductResponseDto> products = memberService.findAllProductsFromWishList(token);

        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @PostMapping("/wishlist")
    public ResponseEntity<List<ProductResponseDto>> addProductToWishlist(@RequestHeader("Authorization") String authHeader,
                                                                  @Valid @RequestBody WishListProductRequestDto productRequestDto) {
        String token = authHeader.substring(7);
        List<ProductResponseDto> products = memberService.addProductToWishListByEmail(token, productRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @DeleteMapping("/wishlist/{productId}")
    public ResponseEntity<Void> deleteProductFromWishlist(@RequestHeader("Authorization") String authHeader,
                                                          @PathVariable("productId") Long productId) {
        String token = authHeader.substring(7);
        memberService.deleteProductFromWishList(token, productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
