package gift.service;

import gift.dto.request.WishAddRequestDto;
import gift.dto.response.WishResponseDto;

public interface WishServiceInterface {

    WishResponseDto addProduct(WishAddRequestDto wishAddRequestDto, String email);
}
