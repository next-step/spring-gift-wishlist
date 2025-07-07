package gift.service;

import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;
import gift.entity.Member;
import gift.repository.MemberRepository;
import gift.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
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

        Member member = memberRepository.findByEmail(requestDto.getEmail())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        String accessToken = jwtUtil.createToken(member);

        return new MemberResponseDto(accessToken);
    }

    @Override
    public MemberResponseDto login(MemberRequestDto requestDto, String bearerAuthorizationHeader) {
        return null;
    }
}
