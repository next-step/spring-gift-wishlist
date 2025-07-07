package gift.service;

import gift.dto.*;
import gift.entity.Member;
import gift.entity.Role;
import gift.exception.InvalidMemberException;
import gift.exception.OperationFailedException;
import gift.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void addMember(MemberAddRequestDto requestDto) {
        validateMemberEmail(requestDto.email(), "admin/memberAdd");
        validateMemberRole(requestDto.role(), "admin/memberAdd");
        Member member = new Member(null, requestDto.email(), requestDto.password(), requestDto.name(), requestDto.role());
        int result = memberRepository.addMember(member);
        if (result == 0) {
            throw new OperationFailedException();
        }
    }

    @Override
    public MemberResponseDto findMemberById(Long id) {
        Member member = memberRepository.findMemberByIdOrElseThrow(id);
        return new MemberResponseDto(member);
    }

    @Override
    public List<MemberResponseDto> findAllMembers() {
        List<Member> members = memberRepository.findAllMembers();
        List<MemberResponseDto> responseDtos = members.stream().map(Member::toMemberResponseDto).toList();
        return responseDtos;
    }

    @Override
    public void updateMemberById(Long id, MemberUpdateRequestDto requestDto) {
        Member member = memberRepository.findMemberByIdOrElseThrow(id);
        if (!member.email().equals(requestDto.email())) {
            validateMemberEmail(requestDto.email(), "admin/memberEdit");
        }
        validateMemberRole(requestDto.role(), "admin/memberEdit");
        Member newMember = new Member(id, requestDto);
        int result = memberRepository.updateMemberById(newMember);
        if (result == 0) {
            throw new OperationFailedException();
        }
    }

    @Override
    public void deleteMemberById(Long id) {
        Member member = memberRepository.findMemberByIdOrElseThrow(id);
        int result = memberRepository.deleteMemberById(member.id());
        if (result == 0) {
            throw new OperationFailedException();
        }
    }

    public void validateMemberEmail(String email, String viewName) {
        Member existing = memberRepository.findMemberByEmail(email);
        if (existing != null) {
            throw new InvalidMemberException("이미 존재하는 이메일입니다.",viewName,"emailErrorMessage");
        }
    }

    public void validateMemberRole(String role, String viewName) {
        if (!Role.isValid(role)){
            throw new InvalidMemberException("잘못된 등급입니다.", viewName, "roleErrorMessage");
        }
    }

}
