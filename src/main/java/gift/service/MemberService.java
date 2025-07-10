package gift.service;

import gift.dto.LoginResponse;
import gift.dto.MemberRequest;
import gift.dto.MemberResponse;
import gift.entity.Member;
import gift.exception.LoginFailedException;
import gift.exception.MemberAlreadyExistsException;
import gift.exception.MemberNotFoundException;
import gift.repository.MemberRepository;
import gift.util.JwtUtil;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public MemberService(MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public LoginResponse register(MemberRequest request) {
        memberRepository.findByEmail(request.email())
                .ifPresent(m -> {
                    throw new MemberAlreadyExistsException("이미 가입된 이메일입니다.");
                });

        String encodedPassword = BCrypt.hashpw(request.password(), BCrypt.gensalt());
        Member member = new Member(request.email(), encodedPassword);

        memberRepository.save(member);
        String token = jwtUtil.generateToken(member.getEmail());
        return new LoginResponse(token);
    }

    public LoginResponse login(MemberRequest request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new LoginFailedException("이메일 또는 비밀번호가 유효하지 않습니다."));

        if (!BCrypt.checkpw(request.password(), member.getPassword())) {
            throw new LoginFailedException("이메일 또는 비밀번호가 유효하지 않습니다.");
        }

        String token = jwtUtil.generateToken(member.getEmail());
        return new LoginResponse(token);
    }

    public void updateMember(Long id, MemberRequest request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("해당 ID의 회원을 찾을 수 없습니다."));

        if (!member.getEmail().equals(request.email())) {
            memberRepository.findByEmail(request.email())
                    .ifPresent(m -> {
                        throw new MemberAlreadyExistsException("이미 사용 중인 이메일입니다.");
                    });
        }

        String password = request.password().isBlank() ? member.getPassword() : BCrypt.hashpw(request.password(), BCrypt.gensalt());

        Member updatedMember = new Member(id, request.email(), password);
        memberRepository.update(updatedMember);
    }

    public void deleteMember(Long id) {
        memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("해당 ID의 회원을 찾을 수 없습니다."));
        memberRepository.deleteById(id);
    }

    public List<MemberResponse> findAllMembers() {
        return memberRepository.findAll().stream()
                .map(MemberResponse::from)
                .collect(Collectors.toList());
    }

    public MemberResponse findMemberById(Long id) {
        return memberRepository.findById(id)
                .map(MemberResponse::from)
                .orElseThrow(() -> new MemberNotFoundException("해당 ID의 회원을 찾을 수 없습니다."));
    }
}