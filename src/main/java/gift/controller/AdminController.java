package gift.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import gift.dto.CreateProductRequest;
import gift.dto.ProductResponse;
import gift.dto.UpdateProductRequest;
import gift.service.ProductService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/products")
public class AdminController {

    private final ProductService productService;

    public AdminController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String adminPage(Model model) {
        List<ProductResponse> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "admin";
    }

    @GetMapping("/add")
    public String addPage() {
        return "addProduct";
    }

    @PostMapping
    public String addProduct(
        @Valid @ModelAttribute CreateProductRequest request
    ) {
        productService.createProduct(request);
        return "redirect:/admin/products";
    }

    @GetMapping("/{id}/edit")
    public String editPage(
        @PathVariable Long id,
        Model model
    ) {
        ProductResponse product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "editProduct";
    }

    @PutMapping("/{id}")
    public String updateProduct(
        @PathVariable Long id,
        @Valid @ModelAttribute UpdateProductRequest request
    ) {
        productService.updateProduct(id, request);
        return "redirect:/admin/products";
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(
        @PathVariable Long id
    ) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }
}
