package gift.service.member;

import gift.JwtProvider;
import gift.entity.Member;
import gift.entity.Token;
import gift.exception.AlreadyRegisterException;
import gift.exception.InvalidPasswordException;
import gift.exception.NotRegisterException;
import gift.repository.member.MemberRepository;
import java.util.List;
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

  @Override
  public List<Member> findAllMember() {
    return repository.findAllMembers();
  }

  @Override
  public Member findMemberById(Long id) {
    return repository.findById(id).orElseThrow(() -> new IllegalStateException("member가 없습니다"));
  }

  @Override
  public Member createMember(Member member) {
    return repository.createMember(member);
  }

  @Override
  public Member updateMember(Long id, Member member) {
    return repository.updateMember(id, member)
        .orElseThrow(() -> new IllegalStateException("member가 없습니다"));
  }

  @Override
  public void deleteMember(Long id) {
    repository.deleteMember(id);
  }
}
