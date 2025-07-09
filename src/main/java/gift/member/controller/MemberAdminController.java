package gift.member.controller;

import gift.member.dto.MemberLoginRequestDto;
import gift.member.service.MemberService;
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
        model.addAttribute("members", memberService.findAll());
        return "/members/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("member", MemberLoginRequestDto.from());
        return "/members/create_form";
    }

    @GetMapping("/{id}/edit")
    public String updateForm(@PathVariable Long id, Model model) {
        model.addAttribute("member", memberService.findById(id));
        return "/members/update_form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute MemberLoginRequestDto requestDto) {
        memberService.register(requestDto);
        return "redirect:/admin/members";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute MemberLoginRequestDto requestDto) {
        memberService.update(id, requestDto);
        return "redirect:/admin/members";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        memberService.delete(id);
        return "redirect:/admin/members";
    }
}
