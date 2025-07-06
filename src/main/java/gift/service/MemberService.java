package gift.service;

import com.sun.jdi.request.DuplicateRequestException;
import gift.dto.api.MemberRegisterRequestDto;
import gift.entity.Member;
import gift.repository.MemberRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final TokenService tokenService;

    public MemberService(MemberRepository memberRepository,
        TokenService tokenService) {
        this.memberRepository = memberRepository;
        this.tokenService = tokenService;
    }

    // 회원 등록
    public String registerMember(MemberRegisterRequestDto requestDto) {

        // 이메일 중복 검사
        if (memberRepository.existByEmail(requestDto.getEmail())) {
            throw new DuplicateKeyException("이미 사용 중인 이메일입니다.");
        }

        // 회원 저장
        Member member = new Member(requestDto.getEmail(), requestDto.getPassword());
        Member saved = memberRepository.save(member);

        // JWT 토큰 생성
        return tokenService.generateToken(saved.getId(), saved.getEmail());
    }

}
