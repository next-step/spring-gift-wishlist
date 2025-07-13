package gift.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginMemberIdArgumentResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(LoginMemberId.class)
        && parameter.getParameterType().equals(Long.class);
  }

  @Override
  public Object resolveArgument(
      MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory
  ) throws Exception {

    HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
    Object memberIdAttr = request.getAttribute("memberId");

    if (memberIdAttr == null) {
      throw new IllegalStateException("로그인된 회원 정보가 없습니다.");
    }

    return memberIdAttr;
  }
}
