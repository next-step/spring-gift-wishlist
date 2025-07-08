package gift.controller;

import gift.dto.MemberLoginRequestDto;
import gift.dto.MemberRegisterRequestDto;
import gift.dto.MemberUpdateRequestDto;
import gift.entity.Member;
import gift.repository.MemberRepository;
import gift.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MemberViewController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    public MemberViewController(MemberService memberService, MemberRepository memberRepository) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
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
            String token = memberService.login(dto);
            model.addAttribute("token", token);
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
        model.addAttribute("members", memberRepository.findAllMembers());
        return "member/list";
    }

    @GetMapping("/admin/members/{id}/update")
    public String updateForm(@PathVariable Long id, Model model) {
        Member member = memberService.findMemberById(id);
        model.addAttribute("id", id);
        model.addAttribute("dto", new MemberUpdateRequestDto(member.getName(), member.getEmail(), member.getPassword()));
        return "member/update";
    }

    @PostMapping("/admin/members/{id}/update")
    public String update(@PathVariable Long id, @ModelAttribute MemberUpdateRequestDto dto) {
        memberService.updateMember(id, dto);
        return "redirect:/admin/members";
    }
}
