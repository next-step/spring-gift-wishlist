package gift.service.member;

import gift.JwtProvider;
import gift.dto.member.MemberRequestDto;
import gift.dto.member.MemberResponseDto;
import gift.entity.Member;
import gift.entity.Token;
import gift.exception.AlreadyRegisterException;
import gift.exception.InvalidPasswordException;
import gift.exception.NotRegisterException;
import gift.exception.notfound.MemberNotFoundException;
import gift.repository.member.MemberRepository;
import java.util.ArrayList;
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
  public Token register(MemberRequestDto requestDto) {
    if (repository.findByEmail(requestDto.getEmail()).isPresent()) {
      throw new AlreadyRegisterException("이미 가입된 이메일입니다");
    }
    Member member = repository.createMember(
        new Member(requestDto.getEmail(), requestDto.getPassword()));
    Token token = jwtProvider.generateToken(member);
    return token;
  }

  @Override
  public Token login(MemberRequestDto requestDto) {
    Optional<Member> memberOptional = repository.findByEmail(requestDto.getEmail());

    Member memberByEmail = memberOptional.orElseThrow(
        () -> new NotRegisterException("가입되지 않은 이메일"));
    if (memberByEmail.isPasswordNotMatch(requestDto.getPassword())) {
      throw new InvalidPasswordException("비밀번호가 일치하지 않습니다");
    }
    Token token = jwtProvider.generateToken(memberByEmail);
    return token;
  }

  @Override
  public List<MemberResponseDto> findAllMember() {
    List<Member> allMembers = repository.findAllMembers();
    List<MemberResponseDto> responseDtoList = new ArrayList<>();
    for (Member member : allMembers) {
      MemberResponseDto responseDto = new MemberResponseDto(member.getId(), member.getEmail(),
          member.getPassword());
      responseDtoList.add(responseDto);
    }
    return responseDtoList;
  }

  @Override
  public MemberResponseDto findMemberById(Long id) {
    return repository.findById(id)
        .map(MemberResponseDto::new)
        .orElseThrow(() -> new MemberNotFoundException("member가 없습니다"));
  }

  @Override
  public MemberResponseDto createMember(MemberRequestDto requestDto) {
    Member member = repository.createMember(
        new Member(requestDto.getEmail(), requestDto.getPassword()));
    return new MemberResponseDto(member);
  }

  @Override
  public MemberResponseDto updateMember(Long id, MemberRequestDto requestDto) {
    return repository.updateMember(id, new Member(requestDto.getEmail(), requestDto.getPassword()))
        .map(MemberResponseDto::new)
        .orElseThrow(() -> new MemberNotFoundException("member가 없습니다"));
  }

  @Override
  public void deleteMember(Long id) {
    repository.deleteMember(id);
  }
}
