package gift.controller;

import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;
import gift.exception.MemberExceptions;
import gift.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/members")
public class MemberViewController {

    private final MemberService memberService;

    public MemberViewController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("memberRequestDto", new MemberRequestDto());
        return "member/register";
    }


    @PostMapping("/register")
    public String register(
            @ModelAttribute("memberRequestDto") MemberRequestDto memberRequestDto,
            BindingResult bindingResult,
            Model model) {

        MemberResponseDto responseDto = null;
        try {
            responseDto = memberService.register(memberRequestDto);
            model.addAttribute("jwtToken", responseDto.getToken());
        } catch (MemberExceptions.EmailAlreadyExistsException e) {
            bindingResult.rejectValue("email", "ExistEmail", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
        }

        if (bindingResult.hasErrors()) {
            return "member/register";
        }

        model.addAttribute("successMessage", "회원가입이 완료되었습니다.");
        return "member/register";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "member/login";
    }
}
