package gift.controller;

import gift.dto.ProductRequestDTO;
import gift.dto.ProductResponseDTO;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductViewController {
    private final ProductService productService;
    public ProductViewController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String getAllProducts(Model model) {
        List<ProductResponseDTO> products = productService.getAll();
        model.addAttribute("products", products);
        return "products";
    }

    @PostMapping("/create")
    public String createProduct(@Valid @ModelAttribute ProductRequestDTO product) {
        productService.create(product);
        return "redirect:/products";
    }

    @PostMapping("/update")
    public String updateProduct(
            @RequestParam("id") Integer id,
            @Valid @ModelAttribute ProductRequestDTO product) {
        productService.update(id, product);
        return "redirect:/products";
    }

    @PostMapping("/delete")
    public String deleteProduct(@RequestParam("id") Integer id) {
        productService.delete(id);
        return "redirect:/products";
    }
}
