package gift.service;

import gift.auth.JwtTokenProvider;
import gift.entity.Member;
import gift.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;


    public MemberService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    public String register(String email, String password) {

        memberRepository.findByEmail(email).ifPresent(member -> {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        });


        Member member = new Member(null, email, password);
        memberRepository.save(member);


        return jwtTokenProvider.createToken(email);
    }


    public String login(String email, String password) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));


        if (!member.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }


        return jwtTokenProvider.createToken(email);
    }
}