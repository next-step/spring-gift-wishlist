package gift.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import gift.dto.AdminMemberResponse;
import gift.dto.RegisterRequest;
import gift.dto.UpdateMemberRequest;
import gift.service.MemberService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/members")
public class MemberAdminController {

    private final MemberService memberService;

    public MemberAdminController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public String adminPage(Model model) {
        List<AdminMemberResponse> members = memberService.findAll();
        model.addAttribute("members", members);
        return "members";
    }

    @GetMapping("/add")
    public String addPage() {
        return "addMember";
    }

    @PostMapping
    public String addUser(
        @Valid @ModelAttribute RegisterRequest request
    ) {
        memberService.signup(request);
        return "redirect:/admin/members";
    }

    @GetMapping("/{id}/edit")
    public String editPage(
        @PathVariable Long id,
        Model model
    ) {
        AdminMemberResponse member = memberService.findById(id);
        model.addAttribute("member", member);
        return "editMember";
    }

    @PutMapping("/{id}")
    public String updateUser(
        @PathVariable Long id,
        @Valid @ModelAttribute UpdateMemberRequest request
    ) {
        memberService.update(id, request);
        return "redirect:/admin/members";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(
        @PathVariable Long id
    ) {
        memberService.delete(id);
        return "redirect:/admin/members";
    }
}
