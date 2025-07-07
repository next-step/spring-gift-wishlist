package gift.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gift.domain.Member;
import gift.dto.AdminMemberResponse;
import gift.dto.LoginRequest;
import gift.dto.LoginResponse;
import gift.dto.RegisterRequest;
import gift.dto.UpdateMemberRequest;
import gift.exception.LoginException;
import gift.exception.RegisterException;
import gift.exception.MemberDeleteException;
import gift.exception.MemberNotFoundException;
import gift.exception.MemberUpdateException;
import gift.repository.MemberRepository;
import gift.util.TokenProvider;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public MemberService(
        MemberRepository memberRepository,
        PasswordEncoder passwordEncoder,
        TokenProvider tokenProvider
    ) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Transactional
    public LoginResponse signup(RegisterRequest request) {
        if (memberRepository.existsByEmail(request.email())) {
            throw new RegisterException("이미 가입된 이메일입니다.");
        }

        Long generatedId = memberRepository.save(Member.of(
            request.email(),
            passwordEncoder.encode(request.password()))
        );

        Member member = memberRepository.findById(generatedId)
            .orElseThrow(() -> new RegisterException("사용자 생성에 실패했습니다."));

        return new LoginResponse(tokenProvider.createToken(member.getId()));
    }

    @Transactional(readOnly = true)
    public LoginResponse signin(LoginRequest request) {
        Member member = memberRepository.findByEmail(request.email())
            .orElseThrow(() -> new LoginException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new LoginException("비밀번호가 일치하지 않습니다.");
        }

        return new LoginResponse(tokenProvider.createToken(member.getId()));
    }

    // TODO: 추후 브라우저 로컬스토리지나 쿠키에 토큰을 저장한다면, 토큰을 invalidate하는 signout 메서드 구현하기!

    @Transactional(readOnly = true)
    public List<AdminMemberResponse> findAll() {
        return memberRepository.findAll()
            .stream()
            .map(AdminMemberResponse::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public AdminMemberResponse findById(Long id) {
        return memberRepository.findById(id)
            .map(AdminMemberResponse::from)
            .orElseThrow(() -> new MemberNotFoundException("해당 유저가 존재하지 않습니다."));
    }

    @Transactional
    public void update(Long id, UpdateMemberRequest request) {
        checkUserExistence(id);

        Member newMember = Member.of(id, request.email(), null);
        int count = memberRepository.update(newMember);
        if (count != 1) {
            throw new MemberUpdateException("유저 이메일 수정을 실패했습니다.");
        }
    }

    @Transactional
    public void delete(Long id) {
        checkUserExistence(id);

        int count = memberRepository.delete(id);
        if (count != 1) {
            throw new MemberDeleteException("유저 삭제를 실패했습니다.");
        }
    }

    private void checkUserExistence(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new MemberNotFoundException("해당 유저가 존재하지 않습니다.");
        }
    }
}
