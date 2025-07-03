package gift;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/products")
@Controller

public class ProductPageController {

    private final ProductStorage products;

    public ProductPageController(ProductStorage products) { this.products = products; }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", products.getProducts());
        return "Products";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("product", new Product());
        return "Productform";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute Product product, BindingResult result) {
        if (product.getName().contains("카카오")) {
            result.rejectValue("name", "invalid_kakao_in_name", "상품명에 '카카오'가 포함되어 있습니다. 담당자 확인이 필요합니다.");
        }

        if (result.hasErrors()) {
            return "Productform";
        }

        products.save(product);
        return "redirect:/products";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Product product = products.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. id=" + id));
        model.addAttribute("product", product);
        return "Productform";
    }

    @PostMapping("/{id}")
    public String edit(@Valid @PathVariable Long id, @ModelAttribute Product updated, BindingResult result) {
        if (result.hasErrors()) {
            return "Productform";
        }
        products.update(id, updated);
        return "redirect:/products";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        products.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. id=" + id));
        products.delete(id);

        return "redirect:/products";
    }

}
