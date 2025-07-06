package gift.service;

import gift.component.BCryptEncryptor;
import gift.dto.LoginRequestDto;
import gift.dto.RegisterMemberRequestDto;
import gift.dto.TokenResponseDto;
import gift.entity.Member;
import gift.repository.MemberRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptEncryptor bCryptEncryptor;

    @Value("${jwt.secret}")
    private String secretKey;

    public MemberService(MemberRepository memberRepository, BCryptEncryptor bCryptEncryptor) {
        this.memberRepository = memberRepository;
        this.bCryptEncryptor = bCryptEncryptor;
    }

    @Transactional
    public void registerMember(RegisterMemberRequestDto requestDto) {
        if (memberRepository.existsByEmail(requestDto.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 존재하는 이메일 입니다.");
        }
        memberRepository.registerMember(requestDto.email(), bCryptEncryptor.encode(requestDto.password()));
    }

    @Transactional
    public TokenResponseDto login(LoginRequestDto requestDto) {
        if (!memberRepository.existsByEmail(requestDto.email())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 불일치 합니다.");
        }

        Member findMember = memberRepository.findMemberByEmailOrElseThrow(requestDto.email());
        if (!bCryptEncryptor.isMatch(requestDto.password(), findMember.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 불일치 합니다.");
        }

        return new TokenResponseDto(getToken(findMember));
    }

    private String getToken(Member member) {
        return Jwts.builder()
                .subject(member.getId().toString())
                .claim("email", member.getEmail())
                .claim("role", member.getRole())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }
}
