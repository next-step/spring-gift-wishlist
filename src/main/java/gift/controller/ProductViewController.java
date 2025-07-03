package gift.controller;

import gift.dto.request.ProductCreateRequestDto;
import gift.dto.request.ProductUpdateRequestDto;
import gift.dto.response.ProductGetResponseDto;
import gift.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/products")
public class ProductViewController {

    private final ProductService productService;

    public ProductViewController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/create")
    public String createProductPage() {
        return "create-product";
    }

    @PostMapping("/create")
    public String createProduct(
        @Valid @ModelAttribute ProductCreateRequestDto productCreateRequestDto,
        BindingResult bindingResult, Model model
    ) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "create-product";
        }

        try {
            productService.saveProduct(productCreateRequestDto);
            return "redirect:/admin/products";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "create-product";
        }
    }

    @GetMapping
    public String getProductsPage(Model model) {

        List<ProductGetResponseDto> products = productService.findAllProducts();
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/{productId}")
    public String getProductById(@RequestParam Long productId, Model model) {

        try {
            ProductGetResponseDto product = productService.findProductById(productId);
            model.addAttribute("products", List.of(product));
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        return "products";
    }

    @GetMapping("/update/{productId}")
    public String updateProductPage(@PathVariable Long productId, Model model) {

        try {
            ProductGetResponseDto product = productService.findProductById(productId);
            model.addAttribute("product", product);
            return "update-product";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/products";
        }
    }

    @PostMapping("/update/{productId}")
    public String updateProductById(
        @PathVariable Long productId,
        @Valid @ModelAttribute ProductUpdateRequestDto productUpdateRequestDto,
        BindingResult bindingResult, RedirectAttributes redirectAttributes
    ) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("productUpdateRequestDto",
                productUpdateRequestDto);
            return "redirect:/admin/products/update/" + productId;
        }

        try {
            productService.updateProductById(productId, productUpdateRequestDto);
            return "redirect:/admin/products";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

            return "redirect:/admin/products/update/" + productId;
        }
    }

    @PostMapping("/delete/{productId}")
    public String deleteProductById(@PathVariable Long productId, Model model) {

        try {
            productService.deleteProductById(productId);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/products";
    }
}