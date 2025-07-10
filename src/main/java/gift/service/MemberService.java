package gift.service;

import gift.dto.MemberRequestDto;
import gift.entity.Member;
import gift.repository.MemberRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member getMember(long memberId) {
        return memberRepository.findMemberById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Member 입니다."));
    }

    public Member creatMember(MemberRequestDto memberRequestDto) {
        String passwordHash = BCrypt.hashpw(
                memberRequestDto.password(),
                BCrypt.gensalt());

        if (memberRepository.findMemberByEmail(memberRequestDto.email()).isPresent()){
            throw new IllegalArgumentException("email이 중복 됩니다.");
        }

        long id = memberRepository.saveMember(
                        memberRequestDto.email(),
                        passwordHash)
                .orElseThrow(() -> new IllegalArgumentException("Member를 생성할 수 없습니다."))
                .longValue();

        return new Member(
                id,
                memberRequestDto.email(),
                passwordHash);
    }

    @Transactional(readOnly = true)
    public Member login(MemberRequestDto memberRequestDto) {
        Member member = memberRepository.findMemberByEmail(memberRequestDto.email())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버입니다."));
        if (!BCrypt.checkpw(
                memberRequestDto.password(),
                member.password())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return member;
    }
}
