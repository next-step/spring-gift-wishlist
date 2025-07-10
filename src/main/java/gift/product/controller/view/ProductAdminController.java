package gift.product.controller.view;

import gift.product.domain.Product;
import gift.product.dto.ProductPatchRequestDto;
import gift.product.dto.ProductSaveRequestDto;
import gift.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/api/admin")
public class ProductAdminController {
    private final ProductService productService;

    public ProductAdminController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/product/list")
    public String findAll(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/product/add")
    public String addForm(Model model) {
        model.addAttribute("requestDto", new ProductSaveRequestDto());
        return "addForm";
    }

    @PostMapping("/product/add")
    public String saveProduct(@Valid @ModelAttribute ProductSaveRequestDto productSaveRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "addForm";
        }
        productService.saveProduct(productSaveRequestDto);
        return "redirect:/api/admin/product/list";
    }

    @ResponseBody
    @GetMapping("/product/{id}")
    public Product findById(@PathVariable UUID id) {
        return productService.findById(id);
    }

    @GetMapping("/product/{id}/update")
    public String updateForm(@PathVariable UUID id, Model model) {
        Product product = productService.findById(id);
        ProductPatchRequestDto productPatchRequestDto = new ProductPatchRequestDto(product);
        model.addAttribute("productPatchRequestDto", productPatchRequestDto);
        return "updateForm";
    }

    @PatchMapping("/product/{id}/update")
    public String updateProduct(@PathVariable UUID id, @Valid @ModelAttribute ProductPatchRequestDto productPatchRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "updateForm";
        }
        productService.updateProduct(id, productPatchRequestDto);
        return "redirect:/api/admin/product/list";
    }

    @DeleteMapping("/product/{id}/delete")
    public String deleteById(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return "redirect:/api/admin/product/list";
    }
}
