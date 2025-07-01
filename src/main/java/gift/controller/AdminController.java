package gift.controller;

import gift.dto.ProductRequestDto;
import gift.entity.Product;
import gift.service.ProductService;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    //생성한 product는 HashMap에 저장
    @PostMapping("/products/add")
    public String createProduct(@ModelAttribute ProductRequestDto requestDto) {
        if (checkProduct(requestDto)) {
            productService.add(requestDto);
            return "redirect:/admin/products/list"; //GetMapping 되어 있는 것을 호출,,,?
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "가격은 음수가 될 수 없으며, 상품명, 가격, 이미지 주소는 필수 값입니다.");
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
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 상품입니다.");
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
        Product product = findProductById(id).get();
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
        String sql = "update products set name = ?, price = ?, image_url = ? where id = ?";
        //jdbcTemplate.update(sql, requestDto.getName(), requestDto.getPrice(),requestDto.getImageUrl(), id);
        return "redirect:/admin/products/list";
    }

    //delete
    //등록된 상품을 삭제
    @PostMapping("/products/remove/{id}")
    public String removeProduct(@PathVariable Long id) {
        productService.remove(id);
        return "redirect:/admin/products/list";
    }

    public boolean checkProduct(ProductRequestDto requestDto) {
        //상품명, 가격, 이미지의 경우 모두 필수 값
        if (requestDto.getName() == null || requestDto.getPrice() == null
                || requestDto.getImageUrl() == null) {
            return false;
        }
        //가격은 0이하가 될 수 없음
        else if (requestDto.getPrice() < 0) {
            return false;
        }
        return true;
    }

    public Optional<Product> findProductById(Long id) {
        String sql = "select * from products where id = ?";
        //List<Product> productList = jdbcTemplate.query(sql, productRowMapper(), id);
        //return productList.stream().findAny();
        return null;
    }

    private RowMapper<Product> productRowMapper() {
        return new RowMapper<Product>() {
            @Override
            public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
                Long id = rs.getLong("id");
                String name = rs.getString("name");
                Integer price = rs.getInt("price");
                String imageUrl = rs.getString("image_url");
                return new Product(id, name, price, imageUrl);
            }
        };
    }
}

