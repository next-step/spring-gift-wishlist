package gift.Controller;

import gift.Entity.Member;
import gift.Entity.Product;
import gift.annotation.LoginMember;
import gift.dto.ProductDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ProductViewController {

    private final ProductDao productDao;

    public ProductViewController(ProductDao productDao) {
        this.productDao = productDao;
    }

    /*
    요런 식으로 구성이 되면
    LoginViewController에서 redirection하기 때문에 값이 제대로 전달이 되지 않음 따라서
    자꾸 member객체는 전부 null로 인식됨
    // 상품 목록 페이지
    @GetMapping
    public String list(Model model) {
        List<Product> products = productDao.showProducts();
        model.addAttribute("products", products);
        return "products/list";
    }
    */

    //따라서 로그인된 Member의 정보를 가져오기
    @GetMapping("/user/products")
    public String list(Model model, @LoginMember Member member) {
        List<Product> products = productDao.showProducts();
        model.addAttribute("products", products);

        model.addAttribute("member", member);

        return "products/list";
    }
}