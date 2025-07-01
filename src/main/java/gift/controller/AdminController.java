package gift.controller;

import gift.dto.ProductRequestDto;
import gift.entity.Product;
import gift.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@RequestMapping("/admin") //prefix설정
@Controller//Controller는 mvc에서 화면을 구성하기 위해서 뷰 이름을 반환하고 ViewResolver를 거치게 됩니다.
public class AdminController {


    private final ProductService productService;

    //의존성 주입(생성자가 1개인 경우 @Autowired 생략 가능)
    public AdminController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String home() {
        return "redirect:/admin/products/list";
    }

    //create
    //BindingResult: 검증 오류를 보관하는 객체
    @PostMapping("/products/add")
    public String createProduct(
            @ModelAttribute @Valid ProductRequestDto requestDto,
            BindingResult bindingResult
    ) {
        if(bindingResult.hasErrors()){
            return "form";
        }
        productService.add(requestDto);
        return "redirect:/admin/products/list"; //GetMapping 되어 있는 것을 호출,,,?
    }

    //form.html을 불러오기 위한 메서드
    @GetMapping("/products/add")
    public String productForm() {
        return "form";
    }

    //read
    //특정 상품을 조회(id)
    @GetMapping("/products/info")
    public String getProduct(
            @RequestParam Long id,
            Model model
    ) {
        Optional<Product> product = productService.findOne(id);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            return "productinfo";
        }
        String errorMsg = "상품 ID가 " + id + "인 상품은 존재하지 않습니다.";
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMsg);
    }

    //read
    //전체 상품을 조회
    @GetMapping("/products/list")
    public String getProducts(Model model) {
        List<Product> productList = productService.findAll();
        model.addAttribute("productList", productList);
        return "home";
    }

    //modify.html을 불러오기 위한 메서드
    @GetMapping("/products/modify/{id}")
    public String modifyForm(
            @PathVariable Long id,
            Model model
    ) {
        Product product = productService.findOne(id).get();
        model.addAttribute("product", product);
        return "modify";
    }

    //update
    //상품 수정
    @PutMapping("/products/modify/{id}")
    public String modifyProduct(
            @ModelAttribute ProductRequestDto requestDto,
            @PathVariable Long id
    ) {
        productService.modify(id, requestDto);
        return "redirect:/admin/products/list";
    }

    //delete
    //등록된 상품을 삭제
    @PostMapping("/products/remove/{id}")
    public String removeProduct(@PathVariable Long id) {
        productService.remove(id);
        return "redirect:/admin/products/list";
    }

}

