package gift.service;

import gift.exception.DuplicateEmailException;
import gift.jwt.JwtUtil;
import gift.model.Member;
import gift.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpHeaders;
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
      throw new DuplicateEmailException("이미 존재하는 이메일입니다");
    }
    String encoded = passwordEncoder.encode(password);
    memberRepository.save(new Member(null, email, encoded));
    return jwtUtil.createToken(email);
  }

  public HttpHeaders login(String email, String password){
    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new SecurityException("존재하지 않는 이메일입니다"));
    if(!passwordEncoder.matches(password, member.getPassword())){
      throw new SecurityException("비밀번호가 틀렸습니다");
    }
    String token = jwtUtil.createToken(email);
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    return headers;
  }


  public List<Member> findAll(){
    return memberRepository.findAll();
  }
  public Optional<Member> findByEmail(String email) {
    return memberRepository.findByEmail(email);
  }

}
