package gift.service;

import gift.dto.MemberLoginRequestDto;
import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;
import gift.entity.Member;
import gift.exception.DuplicateMemberException;
import gift.repository.MemberRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    public MemberResponseDto create(MemberRequestDto requestDto) {
        Optional<Member> existedMember = memberRepository.findByEmail(requestDto.email());
        if (existedMember.isPresent()) {
            throw new DuplicateMemberException(requestDto.email());
        }

        Member member = new Member(requestDto.name(), requestDto.email(), requestDto.password());
        Member newMember = memberRepository.save(member);

        return new MemberResponseDto(newMember.getId(), newMember.getName(), newMember.getEmail(), newMember.getPassword());
    }

    public String login(@Valid MemberLoginRequestDto requestDto) {
        Optional<Member> existedMember = memberRepository.findByEmail(requestDto.email());
        if (existedMember.isPresent()) {
            if (existedMember.get().getPassword().equals(requestDto.password())) {
                return "success";
            }
            else{
                return "fail";
            }
        }
        else{
            return "fail";
        }
    }
}
