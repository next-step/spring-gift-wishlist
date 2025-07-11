package gift.service;

import gift.dto.WishRequestDTO;
import gift.dto.WishResponseDTO;
import gift.dto.WishUpdateDTO;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wish;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishService {
    private final WishRepository wishRepository;
    private final ProductRepository productRepository;

    public WishService(WishRepository wishRepository, ProductRepository productRepository) {
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
    }

    public WishResponseDTO addWish(WishRequestDTO wishRequestDTO, Member member) {
        Long productId = wishRequestDTO.productId();
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("해당 상품을 찾을 수 없습니다."));

        return wishRepository.findByMemberIdAndProductId(member.getId(), productId)
            .map(existingWish -> {
                int newQuantity = existingWish.getQuantity() + wishRequestDTO.quantity();
                wishRepository.updateQuantity(member.getId(), productId, newQuantity);
                return new WishResponseDTO(member.getId(), product.getProductResponseDTO(), newQuantity);
            })
            .orElseGet(() -> {
                wishRepository.save(new Wish(member.getId(), productId, wishRequestDTO.quantity()));
                return new WishResponseDTO(member.getId(), product.getProductResponseDTO(), wishRequestDTO.quantity());
            });
    }

    public List<WishResponseDTO> getWishes(Member member, int page, int size, String sort) {
        long offset = (long) page * size;
        return wishRepository.findByMemberIdWithPagination(member.getId(), size, offset, sort);
    }

    public void updateWishQuantity(Long productId, WishUpdateDTO wishUpdateDTO, Member member) {
        wishRepository.findByMemberIdAndProductId(member.getId(), productId)
            .orElseThrow(() -> new IllegalArgumentException("해당 상품이 위시리스트에 없습니다."));

        int newQuantity = wishUpdateDTO.quantity();
        if (newQuantity == 0) {
            wishRepository.deleteByMemberIdAndProductId(member.getId(), productId);
        } else {
            wishRepository.updateQuantity(member.getId(), productId, newQuantity);
        }
    }

    public void deleteWish(Long productId, Member member) {
        wishRepository.findByMemberIdAndProductId(member.getId(), productId)
            .orElseThrow(() -> new IllegalArgumentException("해당 상품이 위시리스트에 없습니다."));

        wishRepository.deleteByMemberIdAndProductId(member.getId(), productId);
    }
}