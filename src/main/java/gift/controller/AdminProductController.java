package gift.controller;

import gift.dto.product.ProductCreateFormDto;
import gift.dto.product.ProductUpdateFormDto;
import gift.entity.Product;
import gift.service.product.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

  private static final String PRODUCTS_LIST_PAGE_PATH = "/admin/products";

  private final ProductService service;

  public AdminProductController(ProductService service) {
    this.service = service;
  }

  @GetMapping
  public String findAllProduct(Model model) {
    List<Product> responseDtoList = service.findAllProduct();
    model.addAttribute("responseDtoList", responseDtoList);
    return "list";
  }

  @GetMapping("/new")
  public String createForm(Model model) {
    model.addAttribute("createFormDto", new ProductCreateFormDto());
    return "createProductForm";
  }

  @PostMapping("/new")
  public String create(@Valid @ModelAttribute ProductCreateFormDto createFormDto,
      BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors()) {
      String errorMessage = bindingResult
          .getFieldErrors()
          .stream()
          .findFirst()
          .map(fieldError -> fieldError.getDefaultMessage())
          .orElse("잘못된 요청입니다.");
      model.addAttribute("createFormDto", createFormDto);
      model.addAttribute("validationError", errorMessage);
      return "createProductForm";
    }
//    ProductRequestDto requestDto = new ProductRequestDto(createFormDto.getName(),
//        createFormDto.getPrice(), createFormDto.getImageUrl());
    Product product = new Product(createFormDto.getName(), createFormDto.getPrice(),
        createFormDto.getImageUrl());
    service.createProduct(product);
    return "redirect:" + PRODUCTS_LIST_PAGE_PATH;
  }

  @GetMapping("/{id}/update")
  public String updateForm(@PathVariable("id") Long id, Model model) {
    Product responseDto = service.findProductById(id);
    ProductUpdateFormDto updateFormDto = new ProductUpdateFormDto(responseDto.getId(),
        responseDto.getName(), responseDto.getPrice(), responseDto.getImageUrl());
    model.addAttribute("updateFormDto", updateFormDto);
    return "updateProductForm";
  }

  @PutMapping("/{id}/update")
  public String update(@PathVariable("id") Long id,
      @Valid @ModelAttribute ProductUpdateFormDto updateFormDto, BindingResult bindingResult,
      Model model) {
    if (bindingResult.hasErrors()) {
      Product responseDto = service.findProductById(id);
      ProductUpdateFormDto newUpdateFormDto = new ProductUpdateFormDto(responseDto.getId(),
          responseDto.getName(), responseDto.getPrice(), responseDto.getImageUrl());

      String errorMessage = bindingResult
          .getFieldErrors()
          .stream()
          .findFirst()
          .map(fieldError -> fieldError.getDefaultMessage())
          .orElse("잘못된 요청입니다.");
      model.addAttribute("updateFormDto", updateFormDto);
      model.addAttribute("validationError", errorMessage);
      return "updateProductForm";
    }
    service.updateProduct(id,
        new Product(updateFormDto.getName(), updateFormDto.getPrice(),
            updateFormDto.getImageUrl()));
    return "redirect:" + PRODUCTS_LIST_PAGE_PATH;
  }

  @DeleteMapping("/{id}/delete")
  public String delete(@PathVariable("id") Long id) {
    service.deleteProduct(id);
    return "redirect:" + PRODUCTS_LIST_PAGE_PATH;
  }

}
