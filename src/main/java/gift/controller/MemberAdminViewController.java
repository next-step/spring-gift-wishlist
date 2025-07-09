package gift.controller;

import gift.dto.MemberResponseDto;
import gift.dto.MemberUpdateRequestDto;
import gift.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/admin/members")
public class MemberAdminViewController {

    private final MemberService memberService;
    public MemberAdminViewController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("members", memberService.findAllMembers());
        return "member/list";
    }

    @GetMapping("/{id}/update")
    public String updateForm(@PathVariable Long id, Model model) {
        MemberResponseDto m = memberService.findMemberById(id);
        model.addAttribute("id", id);
        model.addAttribute("dto", new MemberUpdateRequestDto(m.name(), m.email(), ""));
        return "member/update";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id, @ModelAttribute MemberUpdateRequestDto dto) {
        memberService.updateMember(id, dto);
        return "redirect:/admin/members";
    }

    @PostMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }
}
