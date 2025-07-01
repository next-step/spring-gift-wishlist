package gift.controller;

import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.entity.Product;
import gift.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/view")
public class ProductViewController {

    private final ProductService productService;

    public ProductViewController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public String showProducts(Model model) {
        List<ProductResponseDto> productList = productService.findAll();

        model.addAttribute("productList", productList);
        model.addAttribute("requestDto", new ProductRequestDto("", 0, ""));
        model.addAttribute("updateDto", new ProductRequestDto("", 0, ""));
        return "home";
    }

    @PostMapping("/products")
    public String createProduct(@ModelAttribute("requestDto") @Valid ProductRequestDto requestDto,
                                BindingResult bindingResult,
                                Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("openCreateModal", true);
            model.addAttribute("requestDto", requestDto);
            model.addAttribute("productList", productService.findAll());
            return "home";
        }
        productService.create(new ProductRequestDto(requestDto.name(), requestDto.price(), requestDto.imageUrl()));

        return "redirect:/view/products";
    }

    @PostMapping("/products/update/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute("requestDto") ProductRequestDto requestDto) {
        productService.update(id, new ProductRequestDto(requestDto.name(), requestDto.price(), requestDto.imageUrl()));

        return "redirect:/view/products";
    }

    @DeleteMapping("/products/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.delete(id);

        return "home";
    }

}
