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
    public List<ProductResponseDto> findAllProductsFromWishList(String token) {
        String email = jwtAuth.getEmailFromToken(token);
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
    public List<ProductResponseDto> addProductToWishListByEmail(String token, WishListProductRequestDto requestDto) {
        String email = jwtAuth.getEmailFromToken(token);
        Long productId = requestDto.getproductId();
        wishListRepository.addProductToWishListByEmail(email, productId);

        return findAllProductsFromWishList(token);
    }

    @Override
    public void deleteProductFromWishList(String token, Long productId) {
        String email = jwtAuth.getEmailFromToken(token);
        boolean deleted = wishListRepository.deleteProductFromWishListByEmail(email, productId);
        if(!deleted) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
