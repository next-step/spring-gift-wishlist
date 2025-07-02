package gift.controller;

import gift.dto.request.ApprovedProductCreateRequestDto;
import gift.service.ApprovedProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class ApprovedProductController {

    private final ApprovedProductService approvedProductService;

    public ApprovedProductController(ApprovedProductService approvedProductService) {
        this.approvedProductService = approvedProductService;
    }

    @PostMapping("/approved-products")
    public ResponseEntity<Void> addApprovedProduct(
        @Valid @RequestBody ApprovedProductCreateRequestDto approvedProductCreateRequestDto) {

        approvedProductService.saveApprovedProductName(approvedProductCreateRequestDto.name());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
