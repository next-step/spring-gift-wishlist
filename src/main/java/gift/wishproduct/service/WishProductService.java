package gift.wishproduct.service;

import gift.wishproduct.dto.WishProductCreateReq;

import java.util.UUID;

public interface WishProductService {

    UUID save(WishProductCreateReq wishProductCreateReq, String email);
}
