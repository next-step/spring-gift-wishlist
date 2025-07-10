package gift.controller;

import gift.dto.request.GiftCreateRequest;
import gift.dto.request.GiftModifyRequest;
import gift.dto.response.GiftResponse;
import gift.exception.gift.InValidSpecialCharException;
import gift.exception.gift.NeedAcceptException;
import gift.exception.gift.NoGiftException;
import gift.exception.gift.NoValueException;
import gift.service.GiftService;
import gift.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static gift.status.GiftErrorStatus.*;

@RestController
@RequestMapping("/api/gifts")
public class GiftController {
    private final GiftService giftService;
    private final TokenService tokenService;

    public GiftController(GiftService giftService, TokenService tokenService) {
        this.giftService = giftService;
        this.tokenService = tokenService;
    }

    @PostMapping("")
    public ResponseEntity<GiftResponse> addGift(
            @Valid @RequestBody GiftCreateRequest giftCreateRequest,
            @RequestHeader HttpHeaders headers
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(giftService.addGift(giftCreateRequest, headers.get("Authorization")));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GiftResponse> getGiftById(@PathVariable Long id) {
        return ResponseEntity.ok().body(giftService.getGiftById(id));
    }

    @GetMapping("")
    public ResponseEntity<List<GiftResponse>> getAllGifts(){
        return ResponseEntity.ok().body(giftService.getAllGifts());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GiftResponse> updateGift(
            @PathVariable Long id,
            @Valid @RequestBody GiftModifyRequest giftModifyRequest,
            @RequestHeader HttpHeaders headers
    ) {
        tokenService.isTokenExpired(headers.get("Authorization"));
        return ResponseEntity.ok().body(giftService.updateGift(id, giftModifyRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGift(
            @PathVariable Long id,
            @RequestHeader HttpHeaders headers
    ) {
        tokenService.isTokenExpired(headers.get("Authorization"));
        giftService.deleteGift(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(value = NoGiftException.class)
    public ResponseEntity<?> handleException(NoGiftException e) {
        return ResponseEntity.status(NO_GIFT.getStatus()).body(e.getMessage());
    }

    @ExceptionHandler(value = NoValueException.class)
    public ResponseEntity<?> handleException(NoValueException e) {
        return ResponseEntity.status(NO_VALUE.getStatus()).body(e.getMessage());
    }

    @ExceptionHandler(value = InValidSpecialCharException.class)
    public ResponseEntity<?> handleException(InValidSpecialCharException e) {
        return ResponseEntity.status(WRONG_CHARACTER.getStatus()).body(e.getMessage());
    }

    @ExceptionHandler(value = NeedAcceptException.class)
    public ResponseEntity<?> handleException(NeedAcceptException e) {
        return ResponseEntity.status(NOT_ACCEPTED.getStatus()).body(e.getMessage());
    }
}
