package gift.controller;

import gift.dto.*;
import gift.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MemberViewController {

    private final MemberService memberService;

    public MemberViewController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("dto", new MemberRegisterRequestDto("", "", ""));
        return "member/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute MemberRegisterRequestDto dto) {
        memberService.register(dto);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "member/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute MemberLoginRequestDto dto, Model model) {
        try {
            TokenResponseDto tokenDto = memberService.login(dto);
            model.addAttribute("token", tokenDto.token());
            return "redirect:/admin/members";
        } catch (Exception e) {
            model.addAttribute("error", "로그인 실패");
            return "member/login";
        }
    }

    @PostMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }

    @GetMapping("/admin/members")
    public String memberList(Model model) {
        model.addAttribute("members", memberService.findAllMembers());
        return "member/list";
    }

    @GetMapping("/admin/members/{id}/update")
    public String updateForm(@PathVariable Long id, Model model) {
        MemberResponseDto memberResponseDto = memberService.findMemberById(id);
        model.addAttribute("id", id);
        model.addAttribute("dto", new MemberUpdateRequestDto(memberResponseDto.name(), memberResponseDto.email(), ""));
        return "member/update";
    }

    @PostMapping("/admin/members/{id}/update")
    public String update(@PathVariable Long id, @ModelAttribute MemberUpdateRequestDto dto) {
        memberService.updateMember(id, dto);
        return "redirect:/admin/members";
    }
}
