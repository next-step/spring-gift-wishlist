package gift.member.controller;

import gift.auth.Login;
import gift.member.domain.Member;
import gift.member.dto.MemberLoginRequest;
import gift.member.dto.MemberRegisterRequest;
import gift.member.dto.MemberTokenResponse;
import gift.member.dto.MemberUpdateRequest;
import gift.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("member", MemberLoginRequest.getEmpty());

        return "/members/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("member") MemberLoginRequest loginRequest, HttpServletResponse response, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            return "/members/login";
        }
        MemberTokenResponse tokenResponse = memberService.login(loginRequest);

        addTokenCookie(response, tokenResponse.token());
        return "redirect:/products";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("member", MemberRegisterRequest.getEmpty());

        return "/members/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("member") MemberRegisterRequest registerRequest,
                           BindingResult bindingResult, HttpServletResponse response) {
        if(bindingResult.hasErrors()) {
            return "members/register";
        }

        MemberTokenResponse tokenResponse = memberService.register(registerRequest);
        addTokenCookie(response, tokenResponse.token());

        return "redirect:/products";
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        expireTokenCookie(response);

        return "redirect:/";
    }

    @GetMapping("/mypage")
    public String mypage(@Login Member member, Model model) {
        model.addAttribute("member", member);

        return "members/mypage";
    }

    @GetMapping("/edit")
    public String editPassword(@Login Member member, Model model) {
        model.addAttribute("member", MemberUpdateRequest.getEmpty());

        return "members/edit-password-form";
    }

    @PostMapping("/edit")
    public String editPassword(@Login Member member, @Valid @ModelAttribute("member") MemberUpdateRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "members/edit-password-form";
        }

        memberService.updatePassword(member, request);

        return "redirect:/members/mypage";
    }

    @PostMapping("/delete")
    public String deleteMember(@Login Member member, @RequestParam String password, HttpServletResponse response) {
        memberService.deleteMember(member, password);
        expireTokenCookie(response);

        return "redirect:/";
    }

    private void addTokenCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("jwt-token", token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60);
        response.addCookie(cookie);
    }

    private void expireTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt-token", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
