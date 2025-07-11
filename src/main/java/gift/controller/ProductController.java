package gift.controller;

import gift.dto.product.ProductRequestDto;

import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
@RequestMapping("/admin/products")

public class ProductController {

    private final ProductService service;
    public ProductController(ProductService service){
        this.service = service;
    }

    @GetMapping // 전체 상품 조회 API
    public String getProducts(Model model) {
        model.addAttribute("products", service.findAllProducts());
        return "admin/product_list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("productRequestDto",new ProductRequestDto());
        model.addAttribute("editMode",false);
        return "admin/product_form";
    }

    @PostMapping
    public String createProduct(@Valid @ModelAttribute ProductRequestDto productDto, BindingResult bindingResult, Model  model) {
        if(bindingResult.hasErrors()) {
            if (productDto.getName() != null && productDto.getName().contains("카카오") && !productDto.getUsableKakao()) {
                model.addAttribute("showKakaoPopup", true);
            }
            model.addAttribute("productRequestDto",productDto);
            return "admin/product_form";
        }
        
        service.createProduct(productDto);
        return "redirect:/admin/products";
    }

    @GetMapping("/{id}/edit")
    public String editProduct(@PathVariable Long id, Model model) {
        ProductRequestDto productRequestDto = service.findProduct(id);
        model.addAttribute("productRequestDto", productRequestDto);
        model.addAttribute("editMode", true);
        return "/admin/product_form";
    }

    @PostMapping("/{id}")
    public String updateProduct(@ModelAttribute ProductRequestDto productRequestDto) {
        service.updateProduct(productRequestDto);
        return "redirect:/admin/products";
    }

    @PostMapping("/{id}/delete")
    public String deleteProduct(@PathVariable Long id) {
        service.deleteProduct(id);
        return "redirect:/admin/products";

    }

}