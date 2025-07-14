package gift.service;

import gift.auth.JwtAuth;
import gift.dto.ProductResponseDto;
import gift.dto.WishListProductRequestDto;
import gift.entity.Product;
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
    private final ProductRepositoryInterface productRepository;
    private final JwtAuth jwtAuth;

    public WishListService(@Qualifier("WishListRepository") WishListRepositoryInterface wishListRepository, @Qualifier("jdbcProductRepository") ProductRepositoryInterface productRepository,JwtAuth jwtAuth) {
        this.wishListRepository = wishListRepository;
        this.productRepository = productRepository;
        this.jwtAuth = jwtAuth;
    }

    @Override
    public List<ProductResponseDto> findAllProductsFromWishList(String email) {
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
        Long productId = requestDto.getproductId();
        wishListRepository.addProductToWishListByEmail(email, productId);

        return findAllProductsFromWishList(email);
    }

    @Override
    public void deleteProductFromWishList(String email, Long productId) {
        boolean deleted = wishListRepository.deleteProductFromWishListByEmail(email, productId);
        if(!deleted) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
