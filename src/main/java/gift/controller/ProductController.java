package gift.controller;

import gift.dto.ProductDto;
import gift.repository.ProductRepository;

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

    private final ProductRepository repository;

    private final String IMAGE_BASE_URL = "https://media.istockphoto.com/id/1667499762/ko/%EB%B2%A1%ED%84%B0/%EC%98%81%EC%97%85%EC%A4%91-%ED%8C%90%EC%A7%80-%EC%83%81%EC%9E%90.jpg?s=612x612&w=0&k=20&c=94uRFQLclgFtnDhE4OfO1tCJdETL3uuBM9ZHD_N4P4Y=";

    public ProductController(ProductRepository repository) {
        this.repository = repository;
    }

    @GetMapping // 전체 상품 조회 API
    public String getProducts(Model model) {
        model.addAttribute("products", repository.findAll());
        return "admin/product_list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("productDto",new ProductDto());
        model.addAttribute("editMode",false);
        return "admin/product_form";
    }

    @PostMapping
    public String createProduct(@Valid @ModelAttribute ProductDto productdto, BindingResult bindingResult, Model  model) {
        if(bindingResult.hasErrors()) {
            if (productdto.getName() != null && productdto.getName().contains("카카오") && !productdto.getUsableKakao()) {
                model.addAttribute("showKakaoPopup", true);
            }
            return "admin/product_form";
        }

        if(productdto.getImageUrl() == null || productdto.getImageUrl().isEmpty()) {
            productdto.setImageUrl(IMAGE_BASE_URL);
        }
        repository.save(productdto);
        return "redirect:/admin/products";
    }

    @GetMapping("/{id}/edit")
    public String editProduct(@PathVariable Long id, Model model) {
        ProductDto productDto = repository.findById(id).orElseThrow();
        model.addAttribute("productDto", productDto);
        model.addAttribute("editMode", true);
        return "/admin/product_form";
    }

    @PostMapping("/{id}")
    public String updateProduct(@ModelAttribute ProductDto productDto) {
        repository.update(productDto);
        return "redirect:/admin/products";
    }

    @PostMapping("/{id}/delete")
    public String deleteProduct(@PathVariable Long id) {
        repository.delete(id);
        return "redirect:/admin/products";

    }

}