package gift.exception;

import gift.dto.MemberRequestDto;
import gift.model.Member;
import gift.model.WishItem;
import gift.service.WishlistService;
import gift.util.LoginMember;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@ControllerAdvice
public class GlobalExceptionHandler {

  private final WishlistService wishlistService;

  public GlobalExceptionHandler(WishlistService wishlistService) {
    this.wishlistService = wishlistService;
  }

  // ✅ 이메일/비밀번호 형식 오류 등 바인딩 예외
  @ExceptionHandler(BindException.class)
  public String handleBindException(BindException ex,
      HttpServletRequest request,
      Model model,
      HttpServletResponse response) {
    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    BindingResult bindingResult = ex.getBindingResult();

    model.addAttribute("memberRequestDto", bindingResult.getTarget());
    model.addAttribute("org.springframework.validation.BindingResult.memberRequestDto", bindingResult);

    return "user/register";
  }

  // ✅ 중복 이메일 예외
  @ExceptionHandler(DuplicateEmailException.class)
  public String handleDuplicateEmail(DuplicateEmailException ex,
      HttpServletRequest request,
      Model model,
      HttpServletResponse response) {
    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

    MemberRequestDto dto = new MemberRequestDto();
    dto.setEmail(request.getParameter("email"));
    dto.setPassword(request.getParameter("password"));

    model.addAttribute("memberRequestDto", dto);
    model.addAttribute("error", ex.getMessage());

    return "user/register";
  }

  // ✅ 로그인 실패 (SecurityException) 처리
  @ExceptionHandler(SecurityException.class)
  public ResponseEntity<String> handleLoginFail(SecurityException ex) {
    return ResponseEntity
        .status(HttpStatus.UNAUTHORIZED)
        .header("X-Error-Message", ex.getMessage())
        .body("로그인 실패: " + ex.getMessage());
  }

  // 위시리스트 수량이 0 이하인 경우
  @ExceptionHandler(InvalidQuantityException.class)
  public String handleInvalidQuantity(InvalidQuantityException ex,
      HttpServletRequest request,
      Model model,
      HttpServletResponse response,
      @LoginMember Member member) {

    response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 상태코드 유지

    // 다시 wishlist를 채워서 렌더링
    List<WishItem> wishList = wishlistService.getWishList(member.getId());
    model.addAttribute("wishList", wishList);
    model.addAttribute("error", ex.getMessage());

    return "wishlist/list"; // 리디렉션 아님, 직접 렌더링
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public String handleIllegalArgument(IllegalArgumentException ex,
      Model model,
      HttpServletResponse response,
      @LoginMember Member member) {
    response.setStatus(HttpServletResponse.SC_NOT_FOUND);

    // 다시 wishlist를 채워서 렌더링
    List<WishItem> wishList = wishlistService.getWishList(member.getId());
    model.addAttribute("wishList", wishList);
    model.addAttribute("error", ex.getMessage());
    return "wishlist/list";
  }
}


