package gift.service;

import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;
import gift.dto.ProductResponseDto;

import java.util.List;

public interface MemberServiceInterface {

    boolean isEmailExists(String email);

    MemberResponseDto register(MemberRequestDto requestDto);

    MemberResponseDto login(MemberRequestDto requestDto);

    List<ProductResponseDto> findAllProductsFromWishList(String token);
}
