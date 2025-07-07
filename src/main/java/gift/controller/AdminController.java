package gift.controller;

import gift.dto.ProductRequestDto;
import gift.entity.Product;
import gift.exception.ProductNotFoundException;
import gift.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/admin") //prefix설정
@Controller//Controller는 mvc에서 화면을 구성하기 위해서 뷰 이름을 반환하고 ViewResolver를 거치게 됩니다.
public class AdminController {


    private final ProductService productService;

    //의존성 주입(생성자가 1개인 경우 @Autowired 생략 가능)
    public AdminController(ProductService productService){
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
            BindingResult bindingResult,
            Model model
    ) {

        if(bindingResult.hasErrors()){
            model.addAttribute("requestDto", requestDto);
            return "form";
        }

        //등록된 상품페이지로 이동하도록 수정
        Long id = productService.add(requestDto);
        return "redirect:/admin/products/info?id=" + id;
    }

    //form.html을 불러오기 위한 메서드
    @GetMapping("/products/add")
    public String productForm(Model model) {
        model.addAttribute("productRequestDto", new ProductRequestDto());
        model.addAttribute("requestDto", new ProductRequestDto());
        return "form";
    }

    //read
    //특정 상품을 조회(id)
    @GetMapping("/products/info")
    public String getProduct(
            @RequestParam(required = false) Long id,
            Model model
    ) {
        //상품 검색하기에 아무런 id를 입력하지 않은 경우 -> 전체 상품을 조회하는 페이지로 이동
        if(id == null){
            return "redirect:/admin";
        }

        Optional<Product> product = productService.findOne(id);
        if (product.isEmpty()) {
            String errorMsg = "상품 ID가 " + id + "인 상품은 존재하지 않습니다.";
            throw new ProductNotFoundException(errorMsg);
        }
        model.addAttribute("product", product.get());
        return "productinfo";
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
        model.addAttribute("productRequestDto", new ProductRequestDto());
        return "modify";
    }

    //update
    //상품 수정
    @PutMapping("/products/modify/{id}")
    public String modifyProduct(
            @ModelAttribute @Valid ProductRequestDto requestDto,
            BindingResult bindingResult,
            @PathVariable Long id,
            Model model
    ) {
        //"카카오"가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있다
        if (requestDto.getName().contains("카카오")){
            String prohibitedWordMsg = "\"카카오\"가 포함된 문구는 담당 MD와 협의가 필요합니다.";
            bindingResult.addError(new FieldError("productRequestDto", "name", prohibitedWordMsg));
        }

        if(bindingResult.hasErrors()){
            //System.out.println(bindingResult);
            Product product = productService.findOne(id).get();
            model.addAttribute("product", product);
            return "modify";
        }

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

