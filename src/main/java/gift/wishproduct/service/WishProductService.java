package gift.wishproduct.service;

import gift.wishproduct.dto.WishProductCreateReq;
import gift.wishproduct.dto.WishProductResponse;

import java.util.List;
import java.util.UUID;

public interface WishProductService {

    UUID save(WishProductCreateReq wishProductCreateReq, String email);

    List<WishProductResponse> findMyWishProduct(String email);
}
