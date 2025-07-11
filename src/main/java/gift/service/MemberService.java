package gift.service;

import gift.Entity.Member;
import gift.LoginResult;
import gift.dto.MemberDao;
import gift.Jwt.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberDao memberDao;
    private final JwtUtil jwtUtil;

    public MemberService(MemberDao memberDao, JwtUtil jwtUtil) {
        this.memberDao = memberDao;
        this.jwtUtil = jwtUtil;
    }

    public void register(Member member) {
        if (memberDao.findById(member.getId()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        if (member.getRole() == null) {
            member.setRole("USER");
        }

        memberDao.insertMember(member);
    }

    public LoginResult login(String id, String rawPassword) {
        // 아이디를 탐색하고 없다면 오류메시지를 던짐
        Member member = memberDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));

        // 비밀번호를 탐색하고 일치하지 않다면 오류메시지를 던짐
        if (!rawPassword.equals(member.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        // 위 조건들을 모두 통과하면 토큰을 만듦
        String token = jwtUtil.createToken(member);
        //토큰과 멤버를 반환
        return new LoginResult(token, member);
    }
}