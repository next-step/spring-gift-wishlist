package gift.service;

import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;
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
    public MemberResponseDto create(MemberRequestDto requestDto) {
        int createRow = memberRepository.create(requestDto);

        if (createRow <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Member member = memberRepository.findByEmail(requestDto.email())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        String accessToken = jwtUtil.createToken(member);

        return new MemberResponseDto(accessToken);
    }

    @Override
    public MemberResponseDto login(MemberRequestDto requestDto) {
        Member member = memberRepository.findByEmail(requestDto.email())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));

        if (!requestDto.password().equals(member.getPassword())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        String accessToken = jwtUtil.createToken(member);

        return new MemberResponseDto(accessToken);
    }

    @Override
    public void changePassword(MemberRequestDto requestDto) {
        Member member = memberRepository.findByEmail(requestDto.email())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));

        memberRepository.changePassword(new Member(requestDto.email(), requestDto.password()));
    }

    @Override
    public void resetPassword(MemberRequestDto requestDto) {
        Member member = memberRepository.findByEmail(requestDto.email())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));

        // TODO: 주어진 이메일에 대해 전송 후, 사용자에게 인증받는 절차는 거쳤다고 가정

        memberRepository.changePassword(new Member(requestDto.email(), requestDto.password()));
    }
}
