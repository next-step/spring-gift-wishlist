package gift.service.member;

import gift.dto.member.MemberRequestDto;
import gift.dto.member.MemberResponseDto;
import gift.entity.Token;
import java.util.List;

public interface MemberService {

  public Token register(MemberRequestDto requestDto);

  public Token login(MemberRequestDto requestDto);

  List<MemberResponseDto> findAllMember();

  MemberResponseDto findMemberById(Long id);

  MemberResponseDto createMember(MemberRequestDto requestDto);

  MemberResponseDto updateMember(Long id, MemberRequestDto requestDto);

  void deleteMember(Long id);
}
