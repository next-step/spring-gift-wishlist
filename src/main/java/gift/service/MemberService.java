package gift.service;

import gift.auth.JwtAuth;
import gift.dto.MemberRequestDto;
import gift.entity.Member;
import gift.exception.MemberExceptions;
import gift.repository.MemberRepositoryInterface;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class MemberService implements MemberServiceInterface {
    private final MemberRepositoryInterface memberRepository;
    private final JwtAuth jwtAuth;

    public MemberService(@Qualifier("MemberRepository") MemberRepositoryInterface memberRepository, JwtAuth jwtAuth) {
        this.memberRepository = memberRepository;
        this.jwtAuth = jwtAuth;
    }

    @Override
    public String register(MemberRequestDto requestDto) {
        if (memberRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new MemberExceptions.EmailAlreadyExistsException(requestDto.getEmail());
        }

        Member member = new Member(requestDto.getEmail(), requestDto.getPassword());
        memberRepository.save(member);
        return jwtAuth.createJwtToken(member);
    }
}
