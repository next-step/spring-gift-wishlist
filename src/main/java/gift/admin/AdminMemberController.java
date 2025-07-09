package gift.admin;

import gift.Entity.Member;
import gift.dto.MemberDto;
import gift.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/admin/members")
public class AdminMemberController {

    private final MemberDto memberDto;
    private final MemberService memberService;

    public AdminMemberController(MemberDto memberDto, MemberService memberService) {
        this.memberDto = memberDto;
        this.memberService = memberService;
    }

    @GetMapping
    public String list(Model model) {
        List<Member> members = memberDto.showallMembers();
        model.addAttribute("members", members);
        return "admin/memberlist";
    }

    @GetMapping("/add")
    public String createForm(Model model) {
        model.addAttribute("member", new Member());
        model.addAttribute("formType", "add");
        return "admin/memberform";
    }

    @PostMapping
    public String createMember(@ModelAttribute @Valid Member member,
                               BindingResult bindingResult,
                               Model model) {
        try {
            memberService.register(member);
        } catch (Exception e) {
            bindingResult.rejectValue("id", "duplicate.id", e.getMessage());
            model.addAttribute("formType", "add");
            return "admin/memberform";
        }

        memberDto.insertMember(member);
        return "redirect:/admin/members";
    }

    @GetMapping("/{id}/edit")
    public String editMember(@PathVariable String id, Model model) {
        Member member = memberDto.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디의 회원을 찾을 수 없습니다."));

        model.addAttribute("member", member);
        model.addAttribute("formType", "edit");
        return "admin/memberform";
    }

    // 상품 수정 처리
    @PostMapping("/{id}")
    public String updateMember(@PathVariable String id, @ModelAttribute @Valid Member member,
                               BindingResult bindingResult,
                               Model model) {
        try {
            memberDto.updateMember(id, member); // 따로 구현 필요
        } catch (Exception e) {
            bindingResult.reject("updateError", e.getMessage());
            model.addAttribute("formType", "edit");
            return "admin/memberform";
        }

        return "redirect:/admin/members";
    }

    // 상품 삭제 처리
    // 메소드 이름 중 첫 글자는 소문자로 시작하도록 통일
    @PostMapping("/{id}/delete")
    public String deleteMember(@PathVariable String id) {
        memberDto.deleteMember(id);
        return "redirect:/admin/members";
    }

}