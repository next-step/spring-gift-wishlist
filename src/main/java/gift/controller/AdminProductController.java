package gift.controller;

import gift.dto.ProductAddRequestDto;
import gift.dto.ProductResponseDto;
import gift.dto.ProductUpdateRequestDto;
import gift.entity.Product;
import gift.exception.InvalidProductException;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String findAllProduct(Model model) {
        List<ProductResponseDto> products = productService.findAllProduct();
        model.addAttribute("products", products);
        return "admin/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new ProductAddRequestDto());
        return "admin/add";
    }

    @PostMapping("/add")
    public String addProduct(@Valid @ModelAttribute("product") ProductAddRequestDto requestDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "admin/add";
        }
        try {
            productService.addProduct(requestDto);
        } catch (InvalidProductException e) {
            model.addAttribute("globalErrorMessage", e.getMessage());
            return "admin/add";
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(
            Model model,
            @PathVariable Long id
    ) {
        ProductResponseDto product = productService.findProductById(id);
        model.addAttribute("product", product);
        return "admin/edit";
    }

    @PutMapping("/edit/{id}")
    public String editProduct(
            @PathVariable Long id,
            @Valid @ModelAttribute ProductUpdateRequestDto requestDto,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            ProductResponseDto product = new ProductResponseDto(id, requestDto.name(), requestDto.price(), requestDto.url());
            model.addAttribute("product", product);
            model.addAttribute("errorMessage", bindingResult.getFieldError("name").getDefaultMessage());
            return "admin/edit";
        }
        try {
            productService.updateProductById(id, requestDto);
        } catch (InvalidProductException e) {
            ProductResponseDto product = new ProductResponseDto(id, requestDto.name(), requestDto.price(), requestDto.url());
            model.addAttribute("product", product);
            model.addAttribute("globalErrorMessage", e.getMessage());
            return "admin/edit";
        }
        return "redirect:/admin/products";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return "redirect:/admin/products";
    }
}
