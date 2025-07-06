package gift.member.service;

import gift.member.dto.AdminMemberCreateRequestDto;
import gift.member.dto.AdminMemberGetResponseDto;
import gift.member.dto.AdminMemberUpdateRequestDto;
import gift.member.dto.RegisterRequestDto;
import gift.member.dto.TokenResponseDto;
import java.util.List;

public interface MemberService {

    TokenResponseDto registerMember(RegisterRequestDto requestDto);

    void findMemberByEmail(RegisterRequestDto requestDto);

    void saveMember(AdminMemberCreateRequestDto requestDto);

    List<AdminMemberGetResponseDto> findAllMembers();

    AdminMemberGetResponseDto findMemberById(Long memberId);

    void updateMemberById(Long memberId, AdminMemberUpdateRequestDto requestDto);

    void deleteMemberById(Long memberId);
}
