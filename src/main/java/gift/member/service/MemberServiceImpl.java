package gift.member.service;

import gift.member.dto.MemberLoginRequestDto;
import gift.member.dto.MemberLoginResponseDto;
import gift.member.dto.MemberResponseDto;
import gift.member.entity.Member;
import gift.global.exception.InvalidPasswordException;
import gift.global.exception.MemberEmailAlreadyExistsException;
import gift.member.repository.MemberRepository;
import gift.member.util.JwtProvider;
import gift.member.vo.Email;
import gift.member.vo.Password;
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
    public MemberLoginResponseDto register(MemberLoginRequestDto requestDto) {
        if (memberRepository.findByEmail(requestDto.email()) != null) {
            throw new MemberEmailAlreadyExistsException();
        }

        Member member = new Member(
            null,
            new Email(requestDto.email()),
            new Password(requestDto.password())
        );
        member = memberRepository.save(member);

        return new MemberLoginResponseDto(jwtProvider.createToken(member));
    }

    @Override
    public MemberLoginResponseDto findByEmail(MemberLoginRequestDto requestDto) {
        Member member = memberRepository.findByEmail(requestDto.email());

        if (!member.getPassword().matches(requestDto.password())) {
            throw new InvalidPasswordException();
        }

        return new MemberLoginResponseDto(jwtProvider.createToken(member));
    }

    @Override
    public List<MemberResponseDto> findAll() {
        return memberRepository
            .findAll()
            .stream()
            .map(MemberResponseDto::from)
            .toList();
    }

    @Override
    public MemberResponseDto findById(Long id) {
        return MemberResponseDto.from(memberRepository.findById(id));
    }

    @Override
    public void update(Long id, MemberLoginRequestDto requestDto) {
        Member member = new Member(
            id,
            new Email(requestDto.email()),
            new Password(requestDto.password())
        );
        memberRepository.update(member);
    }

    @Override
    public void delete(Long id) {
        memberRepository.delete(id);
    }
}
