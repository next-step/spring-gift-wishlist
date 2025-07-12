package gift.controller.view;

import gift.dto.api.ProductUpdateRequestDto;
import gift.dto.view.ProductViewRequestDto;
import gift.entity.Product;
import gift.service.ProductService;
import jakarta.validation.Valid;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/products")
public class AdminProductViewController {

    private final ProductService productService;

    public AdminProductViewController(ProductService productService) {
        this.productService = productService;
    }

    // 상품 목록 화면
    @GetMapping
    public String getProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "products/admin/list";     // templates/product/list.html
    }

    // 상품 등록 폼 화면
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("productRequest", new ProductViewRequestDto());
        return "products/admin/form";     // templates/product/admin/form.html
    }

    // 상품 등록 요청 처리
    @PostMapping("/new")
    public String createProduct(
        @Valid @ModelAttribute("productRequest") ProductViewRequestDto request,
        BindingResult bindingResult,
        Model model) {
        if (bindingResult.hasErrors()) {
            return "products/admin/form";     // 유효성 오류 있으면 다시 폼으로
        }

        try {
            Product product = new Product(
                request.getName(),
                request.getPrice(),
                request.getImageUrl()
            );

            productService.registerProduct(product);
            return "redirect:/admin/products";
        } catch (IllegalArgumentException e) {
            // 예외 메세지를 모델에 추가
            model.addAttribute("errorMessage", e.getMessage());
            return "products/admin/form";
        }
    }

    // 상품 개별 조회 요청 처리
    @GetMapping("/{id}")
    public String viewProductDetail(@PathVariable Long id,
        Model model,
        RedirectAttributes ra) {
        try {
            Product product = productService.getProductById(id);
            model.addAttribute("product", product);
            return "products/admin/detail";
        } catch (NoSuchElementException e) {
            ra.addFlashAttribute("errorMsg", "상품을 찾을 수 없습니다.");
            return "redirect:/admin/products";
        }
    }

    // 상품 수정 폼 보여주기
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);

        ProductViewRequestDto dto = new ProductViewRequestDto();
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setImageUrl(product.getImageUrl());

        model.addAttribute("productRequest", dto);
        model.addAttribute("productId", id);  // 수정 시 필요한 ID

        return "products/admin/form";
    }

    // 상품 수정 요청 처리
    @PostMapping("/{id}/edit")
    public String updateProduct(@PathVariable Long id,
        @Valid @ModelAttribute("productRequest") ProductViewRequestDto dto,
        BindingResult bindingResult,
        Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("productId", id);
            return "products/admin/form";
        }

        try {
            // dto를 변환해서 넘김
            ProductUpdateRequestDto updateDto = new ProductUpdateRequestDto(
                dto.getName(), dto.getPrice(), dto.getImageUrl()
            );

            productService.updateProduct(id, updateDto);
            return "redirect:/admin/products";
        } catch (IllegalArgumentException e) {
            // 예외 메세지를 모델에 추가
            model.addAttribute("errorMessage", e.getMessage());
            return "products/admin/form";
        }

    }

    // 상품 삭제 요청 처리
    @PostMapping("/{id}/delete")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id); // 서비스 재사용
        return "redirect:/admin/products"; // 삭제 후 목록 페이지로 이동
    }
}
