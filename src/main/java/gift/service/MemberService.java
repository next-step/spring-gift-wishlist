package gift.service;

import gift.common.dto.request.CreateMemberDto;
import gift.common.dto.response.TokenResponseDto;
import gift.common.exception.CreationFailException;
import gift.common.exception.RegisterFailException;
import gift.domain.member.Member;
import gift.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public TokenResponseDto handleRegisterRequest(CreateMemberDto request) {
        register(request.email(), request.password());
        // 로그인 처리 (토큰 발급) 후 TokenResponseDto 리턴
        return null;
    }

    private Member register(String email, String plainPassword) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new RegisterFailException("이미 가입된 email 입니다.", HttpStatus.FORBIDDEN);
        }
        String encryptedPassword = encoder.encode(plainPassword);
        Member instance = Member.createTemp(email, encryptedPassword);
        return memberRepository.save(instance)
                .orElseThrow(() -> new CreationFailException("Failed creating Member: " + email));
    }
}
