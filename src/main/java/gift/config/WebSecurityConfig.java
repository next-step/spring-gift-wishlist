package gift.config;

import gift.service.MemberService;
import gift.token.JwtTokenFilter;
import gift.token.JwtTokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static gift.entity.Role.*;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    public WebSecurityConfig(JwtTokenProvider jwtTokenProvider, MemberService memberService, PasswordEncoder passwordEncoder) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtTokenFilter jwtTokenFilter = new JwtTokenFilter(jwtTokenProvider);

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedEntryPoint()))
                .authorizeHttpRequests(auth -> auth
                        // 회원가입/로그인
                        .requestMatchers("/api/members/**").permitAll()
                        // 상품 카테고리
                        .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll() // (전체)상품 조회
                        .requestMatchers(HttpMethod.POST, "/api/products").hasAnyAuthority(ROLE_SELLER.name(), ROLE_MD.name()) // 상품 등록
                        .requestMatchers("/api/products/**").hasAnyAuthority(ROLE_MD.name()) // 상품 수정/삭제
                        // 관리자 페이지 - 멤버관리
                        .requestMatchers(HttpMethod.GET, "/admin/members/**").hasAnyAuthority(ROLE_ADMIN.name(), ROLE_CS.name()) // AdminPage (전체)회원 조회
                        .requestMatchers("/admin/members/**").hasAuthority(ROLE_ADMIN.name()) // AdminPage 회원 수정/삭제/권한변경
                        // 관리자 페이지 - 상품관리
                        .requestMatchers("/admin/products/**").hasAuthority(ROLE_MD.name()) // AdminPage 상품 등록/수정/삭제/검증
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(memberService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}
