package gift.controller.view;

import gift.dto.MemberRequestDto;
import gift.entity.Member;
import gift.service.AuthServiceJWTandCookie;
import gift.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/view")
@Controller
public class LoginViewController {

    private final MemberService memberService;

    private final AuthServiceJWTandCookie authServiceJWTandCookie;


    public LoginViewController(MemberService memberService, AuthServiceJWTandCookie authServiceJWTandCookie){
        this.memberService = memberService;
        this.authServiceJWTandCookie = authServiceJWTandCookie;
    }

    //회원 가입 화면을 보여주는 메서드
    @GetMapping("/register")
    public String registerForm(Model model){
        model.addAttribute("memberRequestDto", new MemberRequestDto(null, null));
        return "/view/register";
    }

    //회원가입 기능 -> 토큰을 반환 (쿠키에 저장)
    @PostMapping("/register")
    public String register(
            @ModelAttribute @Valid MemberRequestDto memberRequestDto,
            BindingResult bindingResult,
            HttpServletResponse response
    ){

        if(memberService.getMemberByEmail(memberRequestDto.email()).isPresent()){
            bindingResult.addError(new FieldError("memberRequestDto", "email", "이미 사용중인 이메일 입니다."));
        }

        if(bindingResult.hasErrors()){
            return "view/register";
        }

        Member member = memberService.register(memberRequestDto);
        String token = authServiceJWTandCookie.createJwt(member.getEmail(), member.getMemberId());

        Cookie tcookie = new Cookie("token", token);
        tcookie.setPath("/");
        response.addCookie(tcookie);
        return "redirect:/view/login";
    }

    //로그인 화면을 보여주는 메서드
    @GetMapping("/login")
    public String loginForm(Model model){
        model.addAttribute("memberRequestDto", new MemberRequestDto(null, null));
        return "/view/login";
    }

    //로그인 기능 -> 토큰을 반환(쿠키에 저장)
    @PostMapping("/login")
    public String login(
            @ModelAttribute @Valid MemberRequestDto memberRequestDto,
            BindingResult bindingResult,
            HttpServletResponse response,
            Model model) {

        if(!memberService.checkMember(memberRequestDto)){
            bindingResult.addError(new FieldError("memberRequestDto", "email", "아이디 또는 비밀번호가 일치하지 않습니다."));
        }

        if(bindingResult.hasErrors()){
            return "view/login";
        }

        //서버에 저장된 id-pw 쌍과 일치한다면 토큰을 발급
        Member member = memberService.getMemberByEmail(memberRequestDto.email()).get();
        String token = authServiceJWTandCookie.createJwt(member.getEmail(), member.getMemberId());

        //발급된 토큰을 쿠키에 저장
        Cookie tcookie = new Cookie("token", token);
        tcookie.setPath("/");
        response.addCookie(tcookie);

        return "redirect:/view/products/list";
    }

    //로그아웃 기능 -> 토큰을 만료시킴
    @GetMapping("/logout")
    public String logout(HttpServletResponse response){

        Cookie logoutcookie = new Cookie("token", null);
        logoutcookie.setPath("/");
        logoutcookie.setMaxAge(0); //즉시 만료되는 토큰을 발행
        response.addCookie(logoutcookie);

        return "redirect:/view/login";
    }

}
