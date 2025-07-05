package gift.service;

import gift.dto.RegisterMemberRequestDto;
import gift.entity.Member;
import gift.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public MemberService(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    public void registerMember(RegisterMemberRequestDto requestDto) {
        Optional<Member> member = memberRepository.findMemberByEmail(requestDto.email());
        if (member.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 존재하는 이메일 입니다.");
        }
        memberRepository.registerMember(requestDto.email(), bCryptPasswordEncoder.encode(requestDto.password()));
    }
}
