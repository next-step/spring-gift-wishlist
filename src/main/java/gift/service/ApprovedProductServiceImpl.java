package gift.service;

import gift.repository.ApprovedProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ApprovedProductServiceImpl implements ApprovedProductService {

    private final ApprovedProductRepository approvedProductRepository;

    public ApprovedProductServiceImpl(ApprovedProductRepository approvedProductRepository) {
        this.approvedProductRepository = approvedProductRepository;
    }

    @Override
    public void saveApprovedProductName(String name) {
        approvedProductRepository.existApprovedProductName(name);
        approvedProductRepository.saveApprovedProductName(name);
    }

    @Override
    public void isApprovedProductName(String name) {
        approvedProductRepository.existApprovedProductName(name);
    }
}
