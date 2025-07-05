package gift.controller;

import gift.dto.Member;
import gift.dto.MemberRequestDto;
import gift.exception.MemberNotFoundException;
import gift.service.MemberService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    //회원을 조회
    @GetMapping("/members/list")
    public String getMembers(Model model){
        List<Member> memberList = memberService.getAllMembers();
        model.addAttribute("memberList", memberList);
        return "members/home";
    }

    //특정 회원 조회
    @GetMapping("/members/info")
    public String getMebers(
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
    public String getMemeberForm(){
        return "members/form";
    }

    //2. 회원 등록(추가)
    @PostMapping("/members/add")
    public String addMember(@ModelAttribute MemberRequestDto memberRequestDto){
        memberService.register(memberRequestDto);
        return "redirect:/admin/members/list";
    }

    //회원 수정
    //1. 수정 화면 가져오기
    @GetMapping("/members/modify/{id}")
    public String modifyMemberForm(
            @PathVariable Long id,
            Model model
    ){
        Member member = memberService.findMember(id).get();
        model.addAttribute("member", member);
        return "members/modifyForm";
    }

    @PostMapping("/members/modify/{id}")
    public String modifyMember(
            @ModelAttribute MemberRequestDto memberRequestDto,
            @PathVariable Long id
    ){
        memberService.modifyMember(id, memberRequestDto);
        return "redirect:/admin/members/list";
    }

    //회원을 삭제
    @PostMapping("/members/remove/{id}")
    public String removeMember(@PathVariable Long id){
        memberService.removeMember(id);
        return "redirect:/admin/members/list";
    }

}
