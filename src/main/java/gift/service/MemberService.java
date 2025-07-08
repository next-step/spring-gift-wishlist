package gift.service;

import gift.domain.Member;
import org.springframework.stereotype.Service;
import gift.repository.MemberRepository;

@Service
public class MemberService {
    private final MemberRepository repository;

    public MemberService(MemberRepository repository) {
        this.repository = repository;
    }

    public void register (Member member) {
        repository.save(member);
    }

    public boolean login(String email, String password) {
        return repository.findByEmail(email)
                .map(m -> m.getPassword().equals(password))
                .orElse(false);
    }

}
