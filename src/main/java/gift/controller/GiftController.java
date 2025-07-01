package gift.controller;

import gift.dto.request.RequestGift;
import gift.dto.request.RequestModifyGift;
import gift.dto.response.ResponseGift;
import gift.exception.NoGiftException;
import gift.repository.GiftJdbcRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class GiftController {
    private final GiftJdbcRepository giftRepository;

    public GiftController(GiftJdbcRepository giftRepository) {
        this.giftRepository = giftRepository;
    }

    @PostMapping("")
    public ResponseEntity<?> addGift(@Valid @RequestBody RequestGift requestGift) {
        return new ResponseEntity<>(
                ResponseGift.from(
                        giftRepository.save(
                            RequestGift.toEntity(requestGift)
                        )
                ), HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGiftById(@PathVariable Long id) {
        return ResponseEntity.ok().body(giftRepository.findById(id).orElseThrow(
                () -> new NoGiftException("해당 상품을 찾을 수 없습니다.")
        ));
    }

    @GetMapping("")
    public ResponseEntity<?> getAllGifts(){
        return ResponseEntity.ok().body(giftRepository.findAll());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateGift(@PathVariable Long id, @Valid @RequestBody RequestModifyGift requestModifyGift) {
        giftRepository.findById(id).orElseThrow(() -> new NoGiftException("해당 상품을 찾을 수 없습니다."));
        return ResponseEntity
                .ok()
                .body(
                        ResponseGift.from(
                                giftRepository.modify(id, RequestModifyGift.toEntity(requestModifyGift))
                        )
                );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGift(@PathVariable Long id) {
        giftRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
