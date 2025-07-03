package gift.controller;

import gift.dto.ProductRequest;
import gift.entity.Product;
import gift.exception.ResourceNotFoundException;
import gift.service.ProductService;
import gift.validation.ValidationGroups;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    private final ProductService productService;

    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "/admin/product_list";
    }


    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("product", new Product(null, "", 0, ""));
        return "/admin/product_form";
    }


    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") Long id, Model model) {
        Product p = productService.getProductById(id)
                .orElseThrow(() -> new ResourceNotFoundException("상품을 찾을 수 없습니다: " + id));
        model.addAttribute("product", p);
        return "/admin/product_form";
    }

    @PostMapping
    public String create(
            @Validated(ValidationGroups.PermittedGroup.class) @ModelAttribute("product") ProductRequest req) {
        Product toSave = Product.createPermittedProduct(null, req.name(), req.price(),
                req.imageUrl());
        productService.createProduct(toSave,
                (p, name) -> Product.createPermittedProduct(null, name, p.price(), p.imageUrl()));
        return "redirect:/admin/products";
    }


    @PutMapping("/{id}")
    public String update(@PathVariable("id") Long id,
            @Validated(ValidationGroups.PermittedGroup.class) @ModelAttribute("product") ProductRequest req) {
        Product toUpdate = Product.createPermittedProduct(id, req.name(), req.price(),
                req.imageUrl());
        productService.updateProduct(id, toUpdate, Product::withPermittedName);
        return "redirect:/admin/products";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }
}
