package gift.service;

import gift.dto.CreateMemberRequestDto;
import gift.dto.DeleteMemberRequestDto;
import gift.dto.JWTResponseDto;
import gift.dto.UpdateMemberPasswordRequestDto;
import gift.entity.Member;
import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final TokenService tokenService;

    public MemberServiceImpl(MemberRepository memberRepository, TokenService tokenService) {
        this.memberRepository = memberRepository;
        this.tokenService = tokenService;
    }

    @Override
    public JWTResponseDto createMember(CreateMemberRequestDto requestDto) {
        throwIfMemberFindByEmail(requestDto.email());
        Member newMember = new Member(null, requestDto.email(), requestDto.password(), "user");
        Member savedMember = memberRepository.createMember(newMember);
        String accessToken = tokenService.createAccessToken(savedMember);
        return new JWTResponseDto(accessToken);
    }

    @Override
    public JWTResponseDto loginMember(CreateMemberRequestDto requestDto) {
        Member find = findMemberByEmailOrElseThrow(requestDto.email());

        if (!isCorrectPassword(find, requestDto.password())) {
            throw new CustomException(ErrorCode.Unauthorized);
        }
        String accessToken = tokenService.createAccessToken(find);
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

    @Override
    public void deleteMember(DeleteMemberRequestDto requestDto) {
        Member find = findMemberByEmailOrElseThrow(requestDto.email());

        if (!isCorrectPassword(find, requestDto.password())) {
            throw new CustomException(ErrorCode.Unauthorized);
        }
        memberRepository.deleteMember(find);
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
}
