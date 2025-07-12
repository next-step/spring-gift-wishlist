package gift.service;

import gift.dto.request.GiftCreateRequest;
import gift.dto.request.GiftModifyRequest;
import gift.dto.response.GiftResponse;
import gift.entity.Gift;
import gift.exception.gift.InValidSpecialCharException;
import gift.exception.gift.NoGiftException;
import gift.repository.GiftRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static gift.status.GiftErrorStatus.*;

@Service
public class GiftService {
    private final GiftRepository giftRepository;

    public GiftService(GiftRepository giftRepository) {
        this.giftRepository = giftRepository;
    }

    public GiftResponse addGift(GiftCreateRequest giftCreateRequest) {
        Gift gift = giftCreateRequest.toEntity();
        if(!gift.isGiftNameValid()){
            throw new InValidSpecialCharException(WRONG_CHARACTER.getErrorMessage());
        }
        gift.isKakaoMessageInclude();
        return GiftResponse.from(giftRepository.save(gift));
    }

    public GiftResponse getGiftById(Long id) {
        return GiftResponse.from(
                giftRepository.findById(id).orElseThrow(() -> new NoGiftException(NO_GIFT.getErrorMessage()))
        );
    }

    public List<GiftResponse> getAllGifts() {
        return giftRepository
                .findAll()
                .stream()
                .map(GiftResponse::from)
                .toList();
    }

    public GiftResponse updateGift(Long id, GiftModifyRequest giftModifyRequest) {
        giftRepository.findById(id).orElseThrow(() -> new NoGiftException(NO_GIFT.getErrorMessage()));
        return GiftResponse.from(giftRepository.modify(id, giftModifyRequest.toEntity()));
    }

    public void deleteGift(Long id) {
        giftRepository.deleteById(id);
    }
}
