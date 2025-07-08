package gift.controller;

import gift.auth.JwtAuth;
import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;
import gift.exception.MemberExceptions;
import gift.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/members")
public class MemberViewController {

    private final MemberService memberService;
    private final JwtAuth jwtAuth;

    public MemberViewController(MemberService memberService, JwtAuth jwtAuth) {
        this.memberService = memberService;
        this.jwtAuth = jwtAuth;
    }

    @GetMapping("/membership")
    public String showRegisterPage(Model model) {
        model.addAttribute("memberRequestDto", new MemberRequestDto());
        return "member/register";
    }


    @PostMapping("/membership")
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
    public String showLoginPage(Model model) {
        model.addAttribute("memberRequestDto", new MemberRequestDto());
        return "member/login";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberRequestDto memberRequestDto) {
        MemberResponseDto responseDto = memberService.login(memberRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/myInfo")
    public ResponseEntity<?> showInfoPage(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new MemberExceptions.InvalidAuthorizationHeaderException();
        }

        String token = authHeader.replace("Bearer ", "");
        return ResponseEntity.status(HttpStatus.OK).body(new MemberResponseDto(token));
    }
}
