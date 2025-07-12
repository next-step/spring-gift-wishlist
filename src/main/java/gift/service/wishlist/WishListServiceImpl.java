package gift.service.wishlist;

import gift.dto.product.ProductResponseDto;
import gift.entity.Product;
import gift.entity.Wish;
import gift.exception.ResourceNotFoundException;
import gift.repository.product.ProductRepository;
import gift.repository.wishlist.WishListRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class WishListServiceImpl implements WishListService {

    private final WishListRepository wishListRepository;
    private final ProductRepository productRepository;

    public WishListServiceImpl(WishListRepository wishListRepository,
        ProductRepository productRepository) {
        this.wishListRepository = wishListRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void create(Long productId, Long memberId) {
        Wish wish = wishListRepository.create(
            new Wish(productId, memberId));
    }

    @Override
    public List<ProductResponseDto> findAll(Long memberId) {
        List<Wish> wishList = wishListRepository.findAll(memberId);
        List<ProductResponseDto> responseDtoList = new ArrayList<>();

        for (Wish wish : wishList) {
            Product product = productRepository.findById(wish.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException());

            responseDtoList.add(
                new ProductResponseDto(product.getId(), product.getName(), product.getPrice(),
                    product.getImageUrl()));
        }

        return responseDtoList.stream()
            .sorted(Comparator.comparing(ProductResponseDto::id))
            .toList();
    }

    @Override
    public void delete(Long productId, Long memberId) {
        int deleteRow = wishListRepository.delete(productId, memberId);

        if (deleteRow <= 0) {
            throw new ResourceNotFoundException();
        }
    }
}
