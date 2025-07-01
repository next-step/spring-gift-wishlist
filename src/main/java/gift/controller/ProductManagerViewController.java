package gift.controller;

import gift.dto.request.ProductRequestDto;
import gift.dto.request.ProductUpdateRequestDto;
import gift.entity.Product;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/home")
public class ProductManagerViewController {

    private final ProductService productService;

    public ProductManagerViewController(ProductService productService) {
        this.productService = productService;
    }

    //관리자 메인 화면
    @GetMapping()
    public String managerHome() {
        return "products";
    }

    //상품 단건 조회
    @GetMapping("/{productId}")
    public String getProductById(@PathVariable long productId, Model model) {
        Product product = productService.getProduct(productId);
        model.addAttribute("product", product);
        return "product"; // product.html로 이동
    }

    //상품 추가
    @PostMapping()
    public String createProduct(
        @ModelAttribute @Valid ProductRequestDto productRequestDto) {
        productService.createProduct(productRequestDto);
        return "redirect:/home";
    }


    //상품 수정(수정 페이지 이동)
    @PostMapping("/{productId}")
    public String updateProduct(
        @PathVariable long productId,
        @ModelAttribute @Valid ProductUpdateRequestDto productUpdateRequestDto) {
        productService.updateProduct(productId, productUpdateRequestDto);
        return "redirect:/home";
    }

    //상품 삭제
    @PostMapping("/delete")
    public String deleteProduct(@RequestParam long productId) {
        productService.deleteProduct(productId);
        return "redirect:/home";
    }
}
