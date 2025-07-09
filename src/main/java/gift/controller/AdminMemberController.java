package gift.controller;

import gift.dto.member.MemberRequestDto;
import gift.dto.member.MemberResponseDto;
import gift.entity.Member;
import gift.service.member.MemberService;
import jakarta.validation.Valid;
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

@Controller
@RequestMapping("/admin/members")
public class AdminMemberController {

  private static final String MEMBERS_LIST_PAGE_PATH = "/admin/members";

  private final MemberService service;

  public AdminMemberController(MemberService service) {
    this.service = service;
  }

  @GetMapping
  public String findAllMembers(Model model) {
    List<MemberResponseDto> memberList = service.findAllMember();
    model.addAttribute("memberList", memberList);
    return "memberList";
  }

  @GetMapping("/new")
  public String createMemberForm(Model model) {
    model.addAttribute("member", new Member());
    return "createMemberForm";
  }

  @PostMapping("/new")
  public String createMember(@Valid @ModelAttribute MemberRequestDto requestDto) {
    service.createMember(requestDto);
    return "redirect:" + MEMBERS_LIST_PAGE_PATH;
  }

  @GetMapping("/{id}/update")
  public String updateMemberForm(@PathVariable("id") Long id, Model model) {
    MemberResponseDto memberById = service.findMemberById(id);
    model.addAttribute("member", memberById);
    return "updateMemberForm";
  }

  @PutMapping("/{id}/update")
  public String update(@PathVariable("id") Long id,
      @Valid @ModelAttribute MemberRequestDto requestDto) {
    service.updateMember(id, requestDto);
    return "redirect:" + MEMBERS_LIST_PAGE_PATH;
  }

  @DeleteMapping("/{id}/delete")
  public String delete(@PathVariable("id") Long id) {
    service.deleteMember(id);
    return "redirect:" + MEMBERS_LIST_PAGE_PATH;
  }
}
