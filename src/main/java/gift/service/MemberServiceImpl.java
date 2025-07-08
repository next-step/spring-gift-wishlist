package gift.service;

import gift.domain.Member;
import gift.exception.DuplicateMemberException;
import gift.repository.MemberRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    public MemberServiceImpl(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    @Override
    public void register(String email, String pwd){
        memberRepository.findByEmail(email)
                .ifPresent(member -> {
                    throw new DuplicateMemberException();
                });
        Member newMember = new Member(null,email,pwd);
        memberRepository.register(newMember);
    }

    @Override
    public String login(String email, String pwd){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.FORBIDDEN, "회원이 존재하지 않습니다."));

        if (!member.pwd().equals(pwd)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "비밀번호가 일치하지 않습니다.");
        }

        return Jwts.builder()
                .setSubject(member.id().toString())
                .claim("email", member.email())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }
}
