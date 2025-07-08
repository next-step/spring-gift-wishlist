package gift.service;

import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;
import gift.dto.RoleRequestDto;
import gift.dto.TokenResponseDto;
import gift.entity.Member;
import gift.entity.RoleType;
import gift.exception.member.MemberAlreadyExistsException;
import gift.exception.member.InvalidCredentialsException;
import gift.repository.MemberRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public TokenResponseDto registerMember(MemberRequestDto dto) {

        String email = dto.getEmail();
        String password = dto.getPassword();
        RoleType role = RoleType.USER;

        if (memberRepository.findMemberByEmail(email).isPresent()) {
            throw new MemberAlreadyExistsException("이미 해당 이메일로 가입된 회원이 존재합니다.");
        }

        Member member = memberRepository.saveMember(email, password, role);
        String token = accessToken(member);

        return new TokenResponseDto(token);
    }

    @Override
    public TokenResponseDto logInMember(MemberRequestDto dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();

        Optional<Member> findMember = memberRepository.findMemberByEmail(email);
        Member member;

        if (findMember.isEmpty()) {
            throw new InvalidCredentialsException("아이디 또는 비밀번호가 잘못 되었습니다. 아이디와 비밀번호를 정확히 입력해 주세요.");
        }

        member = findMember.get();

        if (!password.equals(member.getPassword())) {
            throw new InvalidCredentialsException("아이디 또는 비밀번호가 잘못 되었습니다. 아이디와 비밀번호를 정확히 입력해 주세요.");
        }

        String token = accessToken(member);

        return new TokenResponseDto(token);
    }

    @Override
    public List<MemberResponseDto> findAllMembers() {
        List<Member> findList = memberRepository.findAllMembers();

        List<MemberResponseDto> dtoList = findList
                .stream()
                .map(x -> new MemberResponseDto(
                        x.getId(),
                        x.getEmail(),
                        x.getPassword(),
                        x.getRole()
                ))
                .toList();

        return dtoList;
    }

    @Override
    public Optional<MemberResponseDto> findMemberById(Long id) {

        return memberRepository.findMemberById(id)
                .map(member -> new MemberResponseDto(
                        member.getId(),
                        member.getEmail(),
                        member.getPassword(),
                        member.getRole()
                ));
    }

    @Override
    public MemberResponseDto findMemberByIdElseThrow(Long id) {

        return memberRepository.findMemberById(id)
                .map(member -> new MemberResponseDto(
                        member.getId(),
                        member.getEmail(),
                        member.getPassword(),
                        member.getRole()
                ))
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "해당 ID의 멤버은 존재하지 않습니다."
                        )
                );
    }

    @Override
    public MemberResponseDto saveMember(MemberRequestDto dto) {

        String email = dto.getEmail();
        String password = dto.getPassword();
        RoleType role = RoleType.USER;

        if (memberRepository.findMemberByEmail(email).isPresent()) {
            throw new MemberAlreadyExistsException("이미 해당 이메일로 가입된 회원이 존재합니다.");
        }

        Member member = memberRepository.saveMember(email, password, role);

        return new MemberResponseDto(member.getId(), member.getEmail(), member.getPassword(), member.getRole());
    }

    @Transactional
    @Override
    public MemberResponseDto updateMember(Long id, RoleRequestDto dto) {

        int updatedNum = memberRepository.updateMember(
                id,
                dto.getRole()
        );

        if (updatedNum == 0) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "해당 ID의 멤버은 존재하지 않습니다."
            );
        }

        return findMemberByIdElseThrow(id);
    }

    @Override
    public void deleteMember(Long id) {
        int deletedNum = memberRepository.deleteMember(id);

        if (deletedNum == 0) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "해당 ID의 멤버은 존재하지 않습니다."
            );
        }
    }

    private String accessToken(Member member) {

        String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

        return Jwts.builder()
            .subject(member.getId().toString())
            .claim("email", member.getEmail())
            .claim("role", member.getRole().name())
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .compact();
    }
}
