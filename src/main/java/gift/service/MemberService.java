package gift.service;

import gift.dto.MemberLogInRequestDto;
import gift.dto.MemberLogInResponseDto;
import gift.dto.MemberResponseDto;

import java.util.List;

public interface MemberService {
    MemberLogInResponseDto registerMember(MemberLogInRequestDto requestDto);
    MemberLogInResponseDto findMemberToLogIn(MemberLogInRequestDto requestDto);
    List<MemberResponseDto> findAllMembers();
    MemberResponseDto findMemberById(Long id);
    MemberResponseDto updateMember(Long id, MemberLogInRequestDto requestDto);
    void deleteMember(Long id);
}
