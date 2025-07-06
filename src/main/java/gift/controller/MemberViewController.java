package gift.controller;

import gift.dto.MemberRequestDto;
import gift.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping("/new")
    public ModelAndView createForm() {
        Map<String, Object> model = new HashMap<>();
        model.put("member", new MemberRequestDto(null, null, null, null));
        return new ModelAndView("member/create", model);
    }

    @PostMapping
    public ModelAndView create(
            @Valid @ModelAttribute("member") MemberRequestDto requestDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            ModelAndView mav = new ModelAndView("member/create");
            mav.addObject("member", requestDto);
            return mav;
        }

        memberService.create(requestDto);
        return new ModelAndView("redirect:/members");
    }

}
