package gift.controller;

import gift.dto.MemberRequest;
import gift.dto.MemberResponse;
import gift.dto.ProductRequest;
import gift.dto.ProductResponse;
import gift.service.MemberService;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductService productService;
    private final MemberService memberService;

    public AdminController(ProductService productService, MemberService memberService) {
        this.productService = productService;
        this.memberService = memberService;
    }

    // --- 상품 관리 섹션 ---

    @GetMapping("/products")
    public String showProductList(Model model) {
        List<ProductResponse> products = productService.findAllProducts();
        model.addAttribute("products", products);
        model.addAttribute("productRequest", new ProductRequest("", 0, ""));
        return "admin/product-list";
    }

    @GetMapping("/products/add")
    public String showAddForm(Model model) {
        model.addAttribute("productRequest", new ProductRequest("", 0, ""));
        return "admin/product-form";
    }

    @PostMapping("/products/add")
    public String addProduct(@Valid @ModelAttribute("productRequest") ProductRequest productRequest,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<ProductResponse> products = productService.findAllProducts();
            model.addAttribute("products", products);
            return "admin/product-list";
        }
        productService.addProduct(productRequest);
        return "redirect:/admin/products";
    }

    @GetMapping("/products/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        ProductResponse product = productService.findProductById(id);
        model.addAttribute("productRequest", new ProductRequest(product.name(), product.price(), product.imageUrl()));
        model.addAttribute("productId", id);
        return "admin/product-edit-form";
    }

    @PostMapping("/products/edit/{id}")
    public String editProduct(@PathVariable("id") Long id,
                              @Valid @ModelAttribute("productRequest") ProductRequest productRequest,
                              BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("productId", id);
            return "admin/product-edit-form";
        }
        productService.updateProduct(id, productRequest);
        return "redirect:/admin/products";
    }

    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }

    @PostMapping("/products/delete")
    public String deleteSelectedProducts(@RequestParam(value = "productIds", required = false) List<Long> productIds) {
        if (productIds != null && !productIds.isEmpty()) {
            productService.deleteProducts(productIds);
        }
        return "redirect:/admin/products";
    }


    // --- 회원 관리 섹션 ---

    @GetMapping("/members")
    public String showMemberList(Model model) {
        List<MemberResponse> members = memberService.findAllMembers();
        model.addAttribute("members", members);
        return "admin/member-list";
    }

    @GetMapping("/members/add")
    public String showMemberAddForm(Model model) {
        model.addAttribute("memberRequest", new MemberRequest("", ""));
        return "admin/member-form";
    }

    @PostMapping("/members/add")
    public String addMember(@ModelAttribute MemberRequest memberRequest) {
        memberService.register(memberRequest);
        return "redirect:/admin/members";
    }

    @GetMapping("/members/edit/{id}")
    public String showMemberEditForm(@PathVariable("id") Long id, Model model) {
        MemberResponse member = memberService.findMemberById(id);
        model.addAttribute("memberRequest", new MemberRequest(member.email(), ""));
        model.addAttribute("memberId", id);
        return "admin/member-edit-form";
    }

    @PostMapping("/members/edit/{id}")
    public String editMember(@PathVariable("id") Long id, @ModelAttribute MemberRequest memberRequest) {
        memberService.updateMember(id, memberRequest);
        return "redirect:/admin/members";
    }

    @PostMapping("/members/delete/{id}")
    public String deleteMember(@PathVariable("id") Long id) {
        memberService.deleteMember(id);
        return "redirect:/admin/members";
    }
}