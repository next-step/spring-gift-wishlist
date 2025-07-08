package gift.service;

import gift.Entity.Member;
import gift.dto.MemberDto;
import gift.Jwt.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberDto memberDto;
    private final JwtUtil jwtUtil;

    public MemberService(MemberDto memberDto, JwtUtil jwtUtil) {
        this.memberDto = memberDto;
        this.jwtUtil = jwtUtil;
    }

    public void register(Member member) {
        if (memberDto.selectId(member.getId()) != null) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        if (member.getRole() == null) {
            member.setRole("USER");
        }

        memberDto.insertMember(member);
    }

    public String login(String id, String rawPassword) {
        Member member = memberDto.selectId(id);

        if (member == null || !rawPassword.equals(member.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        return jwtUtil.createToken(member);
    }
}
