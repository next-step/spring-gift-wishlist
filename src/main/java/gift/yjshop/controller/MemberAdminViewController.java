package gift.yjshop.controller;

import gift.dto.MemberRequestDto;
import gift.entity.Member;
import gift.exception.ErrorCode;
import gift.exception.MyException;
import gift.service.MemberService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/view/admin")
public class MemberAdminViewController {
    private final MemberService memberService;

    public MemberAdminViewController(MemberService memberService) {
        this.memberService = memberService;
    }

    //모든 회원을 조회
    @GetMapping("/members")
    public String getMembers(Model model){
        List<Member> memberList = memberService.getAllMembers();
        model.addAttribute("memberList", memberList);
        return "/yjshop/admin/member/home";
    }

    //특정 회원 조회(이메일로 검색)
    @GetMapping("/members/search")
    public String getMembers(
            @RequestParam(required = false) String email,
            Model model
    ){
        if(email.isEmpty()){
            return "redirect:/view/admin/members";
        }
        Optional<Member> member = memberService.getMemberByEmail(email);
        if(member.isEmpty()){
            throw new MyException(ErrorCode.MEMBER_NOT_FOUND);
        }
        model.addAttribute("member", member.get());
        return "/yjshop/admin/member/memberinfo";
    }

    //회원 등록 폼 가져오기
    @GetMapping("/members/add")
    public String getMemeberForm(Model model){
        model.addAttribute("memberRequestDto", new MemberRequestDto(null, null));
        model.addAttribute("memberDto", new MemberRequestDto(null, null));
        return "/yjshop/admin/member/form";
    }

    //회원 등록
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
            return "yjshop/admin/member/form";
        }
        memberService.register(memberRequestDto);
        String email = memberService.getMemberByEmail(memberRequestDto.email()).get().getEmail();
        return "redirect:/view/admin/members/search?email=" + email;
    }

    //회원 수정 폼 가져오기
    @GetMapping("/members/modify/{id}")
    public String modifyMemberForm(
            @PathVariable Long id,
            Model model
    ){
        Member member = memberService.findMember(id).get();
        model.addAttribute("memberRequestDto", new MemberRequestDto(null, null));
        model.addAttribute("member", member);
        return "/yjshop/admin/member/modifyForm";
    }

    //회원 수정
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
            return "/yjshop/admin/member/modifyForm";
        }

        memberService.modifyMember(id, memberRequestDto);
        String email = memberService.getMemberByEmail(memberRequestDto.email()).get().getEmail();
        return "redirect:/view/admin/members/search?email=" + email;
    }

    //회원을 삭제
    @PostMapping("/members/remove/{id}")
    public String removeMember(@PathVariable Long id){
        memberService.removeMember(id);
        return "redirect:/view/admin/members";
    }

    @ExceptionHandler(MyException.class)
    public String MyExceptionHandler(MyException e, Model model){
        model.addAttribute("errorMsg", e.getErrorCode().getMessage());

        if(e.getErrorCode().equals(ErrorCode.JWT_VALIDATION_FAIL)){
            return "redirect:/view/login";
        }

        return "/yjshop/admin/member/membernotfound";
    }




}