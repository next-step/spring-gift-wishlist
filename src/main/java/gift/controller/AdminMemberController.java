package gift.controller;

import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;
import gift.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/members")
public class AdminMemberController {

    private final MemberService memberService;

    public AdminMemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 전체 회원 조회
    @GetMapping
    public ResponseEntity<List<MemberResponseDto>> getAllMembers() {
        List<MemberResponseDto> response = memberService.getAllMembers();
        return ResponseEntity.ok(response);
    }

    // 회원 조회 (ID로)
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponseDto> getMember(@PathVariable Long id) {
        MemberResponseDto response = memberService.getMember(id);
        return ResponseEntity.ok(response);
    }

    // 회원 추가 (관리자가 직접 추가)
    @PostMapping
    public ResponseEntity<MemberResponseDto> createMember(@RequestBody MemberRequestDto requestDto) {
        // 추후 관리자 인증 로직 추가할 것 같음 ...
        memberService.register(requestDto);
        MemberResponseDto response = memberService.getMemberByEmail(requestDto.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 회원 수정
    @PutMapping("/{id}")
    public ResponseEntity<MemberResponseDto> updateMember(@PathVariable Long id, 
                                                       @RequestBody MemberRequestDto requestDto) {
        MemberResponseDto response = memberService.updateMember(id, requestDto);
        return ResponseEntity.ok(response);
    }

    // 회원 삭제
    @DeleteMapping("/{id}")
    public String deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return "redirect:/admin/members/list";
    }

    // 관리자 회원 목록 화면 (HTML)
    @GetMapping("/list")
    public String memberListPage(Model model) {
        model.addAttribute("members", memberService.getAllMembers());
        return "admin/member-list";
    }

    // 회원 등록 폼
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("form", new MemberRequestDto());
        return "admin/member-form";
    }

    // 회원 등록 처리
    @PostMapping("/new")
    public String createMemberForm(@ModelAttribute("form") MemberRequestDto requestDto,
                                   org.springframework.validation.BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/member-form";
        }
        memberService.register(requestDto);
        return "redirect:/admin/members/list";
    }

    // 회원 수정 폼
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        MemberResponseDto responseDto = memberService.getMember(id);
        MemberRequestDto requestDto = new MemberRequestDto();
        requestDto.setEmail(responseDto.getEmail());
        // 비밀번호는 보안상 빈 값으로 둠
        model.addAttribute("form", requestDto);
        model.addAttribute("memberId", id);
        return "admin/member-form";
    }

    // 회원 수정 처리
    @PostMapping("/{id}/edit")
    public String updateMemberForm(@PathVariable Long id,
                                   @ModelAttribute("form") MemberRequestDto requestDto,
                                   org.springframework.validation.BindingResult bindingResult,
                                   Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("memberId", id);
            return "admin/member-form";
        }
        memberService.updateMember(id, requestDto);
        return "redirect:/admin/members/list";
    }

    // 폼에서 회원 삭제 요청을 받을 때
    @PostMapping("/{id}")
    public String deleteMemberForm(@PathVariable Long id) {
        memberService.deleteMember(id);
        return "redirect:/admin/members/list";
    }
} 