package gift.service;

import gift.config.NotMatchPasswordException;
import gift.domain.Member;
import gift.dto.CreateMemberRequest;
import gift.dto.CreateMemberResponse;
import gift.dto.LoginMemberRequest;
import gift.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository repository;

    public MemberService(MemberRepository repository) {
        this.repository = repository;
    }

    public CreateMemberResponse register(CreateMemberRequest request) {
        Member member = repository.save(request);
        return new CreateMemberResponse(member.getEmail());
    }

    public void login(LoginMemberRequest request) {
        Optional<Member> findMember = repository.findByEmail(request.email());
        if (findMember.isEmpty()) {
            throw new NoSuchElementException("Email이 존재하지 않습니다.");
        }
        Member member = findMember.get();
        if (!member.getPassword().equals(request.password())) {
            throw new NotMatchPasswordException("비밀번호가 일치하지 않습니다.");
        }
    }
}
