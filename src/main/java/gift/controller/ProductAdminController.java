package gift.controller;


import gift.dto.ProductAdminRequestDto;
import gift.entity.Product;
import gift.service.ProductAdminService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;


@Controller
@RequestMapping("/admin/products")
public class ProductAdminController {

    private final ProductAdminService productAdminService;

    //생성자 주입

    public ProductAdminController(ProductAdminService productAdminService) {
        this.productAdminService = productAdminService;
    }


    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productAdminService.findAll());
        return "product/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new ProductAdminRequestDto());
        return "product/form";
    }

    @PostMapping
    public String createProduct(@ModelAttribute ProductAdminRequestDto dto) {
        productAdminService.save(dto);
        return "redirect:/admin/products";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productAdminService.findById(id);
        ProductAdminRequestDto dto = new ProductAdminRequestDto(product.getName(), product.getPrice(),
                product.getImageUrl());
        model.addAttribute("product", dto);
        model.addAttribute("id", id);
        return "product/edit";
    }

    @PostMapping("/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute ProductAdminRequestDto dto) {
        productAdminService.update(id, dto);
        return "redirect:/admin/products";
    }

    @PostMapping("/{id}/delete")
    public String deleteProduct(@PathVariable Long id) {
        productAdminService.deleteProductById(id);
        return "redirect:/admin/products";
    }


}

