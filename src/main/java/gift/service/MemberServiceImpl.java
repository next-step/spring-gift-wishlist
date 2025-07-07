package gift.service;

import gift.dto.MemberPasswordChangeDto;
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
        if (memberRepository.existsByEmail(requestDto.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        int createRow = memberRepository.create(requestDto);

        if (createRow <= 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        String accessToken = jwtUtil.createToken(requestDto.email());

        return new MemberResponseDto(accessToken);
    }

    @Override
    public MemberResponseDto login(MemberRequestDto requestDto) {
        Member member = memberRepository.findByEmail(requestDto.email())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));

        if (!requestDto.password().equals(member.getPassword())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        String accessToken = jwtUtil.createToken(requestDto.email());

        return new MemberResponseDto(accessToken);
    }

    @Override
    public void changePassword(MemberPasswordChangeDto requestDto) {
        Member member = memberRepository.findByEmail(requestDto.email())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));

        int changeRow = memberRepository.changePassword(
            new Member(requestDto.email(), requestDto.beforePassword()),
            requestDto.afterPassword());

        if (changeRow <= 0) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void resetPassword(MemberRequestDto requestDto) {
        Member member = memberRepository.findByEmail(requestDto.email())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));

        // TODO: 주어진 이메일에 대해 전송 후, 사용자에게 인증받는 절차는 거쳤다고 가정

        memberRepository.resetPassword(new Member(requestDto.email(), requestDto.password()));
    }
}
