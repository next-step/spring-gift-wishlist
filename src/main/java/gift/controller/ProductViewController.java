package gift.controller;

import gift.entity.Product;
import gift.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin/products")
public class ProductViewController {

    private final ProductService productService;

    ProductViewController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin/product_list";
    }

    @GetMapping("/post")
    public String postProductForm() {
        return "admin/product_post";
    }

    @PostMapping
    public String postProduct(@ModelAttribute Product productWithoutId) {
        productService.createProduct(productWithoutId);
        return "redirect:/admin/products";
    }

    @GetMapping("/update/{id}")
    public String updateProductForm(@PathVariable Long id, Model model) {
        Optional<Product> product = productService.getProductById(id);

        if (product.isEmpty()) {
            return "redirect:/admin/products";
        }

        model.addAttribute("product", product.get());
        return "admin/product_update";
    }

    @PostMapping("/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute Product updateRequest) {
        productService.updateProduct(id, updateRequest);
        return "redirect:/admin/products";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id)
    {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }
}
