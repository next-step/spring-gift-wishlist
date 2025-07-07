package gift.service;

import gift.dto.request.CreateGiftRequest;
import gift.dto.request.ModifyGiftRequest;
import gift.dto.response.ResponseGift;
import gift.entity.Gift;
import gift.exception.gift.InValidSpecialCharException;
import gift.exception.gift.NoGiftException;
import gift.exception.token.TokenExpiredException;
import gift.exception.token.TokenTypeException;
import gift.repository.GiftRepository;
import gift.utils.JwtParser;
import org.springframework.stereotype.Service;

import java.util.List;

import static gift.status.GiftErrorStatus.*;
import static gift.status.TokenErrorStatus.INVALID_TOKEN_TYPE;
import static gift.status.TokenErrorStatus.TOKEN_EXPIRED;

@Service
public class GiftService {
    private final GiftRepository giftRepository;
    private final TokenService tokenService;

    public GiftService(GiftRepository giftRepository,  TokenService tokenService) {
        this.giftRepository = giftRepository;
        this.tokenService = tokenService;
    }

    public ResponseGift addGift(CreateGiftRequest createGiftRequest, List<String> token) {
        if(!JwtParser.isValidTokenType(token)){
            throw new TokenTypeException(INVALID_TOKEN_TYPE.getErrorMessage());
        }
        if(tokenService.isTokenExpired(JwtParser.getToken(token))) {
            throw new TokenExpiredException(TOKEN_EXPIRED.getErrorMessage());
        }
        Gift gift = CreateGiftRequest.toEntity(createGiftRequest);
        if(!gift.isGiftNameValid()){
            throw new InValidSpecialCharException(WRONG_CHARACTER.getErrorMessage());
        }

        gift.isKakaoMessageInclude();

        return ResponseGift.from(giftRepository.save(gift));
    }

    public ResponseGift getGiftById(Long id) {
        return ResponseGift.from(
                giftRepository.findById(id).orElseThrow(() -> new NoGiftException(NO_GIFT.getErrorMessage()))
        );
    }

    public List<ResponseGift> getAllGifts() {
        return giftRepository
                .findAll()
                .stream()
                .map(ResponseGift::from)
                .toList();
    }

    public ResponseGift updateGift(Long id, ModifyGiftRequest modifyGiftRequest, List<String> token) {
        if(!JwtParser.isValidTokenType(token)){
            throw new TokenTypeException(INVALID_TOKEN_TYPE.getErrorMessage());
        }
        if(tokenService.isTokenExpired(JwtParser.getToken(token))) {
            throw new TokenExpiredException(TOKEN_EXPIRED.getErrorMessage());
        }
        giftRepository.findById(id).orElseThrow(() -> new NoGiftException(NO_GIFT.getErrorMessage()));
        return ResponseGift.from(giftRepository.modify(id, ModifyGiftRequest.toEntity(modifyGiftRequest)));
    }

    public void deleteGift(Long id, List<String> token) {
        if(!JwtParser.isValidTokenType(token)){
            throw new TokenTypeException(INVALID_TOKEN_TYPE.getErrorMessage());
        }
        if(tokenService.isTokenExpired(JwtParser.getToken(token))) {
            throw new TokenExpiredException(TOKEN_EXPIRED.getErrorMessage());
        }
        giftRepository.deleteById(id);
    }
}
