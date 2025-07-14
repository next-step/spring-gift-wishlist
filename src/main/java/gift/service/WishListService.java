package gift.service;

import gift.auth.JwtAuth;
import gift.dto.ProductResponseDto;
import gift.dto.WishListProductRequestDto;
import gift.entity.Product;
import gift.exception.MemberExceptions;
import gift.exception.ProductExceptions;
import gift.repository.MemberRepositoryInterface;
import gift.repository.ProductRepositoryInterface;
import gift.repository.WishListRepositoryInterface;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class WishListService implements WishListServiceInterface {
    private final WishListRepositoryInterface wishListRepository;
    private final MemberRepositoryInterface memberRepository;
    private final ProductRepositoryInterface productRepository;

    public WishListService(@Qualifier("WishListRepository") WishListRepositoryInterface wishListRepository,
                           @Qualifier("MemberRepository") MemberRepositoryInterface memberRepository,
                           @Qualifier("jdbcProductRepository") ProductRepositoryInterface productRepository) {
        this.wishListRepository = wishListRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductResponseDto> findAllProductsFromWishList(String email) {
        if (memberRepository.findByEmail(email).isEmpty()) {
            throw new MemberExceptions.MemberNotFoundException(email);
        }
        List<Product> products = wishListRepository.findAllProductsFromWishListByEmail(email);
        List<ProductResponseDto> productResponseDtoList = new ArrayList<>();
        for (Product product : products) {
            productResponseDtoList.add(new ProductResponseDto(product.getId(),
                    product.getName(),
                    product.getPrice(),
                    product.getImageUrl()));
        }
        return productResponseDtoList;
    }

    @Override
    public List<ProductResponseDto> addProductToWishListByEmail(String email, WishListProductRequestDto requestDto) {

        if (memberRepository.findByEmail(email).isEmpty()) {
            throw new MemberExceptions.MemberNotFoundException(email);
        }
        Long productId = requestDto.getproductId();
        Product product = productRepository.findProductById(productId)
                .orElseThrow(() -> new ProductExceptions.ProductNotFoundException(productId));

        wishListRepository.addProductToWishListByEmail(email, productId);

        return findAllProductsFromWishList(email);
    }

    @Override
    public void deleteProductFromWishList(String email, Long productId) {
        if (memberRepository.findByEmail(email).isEmpty()) {
            throw new MemberExceptions.MemberNotFoundException(email);
        }
        boolean deleted = wishListRepository.deleteProductFromWishListByEmail(email, productId);
        if(!deleted) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
