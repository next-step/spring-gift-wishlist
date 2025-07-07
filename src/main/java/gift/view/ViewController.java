package gift.view;

import gift.global.annotation.Admin;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Controller
public class ViewController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @Admin
    @GetMapping("/admin/products")
    public String adminProducts(){
        return "admin-product";
    }

    @Admin
    @GetMapping("/admin/members")
    public String adminMembers(){
        return "admin-member";
    }

    @Admin
    @GetMapping("/admin/products/{id}")
    public String editProduct(@PathVariable UUID id, Model model){

        model.addAttribute("productId", id);

        return "edit-product";
    }

    @Admin
    @GetMapping("/admin/members/add")
    public String addMember(){
        return "add-member";
    }

    @Admin
    @GetMapping("/admin/members/{id}")
    public String editMember(@PathVariable UUID id, Model model){
        model.addAttribute("memberId", id);

        return  "edit-member";
    }

}
