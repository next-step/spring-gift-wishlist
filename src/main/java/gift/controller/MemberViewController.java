package gift.controller;

import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;
import gift.dto.MemberUpdateDto;
import gift.dto.PageRequestDto;
import gift.dto.PageResult;
import gift.entity.Role;
import gift.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/members")
@RequiredArgsConstructor
public class MemberViewController {

    private final MemberService memberService;

    @GetMapping
    public String list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model
    ) {
        PageResult<MemberResponseDto> members = memberService.findAllMembers(new PageRequestDto(page, size));
        model.addAttribute("page", members);
        return "member/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("member", new MemberRequestDto("", "", "", Role.USER.name()));
        return "member/create";
    }

    @PostMapping
    public String create(@Valid MemberRequestDto memberRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "member/create";
        }
        memberService.register(memberRequestDto);
        return "redirect:/admin/members";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("member", memberService.findMemberById(id));
        return "member/detail";
    }

    @GetMapping("/{id}/update")
    public String updateForm(@PathVariable Long id, Model model) {
        MemberResponseDto memberResponseDto = memberService.findMemberById(id);
        MemberUpdateDto memberUpdateDto = new MemberUpdateDto(
                memberResponseDto.email(),
                "",
                memberResponseDto.role()
        );
        model.addAttribute("member", memberUpdateDto);
        model.addAttribute("memberId", id);
        return "member/update";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid MemberUpdateDto memberUpdateDto,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("member", memberUpdateDto);
            return "member/update";
        }
        memberService.updateMember(id, memberUpdateDto);
        return "redirect:/admin/members";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        memberService.deleteMember(id);
        return "redirect:/admin/members";
    }
}
