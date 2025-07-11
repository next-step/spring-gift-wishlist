package gift.config;

import gift.dto.member.MemberResponseDto;
import gift.entity.Member;
import gift.service.member.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

  private final String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

  private final MemberService service;

  public LoginMemberArgumentResolver(MemberService service) {
    this.service = service;
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(LoginMember.class);
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

    String authHeader = webRequest.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new IllegalStateException("no authorization ");
    }

    String token = authHeader.substring(7);

    Claims claims = Jwts.parser()
        .setSigningKey(secretKey.getBytes())
        .build()
        .parseClaimsJws(token)
        .getBody();
    Long memberId = Long.parseLong(claims.getSubject());

    MemberResponseDto responseDto = service.findMemberById(memberId);
    return new Member(responseDto.getId(), responseDto.getEmail(), responseDto.getPassword());
  }
}
