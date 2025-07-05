package gift.controller;

import gift.entity.Member;
import gift.dto.MemberRequestDto;
import gift.exception.MemberNotFoundException;
import gift.service.MemberService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class MemberViewController {

    private final MemberService memberService;

    public MemberViewController(MemberService memberService){
        this.memberService = memberService;
    }

    //모든 회원을 조회
    @GetMapping("/members/list")
    public String getMembers(Model model){
        List<Member> memberList = memberService.getAllMembers();
        model.addAttribute("memberList", memberList);
        return "members/home";
    }

    //특정 회원 조회(이메일로 검색)
    @GetMapping("/members/info")
    public String getMembers(
            @RequestParam(required = false) String email,
            Model model
    ){
        if(email.isEmpty()){
            return "redirect:/admin/members/list";
        }
        Optional<Member> member = memberService.getMemberByEmail(email);
        if(member.isEmpty()){
            String errMsg = "email이 " + email +"인 회원은 존재하지 않습니다.";
            throw new MemberNotFoundException(errMsg);
        }
        model.addAttribute("member", member.get());
        return "members/memberinfo";
    }

    //회원 추가
    //1. 회원 등록 폼을 가져오기
    @GetMapping("/members/add")
    public String getMemeberForm(Model model){
        model.addAttribute("memberRequestDto", new MemberRequestDto(null, null));
        model.addAttribute("memberDto", new MemberRequestDto(null, null));
        return "members/form";
    }

    //2. 회원 등록(추가)
    @PostMapping("/members/add")
    public String addMember(
            @ModelAttribute @Valid MemberRequestDto memberRequestDto,
            BindingResult bindingResult,
            Model model
    ){
        if(memberService.getMemberByEmail(memberRequestDto.email()).isPresent()){
            bindingResult.addError(new FieldError("memberRequestDto", "email", "이미 사용중인 이메일 입니다."));
        }
        if(bindingResult.hasErrors()){
            model.addAttribute("memberDto", memberRequestDto); //입력값 유지를 위함
            return "members/form";
        }
        memberService.register(memberRequestDto);
        String email = memberService.getMemberByEmail(memberRequestDto.email()).get().getEmail();
        return "redirect:/admin/members/info?email=" + email;
    }

    //회원 수정
    //1. 수정 화면 가져오기
    @GetMapping("/members/modify/{id}")
    public String modifyMemberForm(
            @PathVariable Long id,
            Model model
    ){
        Member member = memberService.findMember(id).get();
        model.addAttribute("memberRequestDto", new MemberRequestDto(null, null));
        model.addAttribute("member", member);
        return "members/modifyForm";
    }

    @PostMapping("/members/modify/{id}")
    public String modifyMember(
            @ModelAttribute @Valid MemberRequestDto memberRequestDto,
            BindingResult bindingResult,
            @PathVariable Long id,
            Model model
    ){
        Member member = memberService.findMember(id).get();

        if(!memberService.checkAvailableModify(id, memberRequestDto)){
            bindingResult.addError(new FieldError("memberRequestDto", "email", "이미 사용중인 이메일 입니다."));
        }

        if(bindingResult.hasErrors()){
            model.addAttribute("member", member);
            return "members/modifyForm";
        }

        memberService.modifyMember(id, memberRequestDto);
        String email = memberService.getMemberByEmail(memberRequestDto.email()).get().getEmail();
        return "redirect:/admin/members/info?email=" + email;
    }

    //회원을 삭제
    @PostMapping("/members/remove/{id}")
    public String removeMember(@PathVariable Long id){
        memberService.removeMember(id);
        return "redirect:/admin/members/list";
    }

}
