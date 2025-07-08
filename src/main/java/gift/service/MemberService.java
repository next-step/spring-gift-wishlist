package gift.service;

import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;
import gift.dto.RoleRequestDto;
import gift.dto.TokenResponseDto;

import java.util.List;
import java.util.Optional;

public interface MemberService {

    TokenResponseDto registerMember(MemberRequestDto dto);
    TokenResponseDto logInMember(MemberRequestDto dto);

    List<MemberResponseDto> findAllMembers();
    Optional<MemberResponseDto> findMemberById(Long id);
    MemberResponseDto findMemberByIdElseThrow(Long id);
    MemberResponseDto saveMember(MemberRequestDto dto);
    MemberResponseDto updateMember(Long id, RoleRequestDto dto);
    void deleteMember(Long id);
}
