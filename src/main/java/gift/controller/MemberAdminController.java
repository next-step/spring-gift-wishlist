package gift.controller;

import gift.dto.MemberInfoResponseDto;
import gift.dto.MemberLoginRequestDto;
import gift.service.MemberService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/members")
public class MemberAdminController {

  private final MemberService memberService;

  public MemberAdminController(MemberService memberService) {
    this.memberService = memberService;
  }

  @GetMapping
  public String memberList(Model model) {
    List<MemberInfoResponseDto> members = memberService.searchAllMembers();
    model.addAttribute("members", members);
    return "admin/member-list";
  }

  @GetMapping("/new")
  public String showCreateForm() {
    return "admin/member-create-form";
  }

  @PostMapping
  public String create(@ModelAttribute @RequestBody @Valid MemberLoginRequestDto memberLoginRequestDto) {
    memberService.register(memberLoginRequestDto);
    return "redirect:/admin/members";
  }

  @GetMapping("/{id}/edit")
  public String showEditForm(@PathVariable Long id, Model model) {
    model.addAttribute("id", id);
    return "admin/member-update";
  }

  @PostMapping("/{id}")
  public String update(@PathVariable Long id, @ModelAttribute @RequestBody @Valid MemberLoginRequestDto memberLoginRequestDto) {
    memberService.updateMember(id, memberLoginRequestDto);
    return "redirect:/admin/members";
  }

  @PostMapping("/{id}/delete")
  public String delete(@PathVariable Long id) {
    memberService.deleteMember(id);
    return "redirect:/admin/members";
  }
}
