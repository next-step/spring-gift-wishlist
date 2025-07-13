package gift.product.controller;

import gift.product.dto.ProductInfoDto;
import gift.product.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String products(Model model) {
        List<ProductInfoDto> products = productService.getProducts()
                .stream()
                .map(product -> ProductInfoDto.productFrom(product))
                .toList();

        model.addAttribute("products", products);

        return "products/products";
    }
}
