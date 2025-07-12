package gift.domain.wish.service;

import gift.domain.auth.service.AuthService;
import gift.domain.member.Member;
import gift.domain.wish.Wish;
import gift.domain.wish.dto.WishRequest;
import gift.domain.wish.dto.WishResponse;
import gift.domain.wish.dto.WishUpdateRequest;
import gift.domain.wish.repository.WishRepository;
import gift.global.exception.TokenExpiredException;
import gift.global.exception.WishNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WishService {
    private final WishRepository wishRepository;
    private final AuthService authService;

    public WishService(WishRepository wishRepository, AuthService authService) {
        this.wishRepository = wishRepository;
        this.authService = authService;
    }

    public void createWish(WishRequest wishRequest, String accessToken) {
        Member member = authService.getMemberByToken(accessToken).orElseThrow(() -> new TokenExpiredException(accessToken));
        Wish wish = new Wish(member.getId(), wishRequest.productId(), wishRequest.quantity());
        int affectedRows = wishRepository.save(wish);
        if (affectedRows == 0) {
            throw new RuntimeException("WishService : createWish() failed - 500 Internal Server Error");
        }
    }

    public void updateWish(Long id, WishUpdateRequest req, String accessToken) {
        Member member = authService.getMemberByToken(accessToken).orElseThrow(()->new TokenExpiredException(accessToken));
        Wish wish = wishRepository.get(id, member.getId()).orElseThrow(() -> new WishNotFoundException(id + "가 존재하지 않습니다."));
        wish.update(req.quantity());
        int affectedRows = wishRepository.update(wish);
        if (affectedRows == 0) {
            throw new RuntimeException("WishService : updateWish() failed - 500 Internal Server Error");
        }
    }

    public void deleteWish(Long id, String accessToken) {
        Member member = authService.getMemberByToken(accessToken).orElseThrow(()->new TokenExpiredException(accessToken));
        Wish wish = wishRepository.get(id, member.getId()).orElseThrow(() -> new WishNotFoundException(id + "가 존재하지 않습니다."));
        int affectedRows = wishRepository.delete(wish.getId());
        if (affectedRows == 0) {
            throw new RuntimeException("WishService : deleteWish() failed - 500 Internal Server Error");
        }
    }

    public List<WishResponse> getWishes(String accessToken) {
        Member member = authService.getMemberByToken(accessToken).orElseThrow(()->new TokenExpiredException(accessToken));
        return wishRepository.getAll(member.getId()).stream()
                .map(WishResponse::from)
                .toList();
    }
}
