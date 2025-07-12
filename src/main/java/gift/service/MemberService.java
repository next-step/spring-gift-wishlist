package gift.service;

import gift.domain.Member;
import gift.dto.AuthorizationRequest;
import gift.dto.AuthorizationResponse;
import gift.dto.MemberRegisterResponse;
import gift.dto.MemberResponse;
import gift.exception.BusinessException;
import gift.exception.ErrorCode;
import gift.repository.MemberRepository;
import gift.util.PasswordEncoder;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    public MemberService(MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    public MemberRegisterResponse register(AuthorizationRequest request) {
        String hashedPassword = PasswordEncoder.hashPassword(request.password());
        Member member = Member.of(
                request.email(),
                hashedPassword
        );
        try {
            Member savedMember = memberRepository.save(member);
            String token = jwtProvider.generateToken(savedMember.email());
            return MemberRegisterResponse.of(token, savedMember);
        }catch (DuplicateKeyException ex){
            throw new BusinessException(ErrorCode.USER_EMAIL_ALREADY_EXIST);
        }
    }

    public AuthorizationResponse login(AuthorizationRequest request) {
        String email = request.email();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new BusinessException(ErrorCode.USER_EMAIL_NOT_FOUND));
        if(!PasswordEncoder.checkPassword(request.password(), member.password())){
            throw new BusinessException(ErrorCode.USER_PASSWORD_MISMATCH);
        }

        String token = jwtProvider.generateToken(member.email());
        return AuthorizationResponse.of(token);
    }

    public MemberResponse getById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(()-> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return MemberResponse.from(member);
    }
}
