package gift.controller;

import gift.dto.MemberLogInRequestDto;
import gift.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/members")
public class MemberAdminController {
    private final MemberService memberService;

    public MemberAdminController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("members", memberService.findAllMembers());
        return "/members/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("member", MemberLogInRequestDto.from());
        return "/members/createMemberForm";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("member", memberService.findMemberById(id));
        return "/members/updateMemberForm";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute MemberLogInRequestDto requestDto) {
        memberService.registerMember(requestDto);
        return "redirect:/admin/members";
    }

    @PutMapping("/{id}")
    public String edit(@PathVariable Long id, @Valid @ModelAttribute MemberLogInRequestDto requestDto) {
        memberService.updateMember(id, requestDto);
        return "redirect:/admin/members";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        memberService.deleteMember(id);
        return "redirect:/admin/members";
    }
}
