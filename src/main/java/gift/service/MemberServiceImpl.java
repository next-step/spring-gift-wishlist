package gift.service;

import gift.domain.Member;
import gift.exception.DuplicateMemberException;
import gift.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    @Override
    public void register(String email, String password){
        memberRepository.findByEmail(email)
                .ifPresent(member -> {
                    throw new DuplicateMemberException();
                });
        Member newMember = new Member(null,email,password);
        memberRepository.register(newMember);
    }
}
