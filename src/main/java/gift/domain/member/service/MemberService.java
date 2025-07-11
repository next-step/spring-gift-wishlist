package gift.domain.member.service;

import gift.domain.auth.service.AuthService;
import gift.domain.member.Member;
import gift.domain.member.RoleType;
import gift.domain.member.dto.MemberInfoResponse;
import gift.domain.member.dto.MemberInfoUpdateRequest;
import gift.domain.member.repository.MemberRepository;
import gift.global.exception.MemberNotFoundException;
import gift.global.exception.NotAdminException;
import gift.global.exception.TokenExpiredException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthService authService;

    public MemberService(MemberRepository memberRepository, AuthService authService) {
        this.memberRepository = memberRepository;
        this.authService = authService;
    }

    public MemberInfoResponse getMemberInfo(Long id, String token) {
        Member member = authService.getMemberByToken(token).orElseThrow(()-> new TokenExpiredException(token));
        if (!member.getRole().equalsIgnoreCase(RoleType.ADMIN.toString())){
            throw new NotAdminException("MemberService : getMemberInfo() failed - member is not admin");
        }
        Member targetMember = memberRepository.findById(id).orElseThrow(
                () -> new MemberNotFoundException("MemberService : getMemberInfo() failed - member not found"));
        return MemberInfoResponse.from(targetMember);
    }

    @Transactional
    public void updateMemberInfo(Long id, String token, MemberInfoUpdateRequest req) {
        Member member = authService.getMemberByToken(token).orElseThrow(()->new TokenExpiredException(token));
        if (!member.getRole().equalsIgnoreCase(RoleType.ADMIN.toString())){
            throw new NotAdminException("MemberService : updateMemberInfo() failed - member is not admin");
        }
        Member targetMember = memberRepository.findById(id).orElseThrow(
                () -> new MemberNotFoundException("MemberService : updateMemberInfo() failed - member not found"));
        targetMember.update(req.email(), req.password(), req.name());
        int affectedRows = memberRepository.update(targetMember);
        if (affectedRows == 0) {
            throw new RuntimeException("MemberService : updateMemberInfo() failed - 500 Internal Server Error");
        }
    }

    @Transactional
    public void deleteMemberInfo(Long id, String token) {
        Member member = authService.getMemberByToken(token).orElseThrow(()->new TokenExpiredException(token));
        if (!member.getRole().equalsIgnoreCase(RoleType.ADMIN.toString())){
            throw new NotAdminException("MemberService : deleteMemberInfo() failed - member is not admin");
        }
        Member targetMember = memberRepository.findById(id).orElseThrow(
                () -> new MemberNotFoundException("MemberService : updateMemberInfo() failed - member not found"));
        memberRepository.delete(targetMember.getId());
    }

    public List<MemberInfoResponse> getMembers(String token) {
        Member member = authService.getMemberByToken(token).orElseThrow(()->new TokenExpiredException(token));
        if (!member.getRole().equalsIgnoreCase(RoleType.ADMIN.toString())){
            throw new NotAdminException("MemberService : getMembers() failed - member is not admin");
        }
        List<Member> members = memberRepository.getAll();
        if (members == null) {
            throw new RuntimeException("ProductService : getAllProducts() failed - 500 Internal Server Error");
        }
        if (members.isEmpty()) {
            return null;
        }
        return members
                .stream()
                .map(MemberInfoResponse::from)
                .toList();
    }
}
