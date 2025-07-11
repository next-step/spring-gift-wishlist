package gift.yjshop.controller;

import gift.dto.MemberRequestDto;
import gift.entity.Member;
import gift.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;
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

    public LoginViewController(MemberService memberService){
        this.memberService = memberService;
    }

    //회원 가입 화면을 보여주는 메서드
    @GetMapping("/registerform")
    public String registerForm(Model model){
        model.addAttribute("memberRequestDto", new MemberRequestDto(null, null));
        return "/yjshop/register";
    }

    //회원가입 기능 -> 토큰을 반환 (쿠키에 저장)
    @PostMapping("/register")
    public String register(
            @ModelAttribute @Valid MemberRequestDto memberRequestDto,
            BindingResult bindingResult,
            HttpServletResponse response,
            HttpServletRequest request
    ) throws IOException {

        if(memberService.getMemberByEmail(memberRequestDto.email()).isPresent()){
            bindingResult.addError(new FieldError("memberRequestDto", "email", "이미 사용중인 이메일 입니다."));
        }

        if(bindingResult.hasErrors()){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "/yjshop/register";
        }

        Member member = memberService.register(memberRequestDto);
        request.setAttribute("memberId", member.getMemberId());
        response.setStatus(HttpServletResponse.SC_CREATED);
        return "redirect:/view/loginform";
    }

    //로그인 화면을 보여주는 메서드
    @GetMapping("/loginform")
    public String loginForm(Model model){
        model.addAttribute("memberRequestDto", new MemberRequestDto(null, null));
        return "/yjshop/login";
    }

    //로그인 기능 -> 토큰을 반환(쿠키에 저장)
    @PostMapping("/login")
    public String login(
            @ModelAttribute @Valid MemberRequestDto memberRequestDto,
            BindingResult bindingResult,
            HttpServletResponse response,
            HttpServletRequest request
    ) {

        if(!memberService.checkMember(memberRequestDto)){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            bindingResult.addError(new FieldError("memberRequestDto", "email", "아이디 또는 비밀번호가 일치하지 않습니다."));
        }

        if(bindingResult.hasErrors()){
            return "/yjshop/login";
        }

        //서버에 저장된 id-pw 쌍과 일치한다면 토큰을 발급
        Optional<Member> member = memberService.getMemberByEmail(memberRequestDto.email());
        request.setAttribute("memberId", member.get().getMemberId());
        response.setStatus(HttpServletResponse.SC_CREATED);
        return "redirect:/view/products/list";
    }

    //로그아웃 기능 -> 토큰을 만료시킴
    @GetMapping("/my/logout")
    public String logout(HttpServletResponse response){

        Cookie logoutcookie = new Cookie("yjtoken", null);
        logoutcookie.setPath("/");
        logoutcookie.setMaxAge(0); //즉시 만료되는 토큰을 발행
        response.addCookie(logoutcookie);

        return "redirect:/view/loginform";
    }

}
