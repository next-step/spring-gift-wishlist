package gift.controller;

import gift.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/members")
public class MemberViewController {

    private final MemberService memberService;
    public MemberViewController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ModelAndView list() {
        Map<String, Object> model = new HashMap<>();
        model.put("members", memberService.findAll());
        return new ModelAndView("member/list", model);
    }

}
