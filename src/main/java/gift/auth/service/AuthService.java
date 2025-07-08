package gift.auth.service;

import gift.auth.domain.JwtProvider;
import gift.auth.domain.MemberAuth;
import gift.auth.dto.MemberRegisterRequestDto;
import gift.auth.dto.LoginRequestDto;
import gift.auth.dto.RefreshTokenRequestDto;
import gift.auth.dto.TokenResponseDto;
import gift.auth.repository.MemberAuthRepository;
import gift.member.domain.Member;
import gift.member.repository.MemberRepository;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {
  private final MemberAuthRepository memberAuthRepository;
  private final MemberRepository memberRepository;
  private final JwtProvider jwtProvider;
  private final PasswordEncoder passwordEncoder;

  public AuthService(MemberAuthRepository memberAuthRepository, MemberRepository memberRepository,
      JwtProvider jwtProvider, PasswordEncoder passwordEncoder) {
    this.memberAuthRepository = memberAuthRepository;
    this.memberRepository = memberRepository;
    this.jwtProvider = jwtProvider;
    this.passwordEncoder = passwordEncoder;
  }

  public TokenResponseDto registerMember(MemberRegisterRequestDto dto){
    String email = dto.email();
    if(memberAuthRepository.findByEmail(email).isPresent()){
      throw new IllegalArgumentException("중복된 이메일의 사용자가 존재합니다.");
    }

    Member member = Member.of(dto.username());
    Long memberId = memberRepository.save(member);
    String encodedPassword = passwordEncoder.encode(dto.password());

    MemberAuth memberAuth = MemberAuth.withId(memberId, dto.email(), encodedPassword);
    memberAuthRepository.save(memberAuth);

    return generateToken(memberId,email);
  }

  public TokenResponseDto login(LoginRequestDto dto){
    String email = dto.email();
    MemberAuth memberAuth = memberAuthRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

    if(passwordEncoder.matches(dto.password(), memberAuth.password())){
      throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }

    Member member = memberRepository.findById(memberAuth.memberId())
        .orElseThrow(()->new IllegalStateException("회원 정보가 존재하지 않습니다."));

    return generateToken(member.id(),email);
  }

  public TokenResponseDto refreshToken(RefreshTokenRequestDto dto){
    String refreshToken = dto.refreshToken();
    if(!jwtProvider.validateToken(refreshToken)){
      throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
    }

    String email = jwtProvider.getEmail(refreshToken);
    Long memberId = jwtProvider.getUserId(refreshToken);
    MemberAuth memberAuth = memberAuthRepository.findById(memberId)
        .orElseThrow(()->new IllegalArgumentException("존재하지 않는 회원입니다."));

    if (!refreshToken.equals(memberAuth.refreshToken())) {
      throw new IllegalArgumentException("리프레시 토큰이 일치하지 않습니다.");
    }

    String accessToken = jwtProvider.createToken(memberId, email, List.of());
    String newRefreshToken = jwtProvider.createRefreshToken(memberId);
    return new TokenResponseDto(accessToken,newRefreshToken);
  }

  public void logout(String email){
    MemberAuth memberAuth = memberAuthRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

    memberAuthRepository.updateRefreshToken(memberAuth.memberId(), null);
  }

  private TokenResponseDto generateToken(Long memberId, String email) {
    String accessToken = jwtProvider.createToken(memberId, email, List.of());
    String refreshToken = jwtProvider.createRefreshToken(memberId);

    memberAuthRepository.updateRefreshToken(memberId,refreshToken);
    return new TokenResponseDto(accessToken,refreshToken);
  }

}
