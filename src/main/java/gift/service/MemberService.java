package gift.service;

import gift.entity.Member;
import gift.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member createMember(String email, String password) {
        if (memberRepository.checkEmailExists(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }
        Member member = new Member(email, password);
        Optional<Long> optionalIdentifyNumber = memberRepository.createMember(member);
        if (optionalIdentifyNumber.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Member creation failed");
        }
        return member.updateIdentifyNumber(optionalIdentifyNumber.get());
    }

}
