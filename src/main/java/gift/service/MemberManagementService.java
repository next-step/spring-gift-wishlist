package gift.service;

import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;

import java.util.List;

public interface MemberManagementService {
    List<MemberResponseDto> getAllMembers();
    MemberResponseDto addMember(MemberRequestDto memberRequestDto);
    MemberResponseDto updateMember(Long id, MemberRequestDto memberRequestDto);
    MemberResponseDto deleteMember(Long id);

}
