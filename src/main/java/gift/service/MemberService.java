package gift.service;

import gift.jwt.JwtUtil;
import gift.model.Member;
import gift.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class MemberService {
  private final MemberRepository memberRepository;
  private final JwtUtil jwtUtil;
  private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  public MemberService(MemberRepository memberRepository, JwtUtil jwtUtil) {
    this.memberRepository = memberRepository;
    this.jwtUtil = jwtUtil;
  }

  public String register(String email, String password){
    if(memberRepository.findByEmail(email).isPresent()){
      throw new IllegalArgumentException("이미 존재하는 이메일 입니다");
    }
    String encoded =  passwordEncoder.encode(password);
    memberRepository.save(new Member(null, email, encoded));
    return jwtUtil.createToken(email);
  }

  public String login(String email, String password){
    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다"));
    if(!passwordEncoder.matches(password, member.getPassword())){
      throw new IllegalArgumentException("비밀번호가 틀렸습니다");
    }
    return jwtUtil.createToken(email);
  }
}
