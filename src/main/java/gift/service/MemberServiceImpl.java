package gift.service;

import gift.dto.MemberLoginRequestDto;
import gift.dto.MemberRegisterRequestDto;
import gift.entity.Member;
import gift.entity.Role;
import gift.repository.MemberRepository;
import gift.config.JwtProvider;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public MemberServiceImpl(MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public String register(MemberRegisterRequestDto memberRegisterRequestDto) {
        String rawPassword = memberRegisterRequestDto.password();
        Member member = new Member(
                null,
                memberRegisterRequestDto.name(),
                memberRegisterRequestDto.email(),
                rawPassword,
                Role.USER
        );
        Member saved = memberRepository.saveMember(member);
        return jwtProvider.generateToken(saved.getId(), saved.getEmail(), saved.getRole().name());
    }

    @Override
    public String login(MemberLoginRequestDto memberLoginRequestDto) {
        Member member = memberRepository.findMemberByEmail(memberLoginRequestDto.email())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!memberLoginRequestDto.password().equals(member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return jwtProvider.generateToken(member.getId(), member.getEmail(), member.getRole().name());
    }

    @Override
    public List<Member> findAllMembers() {
        return memberRepository.findAllMembers();
    }

    @Override
    public Member findMemberById(Long id) {
        return memberRepository.findMemberById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }

    @Override
    public void updateMember(Long id, MemberRegisterRequestDto memberRegisterRequestDto) {
        Member member = findMemberById(id);
        member.update(memberRegisterRequestDto.name(), memberRegisterRequestDto.email(), memberRegisterRequestDto.password());
        memberRepository.updateMember(member);
    }

    @Override
    public void deleteMember(Long id) {
        memberRepository.deleteMember(id);
    }
}
