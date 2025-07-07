package gift.member.service;

import gift.domain.Member;
import gift.domain.Role;
import gift.global.exception.AuthenticationException;
import gift.global.exception.BadRequestEntityException;
import gift.global.exception.DuplicateEntityException;
import gift.global.exception.NotFoundEntityException;
import gift.member.dto.*;
import gift.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional
@Service
public class MemberServiceV1 implements MemberService{

    public MemberServiceV1(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    private final MemberRepository memberRepository;

    @Override
    public UUID save(MemberCreateDto memberCreateDto) {

        if (!memberCreateDto.getConfirmPassword().equals(memberCreateDto.getPassword()))
            throw new BadRequestEntityException("비밀번호와 확인 비밀번호가 다릅니다.");

        memberRepository.findByEmail(memberCreateDto.getEmail())
                .ifPresent(member -> {
                    throw new DuplicateEntityException(member.getEmail() + "은 이미 존재하는 이메일 입니다.");
                });


        Member saved = memberRepository.save(new Member(memberCreateDto.getEmail(), memberCreateDto.getPassword(), Role.valueOf(memberCreateDto.getRole())));

        return saved.getId();
    }

    @Override
    public void changePassword(String email, MemberUpdateRequest memberUpdateRequest) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundEntityException("존재하는 회원이 아닙니다."));

        if (!member.getPassword().equals(memberUpdateRequest.getPassword()))
            throw new AuthenticationException("비밀번호가 다릅니다.");


        if (!memberUpdateRequest.getConfirmPassword().equals(memberUpdateRequest.getNewPassword()))
            throw new BadRequestEntityException("새로운 비밀번호가 확인 비밀번호와 일치하지 않습니다.");

        memberRepository.update(new Member(member.getId(), member.getEmail(), memberUpdateRequest.getNewPassword(), member.getRole()));
    }

    @Override
    public void updateMemberForAdmin(UUID id, MemberUpdateReqForAdmin memberUpdateReqForAdmin) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException("존재하는 회원이 아닙니다."));

        if (!member.getPassword().equals(memberUpdateReqForAdmin.getPassword()))
            throw new AuthenticationException("비밀번호가 다릅니다.");


        if (!memberUpdateReqForAdmin.getConfirmPassword().equals(memberUpdateReqForAdmin.getNewPassword()))
            throw new BadRequestEntityException("새로운 비밀번호가 확인 비밀번호와 일치하지 않습니다.");

        memberRepository.update(new Member(member.getId(), member.getEmail(),
                memberUpdateReqForAdmin.getNewPassword(), Role.valueOf(memberUpdateReqForAdmin.getRole())));

    }

    @Override
    public MemberResponse findById(UUID id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException("존재하는 회원이 아닙니다."));
        return new MemberResponse(member.getId(), member.getEmail(),member.getRole());
    }

    @Override
    public List<MemberResponse> findAll() {
        return memberRepository.findAll()
                .stream()
                .map(m-> new MemberResponse(m.getId(), m.getEmail(),m.getRole()))
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException("존재하는 회원이 아닙니다."));


        memberRepository.deleteById(member.getId());
    }

    @Override
    public void deleteByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundEntityException("존재하는 회원이 아닙니다."));


        memberRepository.deleteById(member.getId());
    }

    @Override
    public void tokenValidate(String email, String role) {
        Member findMember = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundEntityException("존재하는 회원이 아닙니다."));

        if (!findMember.getRole().toString().equals(role)) {
            throw new NotFoundEntityException("존재하는 회원이 아닙니다.");
        }
    }

    @Override
    public MemberResponse validate(String email, String password) {

        Member findMember = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundEntityException("존재하는 회원이 아닙니다"));

        if (!findMember.getPassword().equals(password))
            throw new AuthenticationException("비밀번호가 다릅니다.");

        return new  MemberResponse(findMember.getId(), findMember.getEmail(), findMember.getRole());
    }
}
