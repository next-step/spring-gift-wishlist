package gift.Controller;

import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;
import gift.model.Member;
import gift.jwt.JwtUtil;
import gift.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/api/members")
public class MemberController {
  private final MemberService memberService;
  private final JwtUtil jwtUtil;

  public MemberController(MemberService memberService,  JwtUtil jwtUtil) {
    this.memberService = memberService;
    this.jwtUtil = jwtUtil;
  }
  @GetMapping("/register")
  public String showRegisterForm(Model model){
    model.addAttribute("memberRequestDto", new MemberRequestDto());
    return "user/register";
  }


  @PostMapping("/register")
  public String register(@Valid @ModelAttribute MemberRequestDto req, BindingResult bindingResult, Model model) {
    if(bindingResult.hasErrors()){
      model.addAttribute("memberRequestDto", req);
      return "user/register";
    }
    try {
      memberService.register(req.getEmail(), req.getPassword());
      return "redirect:/api/members/login";
    } catch (IllegalArgumentException e) {
      model.addAttribute("memberRequestDto", req);
      model.addAttribute("error", e.getMessage());
      return "user/register";
    }
  }

  @GetMapping("/login")
  public String showLoginForm(){
    return "user/login";
  }

  @PostMapping("/login")
  public ResponseEntity<Void> login(@ModelAttribute MemberRequestDto req, Model model, HttpServletResponse response) {
    try{
      String token = memberService.login(req.getEmail(), req.getPassword());
      return ResponseEntity
          .ok()
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
          .build();
    }catch (SecurityException e){
      return ResponseEntity
          .status(HttpStatus.FORBIDDEN)
          .header("X-Error-Message", e.getMessage())
          .build();
    }
  }
}
