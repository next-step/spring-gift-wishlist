package gift.controller.admin;

import gift.dto.member.MemberForm;
import gift.entity.member.Member;
import gift.exception.MemberNotFoundException;
import gift.service.member.MemberService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/members")
public class AdminMemberController {

    private final MemberService memberService;

    public AdminMemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    private String extractRole(HttpServletRequest req) {
        Claims claims = (Claims) req.getAttribute("authClaims");
        if (claims == null) {
            throw new IllegalStateException("인증 정보가 없습니다.");
        }
        return claims.get("role", String.class);
    }

    /**
     * 회원 목록
     */
    @GetMapping
    public String list(HttpServletRequest req, Model model) {
        String role = extractRole(req);
        List<Member> members = memberService.getAllMembers(role);
        model.addAttribute("members", members);
        return "admin/member_list";
    }

    /**
     * 신규 회원 등록 폼
     */
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm(null, "", "", "USER"));
        return "admin/member_form";
    }

    @PostMapping("/new")
    public String create(
            @Valid @ModelAttribute("memberForm") MemberForm form,
            BindingResult br,
            RedirectAttributes ra,
            HttpServletRequest req
    ) {
        if (br.hasErrors()) {
            return "admin/member_form";
        }
        String role = extractRole(req);
        Member created = memberService.createMember(
                form.email(), form.password(), form.role(), role
        );
        ra.addFlashAttribute("info", "회원이 등록되었습니다: " + created.getEmail());
        return "redirect:/admin/members";
    }

    /**
     * 회원 수정 폼
     */
    @GetMapping("/{id}/edit")
    public String editForm(
            @PathVariable Long id,
            Model model,
            HttpServletRequest req
    ) {
        String role = extractRole(req);
        Member m = memberService.getMemberById(id, role)
                .orElseThrow(() -> new MemberNotFoundException(id.toString()));
        model.addAttribute("memberForm", new MemberForm(
                m.getId().id(), m.getEmail().email(), m.getPassword().password(), m.getRole().name()
        ));
        return "admin/member_form";
    }

    /**
     * 회원 수정 처리
     */
    @PutMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("memberForm") MemberForm form,
            BindingResult br,
            RedirectAttributes ra,
            HttpServletRequest req
    ) {
        if (br.hasErrors()) {
            return "admin/member_form";
        }
        String role = extractRole(req);
        Member updated = memberService.updateMember(
                id, form.email(), form.password(), form.role(), role
        );
        ra.addFlashAttribute("info", "회원 정보가 수정되었습니다: " + updated.getEmail());
        return "redirect:/admin/members";
    }

    /**
     * 회원 삭제 처리
     */
    @DeleteMapping("/{id}")
    public String delete(
            @PathVariable Long id,
            RedirectAttributes ra,
            HttpServletRequest req
    ) {
        String role = extractRole(req);
        memberService.deleteMember(id, role);
        ra.addFlashAttribute("info", "회원이 삭제되었습니다. ID=" + id);
        return "redirect:/admin/members";
    }
}
