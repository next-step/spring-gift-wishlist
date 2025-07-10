package gift.service;

import gift.auth.JwtAuth;
import gift.dto.*;
import gift.entity.Member;
import gift.entity.Product;
import gift.exception.MemberExceptions;
import gift.repository.MemberRepositoryInterface;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class MemberService implements MemberServiceInterface {
    private final MemberRepositoryInterface memberRepository;
    private final JwtAuth jwtAuth;

    public MemberService(@Qualifier("MemberRepository") MemberRepositoryInterface memberRepository, JwtAuth jwtAuth) {
        this.memberRepository = memberRepository;
        this.jwtAuth = jwtAuth;
    }

    @Override
    public boolean isEmailExists(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }

    @Override
    public MemberResponseDto register(MemberRequestDto requestDto) {
        if (memberRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new MemberExceptions.EmailAlreadyExistsException(requestDto.getEmail());
        }

        Member member = new Member(requestDto.getEmail(), requestDto.getPassword());
        memberRepository.save(member);
        String token = jwtAuth.createJwtToken(member);
        return new MemberResponseDto(token);
    }

    @Override
    public MemberResponseDto login(MemberRequestDto requestDto) {
        if (memberRepository.findByEmail(requestDto.getEmail()).isEmpty()) {
            throw new MemberExceptions.MemberNotFoundException(requestDto.getEmail());
        }
        Member member = memberRepository.findByEmailAndPassword(requestDto.getEmail(), requestDto.getPassword())
                .orElseThrow(MemberExceptions.InvalidPasswordException::new);

        String token = jwtAuth.createJwtToken(member);
        return new MemberResponseDto(token);
    }

    @Override
    public List<ProductResponseDto> findAllProductsFromWishList(String token) {
        String email = jwtAuth.getEmailFromToken(token);
        List<Product> products = memberRepository.findAllProductsFromWishListByEmail(email);
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
        Long productId = requestDto.getId();
        memberRepository.addProductToWishListByEmail(email, productId);

        return findAllProductsFromWishList(token);
    }

    @Override
    public void deleteProductFromWishList(String token, Long productId) {
        String email = jwtAuth.getEmailFromToken(token);
        boolean deleted = memberRepository.deleteProductFromWishListByEmail(email, productId);
        if(!deleted) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
