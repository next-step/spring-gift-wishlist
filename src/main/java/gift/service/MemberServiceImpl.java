package gift.service;

import gift.dto.MemberRequestDto;
import gift.dto.TokenResponseDto;
import gift.entity.Member;
import gift.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    @Override
    public void login(MemberRequestDto memberRequestDto) {
        int membercount = memberRepository.countMember(memberRequestDto.getEmail(), memberRequestDto.getPassword());
        if(membercount < 1) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "아이디 또는 비밀번호가 올바르지 않습니다.");
        }
    }


}
