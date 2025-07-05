package gift.service;

import gift.dto.MemberLoginRequestDto;
import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;
import gift.entity.Member;
import gift.exception.DuplicateMemberException;
import gift.exception.InvalidPasswordException;
import gift.exception.MemberNotFoundException;
import gift.jwt.JwtProvider;
import gift.repository.MemberRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public MemberService(MemberRepository memberRepository,  JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
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
        Member existedMember = memberRepository.findByEmail(requestDto.email())
                .orElseThrow(() -> new MemberNotFoundException(requestDto.email()));

        if (existedMember.getPassword().equals(requestDto.password())) {
             return jwtProvider.generateToken(existedMember);
        }
        else{
            throw new InvalidPasswordException();
        }
    }
}
