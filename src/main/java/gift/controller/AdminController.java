package gift.controller;

import gift.dto.ProductCreateFormDto;
import gift.dto.ProductUpdateFormDto;
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
public class AdminController {

  private final ProductService service;
  private static final String PRODUCTS_LIST_PAGE_PATH = "/admin/products";

  public AdminController(ProductService service) {
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
      model.addAttribute("createFormDto", new ProductCreateFormDto());
      model.addAttribute("validationError", "validation에 맞지 않으니 다시 입력하세요");
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
      model.addAttribute("updateFormDto", updateFormDto);
      model.addAttribute("validationError", "validation에 맞지 않으니 다시 입력하세요");
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
