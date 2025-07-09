package gift.service;

import gift.config.JwtProvider;
import gift.dto.MemberRequestDto;
import gift.dto.TokenResponseDto;
import gift.entity.Member;
import gift.entity.MemberRole;
import gift.exception.EmailAlreadyExistsException;
import gift.repository.MemberRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public MemberService(MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    @Transactional
    public TokenResponseDto registerMember(MemberRequestDto memberRequestDto) {
        if (memberRepository.findMemberByEmail(memberRequestDto.email()).isPresent()) {
            throw new EmailAlreadyExistsException(memberRequestDto.email());
        }

        Member member = new Member(
                memberRequestDto.email(),
                BCrypt.hashpw(memberRequestDto.password(), BCrypt.gensalt()),
                MemberRole.ROLE_USER
        );

        Long savedId = memberRepository.saveMember(member);

        return new TokenResponseDto(jwtProvider.generateToken(savedId, member.getRole()));
    }


}
