package gift.auth.service;

import gift.auth.domain.MemberAuth;
import gift.auth.domain.TokenResponse;
import gift.auth.dto.LoginRequestDto;
import gift.auth.dto.LoginResponseDto;
import gift.auth.dto.RefreshTokenRequestDto;
import gift.auth.dto.RegisterMemberRequestDto;
import gift.auth.dto.RegisterMemberResponseDto;
import gift.auth.repository.MemberAuthRepository;
import gift.member.domain.Member;
import gift.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {

  private final MemberAuthRepository memberAuthRepository;
  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final TokenService tokenService;

  public AuthService(MemberAuthRepository memberAuthRepository, MemberRepository memberRepository,
      PasswordEncoder passwordEncoder, TokenService tokenService) {
    this.memberAuthRepository = memberAuthRepository;
    this.memberRepository = memberRepository;
    this.passwordEncoder = passwordEncoder;
    this.tokenService = tokenService;
  }

  @Transactional
  public RegisterMemberResponseDto registerMember(RegisterMemberRequestDto dto) {
    String email = dto.email();
    if (memberAuthRepository.findByEmail(email).isPresent()) {
      throw new IllegalArgumentException("중복된 이메일의 사용자가 존재합니다.");
    }

    Member member = Member.of(dto.username());
    Long memberId = memberRepository.save(member);
    String encodedPassword = passwordEncoder.encode(dto.password());

    MemberAuth memberAuth = MemberAuth.withId(memberId, dto.email(), encodedPassword);
    memberAuthRepository.save(memberAuth);

    TokenResponse tokenResponse = tokenService.generateBearerTokenResponse(memberId, email);
    return RegisterMemberResponseDto.from(tokenResponse, memberId);
  }

  @Transactional
  public LoginResponseDto login(LoginRequestDto dto) {
    String email = dto.email();
    MemberAuth memberAuth = memberAuthRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

    if (!passwordEncoder.matches(dto.password(), memberAuth.password())) {
      throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }

    Member member = memberRepository.findById(memberAuth.memberId())
        .orElseThrow(() -> new IllegalStateException("회원 정보가 존재하지 않습니다."));

    TokenResponse tokenResponse = tokenService.generateBearerTokenResponse(member.id(), email);
    return LoginResponseDto.from(tokenResponse);
  }

  @Transactional
  public LoginResponseDto refreshToken(RefreshTokenRequestDto dto) {
    String refreshToken = dto.refreshToken();
    if (!tokenService.isValidToken(refreshToken)) {
      throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
    }

    String email = tokenService.getEmail(refreshToken);
    Long memberId = tokenService.getUserId(refreshToken);
    MemberAuth memberAuth = memberAuthRepository.findById(memberId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

    if (!refreshToken.equals(memberAuth.refreshToken())) {
      throw new IllegalArgumentException("리프레시 토큰이 일치하지 않습니다.");
    }

    TokenResponse tokenResponse = tokenService.generateBearerTokenResponse(memberId, email);
    return LoginResponseDto.from(tokenResponse);
  }

  @Transactional
  public void logout(String email) {
    MemberAuth memberAuth = memberAuthRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

    memberAuthRepository.updateRefreshToken(memberAuth.memberId(), null);
  }

}
