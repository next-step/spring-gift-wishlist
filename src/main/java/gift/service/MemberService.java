package gift.service;

import gift.entity.Member;
import gift.exception.InvalidCredentialsException;
import gift.repository.MemberRepository;
import gift.token.JwtTokenProvider;
import gift.util.BCryptEncryptor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Member createMember(String email, String rawPassword) {
        String encodedPassword = BCryptEncryptor.encrypt(rawPassword);
        Member member = new Member(email, encodedPassword);
        Optional<Long> optionalIdentifyNumber = memberRepository.createMember(member);
        if (optionalIdentifyNumber.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Member creation failed");
        }
        return member.updateIdentifyNumber(optionalIdentifyNumber.get());
    }

    public String login(String email, String rawPassword) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (optionalMember.isEmpty() || !BCryptEncryptor.matches(rawPassword, optionalMember.get().getPassword())) {
            throw new InvalidCredentialsException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }
        return jwtTokenProvider.createToken(optionalMember.get());
    }

    public List<Member> getMemberList() {
        return memberRepository.getMemberList();
    }

    
}
