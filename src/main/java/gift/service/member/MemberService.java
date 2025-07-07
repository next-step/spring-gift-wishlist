package gift.service.member;

import gift.domain.Member;
import gift.dto.member.MemberRequest;
import gift.dto.member.MemberResponse;
import gift.repository.member.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Long insert(MemberRequest request){

        return memberRepository.insert(new Member(null, request.email(), request.password()));
    }

    public MemberResponse findById(Long memberId){
        Member member = memberRepository.findById(memberId);
        return new MemberResponse(member.getEmail(), member.getPassword());
    }

    public void update(Long memberId, MemberRequest request){
        memberRepository.findById(memberId);

        memberRepository.update(memberId, request);
    }

    public void deleteById(Long memberId){
        memberRepository.findById(memberId);

        memberRepository.deleteById(memberId);
    }
}
