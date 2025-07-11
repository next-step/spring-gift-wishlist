package gift.service;

import gift.config.JwtProvider;
import gift.dto.MemberRequestDto;
import gift.dto.TokenResponseDto;
import gift.entity.Member;
import gift.entity.MemberRole;
import gift.exception.EmailAlreadyExistsException;
import gift.exception.LoginFailedException;
import gift.exception.MemberNotFoundException;
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

    @Transactional(readOnly = true)
    public TokenResponseDto loginMember(MemberRequestDto memberRequestDto) {
        Member member = memberRepository.findMemberByEmail(memberRequestDto.email())
                                        .orElseThrow(LoginFailedException::new);

        if (!BCrypt.checkpw(memberRequestDto.password(), member.getPassword())) {
            throw new LoginFailedException();
        }

        return new TokenResponseDto(jwtProvider.generateToken(member.getId(), member.getRole()));
    }

    @Transactional(readOnly = true)
    public Member getMemberById(Long id) {

        return memberRepository.findMemberById(id)
                               .orElseThrow(() -> new MemberNotFoundException(id));
    }
}
