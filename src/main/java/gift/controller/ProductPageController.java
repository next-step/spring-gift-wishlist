package gift.controller;

import gift.domain.Product;
import gift.dto.ProductMapper;
import gift.dto.ProductRequest;
import gift.service.ProductServiceAdmin;
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

@Controller
@RequestMapping("/admin/products")
public class ProductPageController {

    private final ProductServiceAdmin productServiceAdmin;

    public ProductPageController(ProductServiceAdmin productServiceAdmin) {
        this.productServiceAdmin = productServiceAdmin;
    }

    // 메인 페이지: 상품 목록 조회
    @GetMapping("")
    public String findAll(Model model){
        List<Product> list = productServiceAdmin.getProductListAdmin();
        model.addAttribute("productList", list);
        return "main";
    }

    // 상품 등록 페이지 이동
    @GetMapping("/new")
    public String showNewProductForm(
        Model model
    ){
        model.addAttribute("request", ProductRequest.createForNewProductForm());
        return "newProduct";
    }

    // 상품 등록
    @PostMapping("")
    public String createProduct(
        @Valid @ModelAttribute("request") ProductRequest request,
        BindingResult bindingResult,
        Model model
        ) {

        if(bindingResult.hasErrors()){
            return "newProduct";
        }

        Product product = ProductMapper.toEntity(request);
        productServiceAdmin.insertAdmin(product);
        return "redirect:/admin/products";
    }

    // 상품 수정 페이지 이동
    @GetMapping("/update/{productId}")
    public String updateFormProduct(
        @PathVariable Long productId,
        Model model
    ){
        Product product = productServiceAdmin.getProductByIdAdmin(productId);
        ProductRequest request = new ProductRequest(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl()
        );

        model.addAttribute("request", request);
        return "update";
    }

    // 상품 수정(수정 처리)
    @PostMapping("/update/{productId}")
    public String updateProduct(
        @PathVariable Long productId,
        @ModelAttribute ProductRequest request
    ){
        productServiceAdmin.updateAdmin(request);

        return "redirect:/admin/products";
    }

    // 상품 삭제
    @PostMapping("/delete/{productId}")
    public String deleteProduct(
        @PathVariable Long productId
    ){
        productServiceAdmin.deleteByIdAdmin(productId);

        return "redirect:/admin/products";
    }
}
