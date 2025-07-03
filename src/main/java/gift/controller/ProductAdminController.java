package gift.controller;

import gift.dto.ProductAdminRequestDto;
import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.service.ProductService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
@RequestMapping("/admin/products")
public class ProductAdminController {

  private final ProductService productService;

  public ProductAdminController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping
  public String productList(Model model) {
    List<ProductResponseDto> products = productService.searchAllProducts();
    model.addAttribute("products", products);
    return "admin/product-list";
  }

  @GetMapping("/new")
  public String showCreateForm() {
    return "admin/product-form";
  }

  @PostMapping
  public String create(@Valid @ModelAttribute ProductAdminRequestDto productAdminRequestDto) {
    productService.createAdminProduct(productAdminRequestDto);
    return "redirect:/admin/products";
  }

  @PostMapping(consumes = "application/json", produces = "application/json")
  @ResponseBody
  public ResponseEntity<ProductResponseDto> createUsingJson(@RequestBody @Valid ProductAdminRequestDto productAdminRequestDto) {
    ProductResponseDto createdProduct = productService.createAdminProduct(productAdminRequestDto);

    URI location = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(createdProduct.id())
        .toUri();

    return ResponseEntity
        .created(location)
        .body(createdProduct);
  }

  @GetMapping("/{id}/edit")
  public String showEditForm(@PathVariable Long id, Model model) {
    model.addAttribute("id", id);
    return "admin/product-update";
  }

  @PostMapping("/{id}")
  public String update(@PathVariable Long id, @Valid @ModelAttribute ProductAdminRequestDto productAdminRequestDto) {
    productService.updateAdminProduct(id, productAdminRequestDto);
    return "redirect:/admin/products";
  }

  @PostMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
  @ResponseBody
  public ResponseEntity<ProductResponseDto> updateUsingJson(@PathVariable Long id,
      @RequestBody @Valid ProductAdminRequestDto productAdminRequestDto) {
    return ResponseEntity.ok(productService.updateAdminProduct(id, productAdminRequestDto));
  }

  @PostMapping("/{id}/delete")
  public String delete(@PathVariable Long id) {
    productService.deleteProduct(id);
    return "redirect:/admin/products";
  }
}
