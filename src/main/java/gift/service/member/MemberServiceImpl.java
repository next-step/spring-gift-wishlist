package gift.service.member;

import gift.JwtProvider;
import gift.entity.Member;
import gift.entity.Token;
import gift.exception.AlreadyRegisterException;
import gift.exception.InvalidPasswordException;
import gift.exception.NotRegisterException;
import gift.repository.member.MemberRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

  private final MemberRepository repository;
  private final JwtProvider jwtProvider;

  public MemberServiceImpl(MemberRepository repository, JwtProvider jwtProvider) {
    this.repository = repository;
    this.jwtProvider = jwtProvider;
  }


  @Override
  public Token register(Member member) {
    if (repository.findByEmail(member.getEmail()).isPresent()) {
      throw new AlreadyRegisterException("이미 가입된 이메일입니다");
    }
    repository.createMember(member);
    Token token = jwtProvider.generateToken(member);
    return token;
  }

  @Override
  public Token login(Member member) {
    Optional<Member> memberOptional = repository.findByEmail(member.getEmail());

    Member memberByEmail = memberOptional.orElseThrow(
        () -> new NotRegisterException("가입되지 않은 이메일"));
    if (memberByEmail.getPassword().equals(member.getPassword()) == false) {
      throw new InvalidPasswordException("비밀번호가 일치하지 않습니다");
    }
    Token token = jwtProvider.generateToken(memberByEmail);
    return token;
  }
}
