package gift.service;

import gift.repository.ApprovedProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ApprovedProductServiceImpl implements ApprovedProductService {

    private final ApprovedProductRepository approvedProductRepository;

    public ApprovedProductServiceImpl(ApprovedProductRepository approvedProductRepository) {
        this.approvedProductRepository = approvedProductRepository;
    }

    @Override
    public void saveApprovedProductName(String name) {
        if (approvedProductRepository.existApprovedProductName(name)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "해당 상품명은 등록되어 있습니다.");
        }

        approvedProductRepository.saveApprovedProductName(name);
    }

    @Override
    public boolean isApprovedProductName(String name) {
        return approvedProductRepository.existApprovedProductName(name);
    }
}
