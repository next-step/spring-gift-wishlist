package gift.service;

import gift.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MemberService {
    public void addMember(MemberAddRequestDto requestDto);
    public MemberResponseDto findMemberById(Long id);
    public List<MemberResponseDto> findAllMembers();
    public void updateMemberById(Long id, MemberUpdateRequestDto requestDto);
    public void deleteMemberById(Long id);
}
