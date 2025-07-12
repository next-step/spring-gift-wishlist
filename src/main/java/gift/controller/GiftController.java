package gift.controller;

import gift.annotation.AuthUser;
import gift.dto.request.GiftCreateRequest;
import gift.dto.request.GiftModifyRequest;
import gift.dto.response.GiftResponse;
import gift.entity.User;
import gift.exception.gift.InValidSpecialCharException;
import gift.exception.gift.NeedAcceptException;
import gift.exception.gift.NoGiftException;
import gift.exception.gift.NoValueException;
import gift.service.GiftService;
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

    public GiftController(GiftService giftService) {
        this.giftService = giftService;
    }

    @PostMapping()
    public ResponseEntity<GiftResponse> addGift(
            @Valid @RequestBody GiftCreateRequest giftCreateRequest,
            @AuthUser User user
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(giftService.addGift(giftCreateRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GiftResponse> getGiftById(@PathVariable Long id) {
        return ResponseEntity.ok().body(giftService.getGiftById(id));
    }

    @GetMapping()
    public ResponseEntity<List<GiftResponse>> getAllGifts(){
        return ResponseEntity.ok().body(giftService.getAllGifts());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GiftResponse> updateGift(
            @PathVariable Long id,
            @Valid @RequestBody GiftModifyRequest giftModifyRequest,
            @AuthUser User user
    ) {
        return ResponseEntity.ok().body(giftService.updateGift(id, giftModifyRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGift(
            @PathVariable Long id,
            @RequestHeader HttpHeaders headers
    ) {
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
