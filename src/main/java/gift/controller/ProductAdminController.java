package gift.controller;


import gift.dto.request.ProductRequestDto;
import gift.dto.response.ProductResponseDto;
import gift.dto.view.ProductFormDto;
import gift.dto.view.ProductView;
import gift.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class ProductAdminController {
    private final ProductService productService;


    public ProductAdminController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String listProducts(Model model){
        List<ProductResponseDto> productDtos = productService.getProducts();

        List<ProductView> productViews = productDtos.stream()
                .map(ProductView::from)
                .toList();

        model.addAttribute("products", productViews);

        return "admin/products";
    }

    @GetMapping("/add")
    public String showAddForm(Model model){
        model.addAttribute("product",
                ProductFormDto.emptyForm());

        return "admin/add-form";
    }

    @PostMapping("/add")
    public String addProduct(@Valid @ModelAttribute("product") ProductRequestDto productRequestDto,
                             BindingResult bindingResult,
                             Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("product", productRequestDto);
            return "admin/add-form";
        }

        productService.addProduct(productRequestDto);

        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model){
        ProductResponseDto productDto = productService.getProduct(id);

        ProductFormDto productFormDto = ProductFormDto.from(productDto);

        model.addAttribute("product", productFormDto);
        model.addAttribute("productId", id);

        return "admin/edit-form";
    }

    @PutMapping("/edit/{id}")
    public String updateProduct(@PathVariable Long id,
                                @Valid @ModelAttribute("product") ProductRequestDto requestDto,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("productId", id);
            return "admin/edit-form";
        }

        productService.updateProduct(id, requestDto);

        return "redirect:/admin/products";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);

        return "redirect:/admin/products";
    }


}
