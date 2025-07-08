package gift.service;

import gift.dto.AuthResponseDto;
import gift.dto.MemberRequestDto;
import gift.entity.Member;
import gift.repository.MemberRepository;
import gift.util.JwtTokenProvider;
import org.apache.tomcat.websocket.AuthenticationException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AuthResponseDto creatMember(MemberRequestDto memberRequestDto) {
        String passwordHash = BCrypt.hashpw(
                memberRequestDto.password(),
                BCrypt.gensalt());

        if (memberRepository.findMemberByEmail(memberRequestDto.email()).isPresent()){
            throw new IllegalArgumentException("email이 중복 됩니다.");
        }

        long id = memberRepository.saveMember(
                        memberRequestDto.email(),
                        passwordHash)
                .orElseThrow(() -> new IllegalArgumentException("Member를 생성할 수 없습니다."))
                .longValue();

        return new AuthResponseDto(
                jwtTokenProvider.makeJwtToken(new Member(
                        id,
                        memberRequestDto.email(),
                        passwordHash)));
    }

    @Transactional(readOnly = true)
    public AuthResponseDto login(MemberRequestDto memberRequestDto) throws AuthenticationException {
        Member member = memberRepository.findMemberByEmail(memberRequestDto.email())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버입니다."));
        if (!BCrypt.checkpw(
                memberRequestDto.password(),
                member.password())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return new AuthResponseDto(
                jwtTokenProvider.makeJwtToken(member));
    }
}
