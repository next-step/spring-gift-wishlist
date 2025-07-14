package gift.service;

import gift.domain.Member;
import gift.domain.Wish;
import gift.repository.WishRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishService {
    private final WishRepository repository;

    public WishService(WishRepository repository) {
        this.repository = repository;
    }

    public void addWish(Member member, Long productId) {
        System.out.println(member.getEmail());
        repository.save(member.getEmail(), productId);
    }

    public List<Wish> getWishes(Member member) {
        return repository.findByMemberId(member.getEmail());
    }

    public void deleteWish(Member member, Long productId) {
        repository.delete(member.getEmail(), productId);
    }


}
