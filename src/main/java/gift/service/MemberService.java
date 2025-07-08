package gift.service;

import gift.dto.MemberRequestDto;
import gift.entity.Member;
import gift.repository.MemberRepository;
import gift.security.JwtProvider;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public MemberService(MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }


    public String register(MemberRequestDto requestDto) {
        if (memberRepository.existsByEmail(requestDto.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "이미 등록된 이메일입니다.");
        }

        Member member = new Member(requestDto.getEmail(), requestDto.getPassword());
        memberRepository.save(member);

        Member saved = memberRepository.findByEmail(member.getEmail())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR, "회원 저장 후 조회 실패"));

        return jwtProvider.createToken(saved);
    }

    public String login(MemberRequestDto requestDto) {
        Member member = memberRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.FORBIDDEN, "이메일이 존재하지 않습니다."));

        if (!member.getPassword().equals(requestDto.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "비밀번호가 일치하지 않습니다.");
        }

        return jwtProvider.createToken(member);
    }
}