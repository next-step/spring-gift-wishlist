package gift.controller;

import gift.dto.request.GiftCreateRequest;
import gift.dto.request.GiftModifyRequest;
import gift.dto.response.GiftResponse;
import gift.repository.GiftRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class GiftWebViewController {
    private final GiftRepository giftRepository;

    public GiftWebViewController(GiftRepository giftRepository) {
        this.giftRepository = giftRepository;
    }

    @GetMapping("/admin")
    public String adminWebView(Model model) {
        model.addAttribute("gifts", giftRepository.findAll());
        return "/admin.html";
    }

    @GetMapping("/addItem")
    public String addItemWebView(){
        return "/addGift.html";
    }

    @PostMapping("/addItem")
    public String addItem(@ModelAttribute GiftCreateRequest giftCreateRequest, RedirectAttributes redirectAttributes){
        GiftResponse giftResponse = GiftResponse.from(giftRepository.save(giftCreateRequest.toEntity()));
        redirectAttributes.addAttribute("giftId", giftResponse.id());
        return "redirect:/gift/{giftId}";
    }

    @GetMapping("/gift/{id}")
    public String giftWebView(Model model, @PathVariable Long id){
        model.addAttribute("gift", giftRepository.findById(id).orElse(null));
        return "/singleGift.html";
    }

    @GetMapping("/gift/{id}/edit")
    public String editWebView(Model model, @PathVariable Long id){
        model.addAttribute("gift", giftRepository.findById(id).orElse(null));
        return "/modify.html";
    }

    @PostMapping("gift/{id}/edit")
    public String edit(
            @PathVariable Long id,
            @ModelAttribute GiftModifyRequest item,
            RedirectAttributes redirectAttributes
    ) {
        giftRepository.modify(id, item.toEntity());
        redirectAttributes.addAttribute("giftId", id);
        return "redirect:/gift/{giftId}";
    }

    @PostMapping("/gift/{id}/delete")
    public String delete(@PathVariable Long id){
        giftRepository.deleteById(id);
        return "redirect:/admin";
    }
}
