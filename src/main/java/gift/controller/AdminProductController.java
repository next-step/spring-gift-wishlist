package gift.controller;

import gift.domain.Product;
import gift.dto.ProductRequest;
import gift.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    private final ProductService productService;

    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String list(Model model) {
        List<Product> products = productService.getAll();
        model.addAttribute("products", products);
        return "admin/product-list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Product product = productService.getById(id);
        model.addAttribute("product", product);
        return "admin/product-detail";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("productRequest", new ProductRequest());
        return "admin/product-form";
    }

    @PostMapping
    public String create(@ModelAttribute ProductRequest productRequest) {
        productService.create(productRequest.toEntity());
        return "redirect:/admin/products";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable(name = "id") Long productId, Model model) {
        Product product = productService.getById(productId);
        ProductRequest dto = new ProductRequest(product.getName(), product.getPrice(), product.getImageUrl());
        model.addAttribute("productRequest", dto);
        model.addAttribute("productId", productId);
        return "admin/product-form";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id, @ModelAttribute ProductRequest request) {
        productService.update(id, request.toEntity());
        return "redirect:/admin/products";
    }


    @PostMapping("/{id}/delete")
    public String delete(@PathVariable(name = "id") Long productId) {
        productService.delete(productId);
        return "redirect:/admin/products";
    }

}
