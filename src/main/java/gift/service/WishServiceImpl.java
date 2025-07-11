package gift.service;

import gift.domain.Member;
import gift.domain.Wish;
import gift.dto.request.WishRequest;
import gift.dto.response.WishResponse;
import gift.repository.WishRepository;
import org.springframework.stereotype.Service;

@Service
public class WishServiceImpl implements WishService {

    private final WishRepository wishRepository;

    public WishServiceImpl(WishRepository wishRepository){
        this.wishRepository = wishRepository;
    }

    @Override
    public WishResponse add(Member member, WishRequest request){
        wishRepository.add(new Wish(member.getId(), request.productId()));
        return new WishResponse("위시리스트에 추가되었습니다.");
    }
}
