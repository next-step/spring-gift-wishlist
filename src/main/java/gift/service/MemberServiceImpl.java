package gift.service;

import gift.dto.MemberRequestDto;
import gift.dto.TokenResponseDto;
import gift.entity.Member;
import gift.repository.MemberRepository;
import gift.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public MemberServiceImpl(MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public String saveMember(MemberRequestDto memberRequestDto) {
        memberRepository.saveMember(memberRequestDto.getEmail(), memberRequestDto.getPassword(), memberRequestDto.getRole());
        return jwtUtil.generateToken(memberRequestDto);
    }

    @Override
    public String existMember(MemberRequestDto memberRequestDto) {
        int membercount = memberRepository.countMember(memberRequestDto.getEmail(), memberRequestDto.getPassword());
        if(membercount < 1) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다.");
        }
        return jwtUtil.generateToken(memberRequestDto);
    }

    @Override
    public Member findByEmail (String email) {
        return memberRepository.findByEmail(email);
    }
}
