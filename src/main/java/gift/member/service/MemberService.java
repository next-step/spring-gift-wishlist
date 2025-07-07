package gift.member.service;

import gift.exception.EmailExistsException;
import gift.exception.MemberNotFoundByEmailException;
import gift.exception.MemberNotFoundByIdException;
import gift.member.dto.request.UpdateRequestDto;
import gift.member.dto.response.MemberResponseDto;
import gift.member.dto.request.RegisterRequestDto;
import gift.member.entity.Member;
import gift.member.repository.MemberRepository;
import jakarta.validation.constraints.Email;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponseDto register(RegisterRequestDto registerRequestDto) {
        if(memberRepository.findByEmail(registerRequestDto.email()).isPresent()) {
            throw new EmailExistsException();
        }

        Member member = new Member(
                registerRequestDto.email(),
                registerRequestDto.password(),
                "USER");

        return MemberResponseDto.from(memberRepository.save(member));
    }

    public List<MemberResponseDto> getMembers() {
        return memberRepository.findAll()
                .stream()
                .map(MemberResponseDto::from)
                .toList();
    }

    public MemberResponseDto getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundByIdException(id));

        return MemberResponseDto.from(member);
    }

    public Member getMemberByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundByEmailException(email));

        return member;
    }

    public MemberResponseDto updateMember(Long id, UpdateRequestDto updateRequestDto) {
        memberRepository.findByEmail(updateRequestDto.email())
                .filter(foundMember -> !foundMember.getId().equals(id))
                .ifPresent(m -> {
                    throw new EmailExistsException();
                });

        Member memberToUpdate = new Member(
                id,
                updateRequestDto.email(),
                updateRequestDto.password(),
                updateRequestDto.role()
        );

        return MemberResponseDto.from(memberRepository.update(memberToUpdate));
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
}
