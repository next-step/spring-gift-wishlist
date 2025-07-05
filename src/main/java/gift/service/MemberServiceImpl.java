package gift.service;

import gift.dto.MemberRequestDto;
import gift.dto.TokenResponseDto;
import gift.entity.Member;
import gift.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void saveMember(MemberRequestDto memberRequestDto) {
        memberRepository.saveMember(memberRequestDto.getEmail(), memberRequestDto.getPassword(), memberRequestDto.getRole());
    }
}
