package gift.controller;

import gift.auth.Login;
import gift.dto.WishRequestDto;
import gift.dto.WishResponseDto;
import gift.entity.Member;
import gift.entity.Wish;
import gift.service.WishService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishes")
public class WishController {
    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping
    public ResponseEntity<List<WishResponseDto>> getWishes(@Login Member member) {
        return new ResponseEntity<>(wishService.getWishes(member.getId()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> addWish(
            @Login Member member,
            @Valid @RequestBody WishRequestDto wishRequestDto
    ) {
        wishService.addWish(member.getId(), wishRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
