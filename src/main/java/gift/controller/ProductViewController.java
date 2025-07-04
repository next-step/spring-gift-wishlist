package gift.controller;

import gift.dto.PageResponseDto;
import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductViewController {

    private final ProductService productService;

    public ProductViewController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/view")
    public String showProductsForm(Model model, @RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "5") int pageSize) {

        int totalProducts = productService.countAllProducts();
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
        if (page < 1) page = 1;
        if (page > totalPages) page = totalPages;

        PageResponseDto pageResponseDto = productService.getPageProducts(page, pageSize);

        model.addAttribute("products", pageResponseDto.getPageProducts());
        model.addAttribute("currentPage", pageResponseDto.getCurrentPage());
        model.addAttribute("totalPages", pageResponseDto.getTotalPages());
        model.addAttribute("pageSize", pageSize);

        return "view";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new ProductRequestDto());
        return "add";
    }

    @PostMapping("/add")
    public String addProduct(@Valid @ModelAttribute("product") ProductRequestDto requestDto,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("product", requestDto);
            return "add";
        }
        try {
            productService.addProduct(requestDto);
        } catch (IllegalArgumentException ex) {
            bindingResult.rejectValue("name", "NotApprovedUsingKakaoName", ex.getMessage());
            model.addAttribute("product", requestDto);
            return "add";
        }
        return "redirect:/products/add";
    }

    @GetMapping("/{id}")
    public String showProductForm(@PathVariable Long id, Model model) {
        return productService.findProductById(id)
                .map(product -> {
                    model.addAttribute("product", product);
                    return "detail";
                })
                .orElse("not-found");
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        return productService.findProductById(id)
                .map(product -> {
                    model.addAttribute("product", product);
                    model.addAttribute("id", id);
                    return "edit";
                })
                .orElse("not-found");
    }

    @PostMapping("/edit/{id}")
    public String editProduct(@PathVariable Long id, @Valid @ModelAttribute("product") ProductRequestDto requestDto,
                              BindingResult bindingResult,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("product", requestDto);
            model.addAttribute("id", id);
            return "edit";
        }
        try {
            productService.updateProduct(id, requestDto);
        } catch (IllegalArgumentException ex) {
            bindingResult.rejectValue("name", "NotApprovedUsingKakaoName", ex.getMessage());
            model.addAttribute("product", requestDto);
            return "edit";
        }
        return "redirect:/products/" + id;
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/products/view";
    }

}
