package gift.controller;

import gift.dto.request.CreateGiftRequest;
import gift.dto.request.ModifyGiftRequest;
import gift.dto.response.ResponseGift;
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
@RequestMapping("/api/products")
public class GiftController {
    private final GiftService giftService;

    public GiftController(GiftService giftService) {
        this.giftService = giftService;
    }

    @PostMapping("")
    public ResponseEntity<ResponseGift> addGift(
            @Valid @RequestBody CreateGiftRequest createGiftRequest,
            @RequestHeader HttpHeaders headers
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(giftService.addGift(createGiftRequest, headers.get("Authorization")));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseGift> getGiftById(@PathVariable Long id) {
        return ResponseEntity.ok().body(giftService.getGiftById(id));
    }

    @GetMapping("")
    public ResponseEntity<List<ResponseGift>> getAllGifts(){
        return ResponseEntity.ok().body(giftService.getAllGifts());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseGift> updateGift(
            @PathVariable Long id,
            @Valid @RequestBody ModifyGiftRequest modifyGiftRequest,
            @RequestHeader HttpHeaders headers
    ) {
        return ResponseEntity
                .ok()
                .body(giftService.updateGift(id, modifyGiftRequest, headers.get("Authorization")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGift(
            @PathVariable Long id,
            @RequestHeader HttpHeaders headers
    ) {
        System.out.println("headers.get(\"Authorization\") = " + headers.get("Authorization"));
        giftService.deleteGift(id,  headers.get("Authorization"));
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
