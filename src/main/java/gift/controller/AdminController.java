package gift.controller;

import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.dto.UserRequestDto;
import gift.dto.UserResponseDto;
import gift.service.AuthService;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/admin")
public class AdminController {
    // 의존성 고정하여 안전하게 유지
    private final ProductService productService;
    private final AuthService authService;

    // 의존성 주입
    private AdminController(ProductService productService, AuthService authService) {
        this.productService = productService;
        this.authService = authService;
    }

    /**
     * 관리자 페이지 제품 리스트 View 출력
     * @param model 렌더링용 모델 객체
     * @return Thymeleaf template
     */
    @GetMapping("/products")
    public String productsListView(Model model) {
        model.addAttribute("products", productService.findAllProduct());
        return "products_list";
    }

    /**
     * 상품 등록 페이지 View 출력
     * @return Thymeleaf template
     */
    @GetMapping("/create")
    public String createProductView() {
        return "products_create";
    }

    /**
     * 상품 수정 페이지 View 출력
     * @param id 상품 번호
     * @param model 상품Dto 전달할 개체
     * @return Thymeleaf template
     */
    @GetMapping("/patch/{id}")
    public String patchProductView(@PathVariable Long id, Model model) {
        ProductResponseDto responseDto = productService.findProductById(id);
        model.addAttribute("product", responseDto);
        return "products_patch";
    }

    /**
     * 삭제 버튼 구현
     * @param id 상품 번호
     * @return list로 redirect
     */
    @PostMapping("/delete/{id}")
    public String deleteProductView(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products"; // 목록으로 리다이렉트
    }

    /**
     * 상품 생성 구현
     * @param requestDto 상품 요청
     * @return list로 redirect
     */
    @PostMapping("/create")
    public String createProduct(@Valid @ModelAttribute ProductRequestDto requestDto) {
        productService.saveProduct(requestDto);
        return "redirect:/admin/products";
    }


    /**
     * 상품 수정 구현
     * @param id 상품 번호
     * @param requestDto 상품 요청
     * @return list로 redirect
     */
    @PostMapping("/patch/{id}")
    public String patchProduct(@PathVariable Long id, @Valid @ModelAttribute ProductRequestDto requestDto) {
        productService.updateProduct(id, requestDto);
        return "redirect:/admin/products";
    }



 /////////////  유저 리스트 //////////////////////////

    /**
     * 유저 리스트 페이지 View 출력
     * @param model 렌더링용 모델 객체
     * @return Thymeleaf template
     */
    @GetMapping("/users")
    public String usersListView(Model model) {
        model.addAttribute("users", authService.findAllUsers());
        return "users_list";
    }

    /**
     * 유저 등록 페이지 View 출력
     * @return Thymeleaf template
     */
    @GetMapping("/users/create")
    public String createUserView() {
        return "users_create";
    }

    /**
     * 유저 수정 페이지 View 출력
     * @param id 유저 번호
     * @param model 유저Dto 전달할 개체
     * @return Thymeleaf template
     */
    @GetMapping("/users/patch/{id}")
    public String patchUserView(@PathVariable Long id, Model model) {
        UserResponseDto responseDto = authService.findUserById(id);
        model.addAttribute("user", responseDto);
        return "users_patch";
    }

    /**
     * 삭제 버튼 구현
     * @param id 유저 번호
     * @return list로 redirect
     */
    @PostMapping("/users/delete/{id}")
    public String deleteUserView(@PathVariable Long id) {
        authService.deleteUser(id);
        return "redirect:/admin/users"; // 목록으로 리다이렉트
    }

    /**
     * 유저 생성 구현
     * @param requestDto 유저 정보
     * @return list로 redirect
     */
    @PostMapping("/users/create")
    public String createUser(@Valid @ModelAttribute UserRequestDto requestDto) {
        authService.userSignUp(requestDto);
        return "redirect:/admin/users";
    }

    /**
     * 유저 수정 구현
     * @param id 유저 번호
     * @param requestDto 상품 정보
     * @return list로 redirect
     */
    @PostMapping("/users/patch/{id}")
    public String patchUser(@PathVariable Long id, @Valid @ModelAttribute UserRequestDto requestDto) {
        authService.updateUser(id, requestDto);
        return "redirect:/admin/users";
    }


}
