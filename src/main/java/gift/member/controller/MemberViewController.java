package gift.member.controller;

import gift.member.dto.MemberRequest;
import gift.member.dto.MemberResponse;
import gift.member.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/members")
public class MemberViewController {

    private final MemberService memberService;

    public MemberViewController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public String showMemberList(Model model) {
        List<MemberResponse> members = memberService.findAllMembers();
        model.addAttribute("members", members);
        return "admin/memberList";
    }

    @GetMapping("/add")
    public String showMemberAddForm(Model model) {
        model.addAttribute("memberRequest", new MemberRequest("", "",""));
        return "admin/memberAdd";
    }

    @PostMapping("/add")
    public String addMember(@ModelAttribute MemberRequest memberRequest) {
        memberService.register(memberRequest);
        return "redirect:/admin/members";
    }

    @GetMapping("/enter")
    public String showMemberLoginForm(Model model) {
        model.addAttribute("memberRequest", new MemberRequest("", "",""));
        return "admin/memberLogin";
    }

    @PostMapping("/enter")
    public String checkMember(@ModelAttribute MemberRequest memberRequest) {
        memberService.login(memberRequest);
        return "redirect:/admin/members";
    }

    @GetMapping("/edit/{id}")
    public String showMemberEditForm(@PathVariable("id") Long id, Model model) {
        MemberResponse member = memberService.findMemberById(id);
        model.addAttribute("memberRequest", new MemberRequest(member.email(), "",""));
        model.addAttribute("memberId", id);
        return "admin/memberEdit";
    }

    @PostMapping("/edit/{id}")
    public String editMember(@PathVariable("id") Long id, @ModelAttribute MemberRequest memberRequest) {
        memberService.updateMember(id, memberRequest);
        return "redirect:/admin/members";
    }

    @PostMapping("/delete/{id}")
    public String deleteMember(@PathVariable("id") Long id) {
        memberService.deleteMember(id);
        return "redirect:/admin/members";
    }
}