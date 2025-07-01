package gift.product.controller;

import gift.product.domain.Product;
import gift.product.dto.ProductEditDto;
import gift.product.dto.ProductInfoDto;
import gift.product.dto.ProductRequestDto;
import gift.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class ProductAdminController {

    private final ProductService productService;

    public ProductAdminController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping
    public String products(Model model) {
        List<ProductInfoDto> products = productService.getProducts()
                .stream()
                .map(product -> ProductInfoDto.productFrom(product))
                .toList();

        model.addAttribute("products", products);

        return "admin/products";
    }

    @GetMapping("/{id}")
    public String productDetail(@PathVariable("id") Long id, Model model){
        Product product = productService.getProduct(id);

        model.addAttribute("product", ProductInfoDto.productFrom(product));

        return "admin/product-detail";
    }

    @GetMapping("/add")
    public String addProductForm(Model model) {
        model.addAttribute("product", ProductRequestDto.getEmpty());
        return "admin/product-add-form";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute @Valid ProductRequestDto requestDto, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return "admin/product-add-form";
        }

        productService.saveProduct(requestDto);
        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable("id") Long id, Model model){
        Product product = productService.getProduct(id);

        model.addAttribute("product", new ProductEditDto(product.getName(), product.getPrice(), product.getImageUrl()));
        model.addAttribute("productId", id);

        return "admin/product-edit-form";
    }

    @PostMapping("/edit/{id}")
    public String editProduct(
            @PathVariable("id") Long id,
            @ModelAttribute @Valid ProductEditDto editDto,
            BindingResult bindingResult,
            Model model
    ){
        if(bindingResult.hasErrors()) {
            model.addAttribute("productId", id);
            return "/admin/product-edit-form";
        }

        ProductRequestDto requestDto = new ProductRequestDto(editDto.name(), editDto.price(),editDto.imageUrl());
        productService.update(id, requestDto);

        return "redirect:/admin/products/" + id;
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.delete(id);

        return "redirect:/admin/products";
    }
}
