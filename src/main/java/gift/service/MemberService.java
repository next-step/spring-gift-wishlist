package gift.service;

import gift.dto.AuthResponseDto;
import gift.dto.MemberRequestDto;
import gift.entity.Member;
import gift.repository.MemberRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.coyote.BadRequestException;
import org.apache.tomcat.websocket.AuthenticationException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final String secretKey;

    MemberService(MemberRepository memberRepository, @Value("${jwt.secretKey}") String secretKey) {
        this.memberRepository = memberRepository;
        this.secretKey = secretKey;
    }

    public AuthResponseDto creatMember(MemberRequestDto memberRequestDto) throws BadRequestException {
        String passwordHash = BCrypt.hashpw(
                memberRequestDto.password(),
                BCrypt.gensalt());

        if (memberRepository.findMemberByEmail(memberRequestDto.email()).isPresent()){
            throw  new BadRequestException("email이 중복 됩니다.");
        }

        long id = memberRepository.saveMember(
                        memberRequestDto.email(),
                        passwordHash)
                .orElseThrow(() -> new BadRequestException("Member를 생성할 수 없습니다."))
                .longValue();

        return new AuthResponseDto(
                makeJwtToken(new Member(
                        id,
                        memberRequestDto.email(),
                        passwordHash)));
    }

    @Transactional(readOnly = true)
    public AuthResponseDto login(MemberRequestDto memberRequestDto) throws AuthenticationException {
        Member member = memberRepository.findMemberByEmail(memberRequestDto.email())
                .orElseThrow(() -> new AuthenticationException("존재하지 않는 멤버입니다."));
        if (!BCrypt.checkpw(
                memberRequestDto.password(),
                member.password())) {
            throw new AuthenticationException("비밀번호가 일치하지 않습니다.");
        }

        return new AuthResponseDto(
                makeJwtToken(member));
    }

    private String makeJwtToken(Member member) {
        return Jwts.builder()
                .subject(Long.toString(member.id()))
                .claim("email", member.email())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }
}
