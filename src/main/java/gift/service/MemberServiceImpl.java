package gift.service;

import gift.dto.CreateMemberRequestDto;
import gift.dto.JWTResponseDto;
import gift.dto.UpdateMemberPasswordRequestDto;
import gift.entity.Member;
import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.repository.MemberRepository;
import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final SecretKey key = Jwts.SIG.HS256.key().build();

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public JWTResponseDto createMember(CreateMemberRequestDto requestDto) {
        throwIfMemberFindByEmail(requestDto.email());
        Member newMember = new Member(null, requestDto.email(), requestDto.password(), "user");
        Member savedMember = memberRepository.createMember(newMember);
        String accessToken = createAccessToken(savedMember);
        return new JWTResponseDto(accessToken);
    }

    @Override
    public JWTResponseDto loginMember(CreateMemberRequestDto requestDto) {
        Member find = findMemberByEmailOrElseThrow(requestDto.email());

        if (!isCorrectPassword(find, requestDto.password())) {
            throw new CustomException(ErrorCode.Unauthorized);
        }
        String accessToken = createAccessToken(find);
        return new JWTResponseDto(accessToken);
    }

    @Override
    public void updateMemberPassword(UpdateMemberPasswordRequestDto requestDto) {
        Member find = findMemberByEmailOrElseThrow(requestDto.email());

        if (!isCorrectPassword(find, requestDto.oldPassword())) {
            throw new CustomException(ErrorCode.Unauthorized);
        }
        memberRepository.updateMemberPassword(find, requestDto.newPassword());
    }

    private Member findMemberByEmailOrElseThrow(String email) {
        return memberRepository.findMemberByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NotRegisterd));
    }

    private void throwIfMemberFindByEmail(String email) {
        memberRepository.findMemberByEmail(email)
                .ifPresent(member -> {
                    throw new CustomException(ErrorCode.AlreadyRegistered);
                });
    }

    private Boolean isCorrectPassword(Member member, String password) {
        return member.getPassword().equals(password);
    }

    private String createAccessToken(Member member) {
        String accessToken = Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("email", member.getEmail())
                .claim("password", member.getPassword())
                .claim("role", member.getRole())
                .signWith(key)
                .compact();
        return accessToken;
    }
}
