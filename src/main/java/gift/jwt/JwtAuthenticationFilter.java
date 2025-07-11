package gift.jwt;

import gift.dto.LoginMember;
import gift.repository.MemberRepository;
import gift.entity.Member;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository; // DB에서 id 조회용

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // "Bearer " 제거

            if (jwtTokenProvider.validateToken(token)) {
                String email = jwtTokenProvider.getEmailFromToken(token);

                Member member = memberRepository.findByEmail(email)
                        .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자"));

                LoginMember loginMember = new LoginMember(member.getId(), member.getEmail());

                request.setAttribute("loginMember", loginMember);
            }
        }

        filterChain.doFilter(request, response);
    }
}
