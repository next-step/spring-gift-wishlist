package gift.member.service;

import gift.exception.EntityAlreadyExistsException;
import gift.exception.InvalidCredentialsException;
import gift.member.dto.MemberInfo;
import gift.member.dto.MemberLoginRequestDto;
import gift.member.dto.MemberLoginResponseDto;
import gift.member.dto.MemberRegisterRequestDto;
import gift.member.dto.MemberTokenInfo;
import gift.member.entity.Member;
import gift.member.repository.MemberRepository;
import gift.token.service.TokenProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    public MemberService(MemberRepository memberRepository, TokenProvider tokenProvider) {
        this.memberRepository = memberRepository;
        this.tokenProvider = tokenProvider;
    }

    @Transactional
    public MemberInfo register(MemberRegisterRequestDto requestDto) {
        if(memberRepository.existsByEmail(requestDto.email())) {
            throw new EntityAlreadyExistsException("이미 가입된 계정입니다.");
        }

        Member member = new Member(requestDto);
        memberRepository.save(member);
        return new MemberInfo(member);
    }

    @Transactional
    public MemberLoginResponseDto login(MemberLoginRequestDto requestDto) {
        Member member = memberRepository.findByEmail(requestDto.email())
                .orElseThrow(InvalidCredentialsException::new);

        if (!member.verifyPassword(requestDto.password())) {
            throw new InvalidCredentialsException();
        }

        MemberTokenInfo memberTokenInfo = new MemberTokenInfo(
                tokenProvider.generateAccessToken(member),
                tokenProvider.generateRefreshToken(member));
        return new MemberLoginResponseDto(new MemberInfo(member), memberTokenInfo);
    }

}
