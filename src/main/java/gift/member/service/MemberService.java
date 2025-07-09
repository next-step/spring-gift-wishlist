package gift.member.service;

import gift.member.dto.MemberLoginRequestDto;
import gift.member.dto.MemberLoginResponseDto;
import gift.member.dto.MemberResponseDto;

import java.util.List;

public interface MemberService {
    MemberLoginResponseDto register(MemberLoginRequestDto requestDto);
    MemberLoginResponseDto findByEmail(MemberLoginRequestDto requestDto);
    List<MemberResponseDto> findAll();
    MemberResponseDto findById(Long id);
    void update(Long id, MemberLoginRequestDto requestDto);
    void delete(Long id);
}
