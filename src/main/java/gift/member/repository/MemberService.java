package gift.member.repository;

import gift.member.dto.MemberResponseDto;
import gift.member.dto.RegisterRequestDto;
import gift.member.entity.Member;
import gift.member.service.MemberRepository;
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
            throw new IllegalArgumentException("해당 이메일은 이미 가입되어 있습니다.");
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
                .orElseThrow(() -> new IllegalArgumentException(id + "에 해당하는 멤버가 없습니다."));

        return MemberResponseDto.from(member);
    }

    public Member getMemberByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(email + "에 해당하는 멤버가 없습니다."));

        return member;
    }

    public MemberResponseDto updateMember(Member member) {
        memberRepository.findByEmail(member.getEmail())
                .filter(foundMember -> !foundMember.getId().equals(member.getId()))
                .ifPresent(m -> {
                    throw new IllegalArgumentException("변경하려는 이메일이 이미 사용 중인 이메일입니다.");
                });

        return MemberResponseDto.from(memberRepository.update(member));
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
}
